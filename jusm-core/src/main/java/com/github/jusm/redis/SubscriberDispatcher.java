package com.github.jusm.redis;

import java.io.Serializable;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jusm.component.SpringContextHolder;


public class SubscriberDispatcher implements MessageDelegateHandler {

	private TopicManager topicManager;

	public SubscriberDispatcher(TopicManager topicManager) {
		this.topicManager = topicManager;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handleMessage(Serializable message, String channel) {
		logger.info("Recived channel:" + channel);
		logger.info("Recived message:" + message);
		if (message != null) {
			if (message instanceof String) {
				Set<String> set = topicManager.getMessageHandler(channel);
				if (set != null && !set.isEmpty()) {
					for (String bean : set) {
						MessageHandler mh = (MessageHandler) SpringContextHolder.getBean(bean);
						mh.handle(message);
					}
				}
			}
		}
	}

}
