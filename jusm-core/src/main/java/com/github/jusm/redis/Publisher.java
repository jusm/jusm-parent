package com.github.jusm.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class Publisher {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private RedisTemplate<String, Serializable> redisTemplate;

	public Publisher(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * @param channel
	 *            频道
	 * @param message
	 *            消息
	 */
	public void publish(String channel, String message) {
		logger.info("channel:" + channel);
		logger.info("message:" + message);
		redisTemplate.convertAndSend(channel, message);
	}

	public RedisTemplate<String, Serializable> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
