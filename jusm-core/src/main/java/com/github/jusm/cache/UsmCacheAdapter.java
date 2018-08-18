package com.github.jusm.cache;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class UsmCacheAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RedisTemplate<String, Serializable> redisTemplate;
	
	public UsmCacheAdapter(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String flushCache() {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					connection.flushDb();
				} catch (Exception e) {
					logger.error("清除Redis数据缓存失败");
					throw e;
				}
				return "清除Redis数据缓存成功";
			}
		});
		return result;
	}

	public RedisTemplate<String, Serializable> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
