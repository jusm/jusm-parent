package com.github.jusm.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

public class RedisTemplateFacade {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	
	public long add(String key,String value,long exipre) {
		
		if(stringRedisTemplate.opsForSet().getOperations().expire(key, exipre , TimeUnit.SECONDS)) {
			return stringRedisTemplate.opsForSet().add(key, value);	
		}
		return -1;
	}
	
		
}
