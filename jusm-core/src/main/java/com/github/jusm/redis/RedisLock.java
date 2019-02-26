package com.github.jusm.redis;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.Jedis;

public class RedisLock implements Lock {

	private final static String lua_script_unloack = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then \r\n"
			+ "	return redis.call(\"del\",KEYS[1])\r\n" + "else\r\n" + "	return 0\r\n" + "end";

	private final ThreadLocal<String> threadLocal = new ThreadLocal<>();
	private final String key;

	private final long expire;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private JedisConnectionFactory factory;

	/**
	 * @param key
	 *            锁的key
	 * @param expire
	 *            锁的失效时间
	 */
	public RedisLock(String key, long expire) {
		this.key = key;
		this.expire = expire;
	}

	/**
	 * 阻塞式加锁
	 * 
	 * @see java.util.concurrent.locks.Lock#lock()
	 */
	@Override
	public void lock() {
		// 1.尝试加锁
		if (tryLock0()) {
			return;
		}
		// 2.加锁失败尝试加锁 不优雅的地方
		try {
			TimeUnit.MILLISECONDS.sleep(this.expire / 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 3.递归调用
		lock();
	}

	/**
	 * 解锁
	 */
	@Override
	public void unlock() {
		if (null == threadLocal.get()) {
			return;
		}
		RedisConnection conn = null;
		try {
			conn = RedisConnectionUtils.getConnection(factory);
			Jedis jedis = (Jedis) conn.getNativeConnection();
			jedis.eval(lua_script_unloack, Arrays.asList(key), Arrays.asList(threadLocal.get()));
		} finally {
			RedisConnectionUtils.releaseConnection(conn, factory);
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	@Override
	public boolean tryLock() {
		String uuid = UUID.randomUUID().toString();
		RedisConnection conn = null;
		try {
			conn = RedisConnectionUtils.getConnection(factory);
			Jedis jedis = (Jedis) conn.getNativeConnection();
			String result = jedis.set(this.key, uuid, "NX", "PX", this.expire);
			if ("OK".equals(result)) {
				threadLocal.set(uuid);
			}
			return "OK".equals(result);
		} finally {
			RedisConnectionUtils.releaseConnection(conn, factory);
		}
	}

	public boolean tryLock0() {
		String uuid = UUID.randomUUID().toString();
		Boolean Boolean = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				Jedis jedis = (Jedis) connection.getNativeConnection();
				String result = jedis.set(RedisLock.this.key, uuid, "NX", "PX", RedisLock.this.expire);
				if ("OK".equals(result)) {
					threadLocal.set(uuid);
				}
				return "OK".equals(result);
			}

		});
		return Boolean.booleanValue();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
