package com.github.jusm.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

public class RedisTemplateFacade {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	public String flushDB() {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "清除Redis数据缓存成功";
			}
		});
		return result;
	}
	
	public long add(String key,String value,long exipre) {
		
		if(stringRedisTemplate.opsForSet().getOperations().expire(key, exipre , TimeUnit.SECONDS)) {
			return stringRedisTemplate.opsForSet().add(key, value);	
		}
		return -1;
	}
	
		
}
