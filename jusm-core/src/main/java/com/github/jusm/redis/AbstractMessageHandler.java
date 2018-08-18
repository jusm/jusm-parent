package com.github.jusm.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMessageHandler implements MessageHandler {
	@Autowired
	private TopicManager topicManager;

	@Override
	@PostConstruct
	public boolean subscribe() {
		return topicManager.addRouter(getChannelTopic(), getBeanId());
	}

	public abstract String getChannelTopic();

	/**
	 * beanID
	 * 
	 * @return
	 */
	public String getBeanId() {
		String beanId = getClass().getSimpleName();
		return beanId.substring(0, 1).toLowerCase().concat(beanId.substring(1));
	}
}
