package com.github.jusm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.github.jusm.listener.StartupApplicationListener.StartupJob;

@Component
public class FlashDB implements StartupJob {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisService redisService;

	@Override
	public void run(ContextRefreshedEvent event) {
		logger.info("即将清除redis数据");
		String flushDB = redisService.flushDB();
		logger.info(flushDB);
	}

}
