package com.github.jusm.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.context.MessageCode;
import com.github.jusm.context.UsmContext;
import com.github.jusm.listener.StartupApplicationListener.StartupJob;
import com.github.jusm.util.UsmNotice;


/**
 * 清除USM系统 redis缓存
 */
public class UsmRedisCacheCleanJob implements StartupJob {

	@Autowired
	private RedisRepository redisTemplateFacade;

	@Override
	public void run(ContextRefreshedEvent event) {
		String pattern = UsmCacheNames.CACHE_NAME_PREFIX + "*";
		UsmNotice.notice(getClass(),"即将清除USM系统 redis缓存: " + pattern);
		UsmNotice.notice(getClass(), UsmContext.getMessage(MessageCode.USM_NOTICE_REDIS_CACHE_EVICT_TIP1),
				UsmContext.getMessage(MessageCode.USM_NOTICE_REDIS_CACHE_EVICT_TIP2));
		redisTemplateFacade.clean(pattern);
		//redisTemplateFacade.flushDB();


	}
}
