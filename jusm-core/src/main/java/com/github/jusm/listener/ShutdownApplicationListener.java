package com.github.jusm.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ShutdownApplicationListener implements ApplicationListener<ContextClosedEvent> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.info("应用程序Spring上下文即将销毁,开始USM的关闭任务 ");
		Map<String, ShutdownJob> beansMap = event.getApplicationContext().getBeansOfType(ShutdownJob.class);
		Collection<ShutdownJob> beans = beansMap.values();
		for (Iterator<ShutdownJob> iterator = beans.iterator(); iterator.hasNext();) {
			ShutdownJob jobOnShutdown = iterator.next();
			jobOnShutdown.run(event);
		}
	}
	
	public interface ShutdownJob{
		void run(ContextClosedEvent event);
	}

}
