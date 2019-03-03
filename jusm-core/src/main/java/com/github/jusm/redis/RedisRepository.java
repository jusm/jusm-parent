package com.github.jusm.redis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 操作类
 * 
 */
public class RedisRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private StringRedisTemplate stringRedisTemplate;

	private RedisTemplate<String, Object> fastJsonRedisTemplate;

	private RedisTemplate<Object, Object> redisTemplate;

	private JedisConnectionFactory jedisConnectionFactory;

	public RedisRepository(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> fastJsonRedisTemplate,
			RedisTemplate<Object, Object> redisTemplate, JedisConnectionFactory jedisConnectionFactory) {
		this.stringRedisTemplate = stringRedisTemplate;
		this.redisTemplate = redisTemplate;
		this.fastJsonRedisTemplate = fastJsonRedisTemplate;
		this.jedisConnectionFactory = jedisConnectionFactory;
	}

	public JedisPoolConfig getRedisPoolConfig() {
		return jedisConnectionFactory.getPoolConfig();
	}

	public List<RedisClientInfo> getClientList() {
		return redisTemplate.getClientList();
	}

	public void set(byte[] key, byte[] value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void set(Map<byte[], byte[]> map) {
		redisTemplate.executePipelined(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.mSet(map);
				return null;
			}
		});
	}

	public byte[] get(byte[] key) {
		return (byte[]) redisTemplate.opsForValue().get(key);
	}

	public void delete(byte[] key) {
		redisTemplate.delete(key);
	}

	public void deleteBatch(byte[]... keys) {
		redisTemplate.executePipelined(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(keys);
			}
		});
	}

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	public String get(String key, Long expire) {
		Boolean boo = stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
		if (!boo) {
			logger.error(key + "expire:" + expire + "failed!");
		}
		return stringRedisTemplate.opsForValue().get(key);
	}

	public String flushDB() {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "清除Redis数据缓存成功";
			}
		});
		return result;
	}

	public Long incr(String key, long liveTime, TimeUnit timeUnit) {
		RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, jedisConnectionFactory);
		Long increment = entityIdCounter.getAndIncrement();
		if ((null == increment || increment.longValue() == 0) && liveTime > 0) {// 初始设置过期时间
			entityIdCounter.expire(liveTime, timeUnit);
		}
		return increment;
	}

	public long add(String key, String value, long exipre) {

		if (stringRedisTemplate.opsForSet().getOperations().expire(key, exipre, TimeUnit.SECONDS)) {
			return stringRedisTemplate.opsForSet().add(key, value);
		}
		return -1;
	}

	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	public void set(String key, String value, long exipre) {
		stringRedisTemplate.opsForValue().set(key, value, exipre, TimeUnit.SECONDS);
	}

	public Boolean setIfAbsent(String key, String value) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
	}

	public Boolean setIfAbsent(String key, String value, long exipre) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.setIfAbsent(key, value);
		return opsForValue.getOperations().expire(key, exipre, TimeUnit.SECONDS);
	}

	public long getExpire(String key, long exipre) {
		return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	public long getExpire(String key) {
		return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	public boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	public void clean(String pattern) {
		Set<String> keys = stringRedisTemplate.keys(pattern);
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String keyStr = it.next();
			logger.info("Clean string key >>> " + keyStr);
			stringRedisTemplate.delete(keyStr);
		}
		Set<String> objectKeys = fastJsonRedisTemplate.keys(pattern);
		Iterator<String> objKey = objectKeys.iterator();
		while (objKey.hasNext()) {
			Object keyStr = objKey.next();
			logger.info("Clean object key >>> " + keyStr.toString());
			redisTemplate.delete(keys);
		}
	}
}
