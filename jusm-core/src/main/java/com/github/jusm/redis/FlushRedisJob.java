package com.github.jusm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.jusm.listener.StartupApplicationListener.StartupJob;

/**
 * 清除redis缓存
 */
public class FlushRedisJob implements StartupJob {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public void run(ContextRefreshedEvent event) {
		logger.info("即将清除redis数据");
		String flushDB = redisRepository.flushDB();
		logger.info(flushDB);
	}

}
