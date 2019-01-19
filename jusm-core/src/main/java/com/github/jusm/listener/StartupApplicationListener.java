package com.github.jusm.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 系统启动监听器 系统启动完后 确切的将是spring初始化完成之后触发
 */
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("应用程序Spring上下文已经初始化完成,开始USM的启动任务 ");
		Map<String, StartupJob> beansMap = event.getApplicationContext().getBeansOfType(StartupJob.class);
		Collection<StartupJob> beans = beansMap.values();
		for (Iterator<StartupJob> iterator = beans.iterator(); iterator.hasNext();) {
			StartupJob startupJob = iterator.next();
			startupJob.run(event);
		}
	}

	public static interface StartupJob {
		void run(ContextRefreshedEvent event);
	}
}