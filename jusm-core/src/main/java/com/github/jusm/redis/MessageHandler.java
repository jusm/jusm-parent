package com.github.jusm.redis;

import java.io.Serializable;

public interface MessageHandler {

	/**
	 * 处理消息
	 * 
	 * @param message
	 */
	void handle(Serializable message);

	/**
	 * 订阅主题
	 * 
	 * @param channelTopic
	 * @return
	 */
	boolean subscribe();
}
