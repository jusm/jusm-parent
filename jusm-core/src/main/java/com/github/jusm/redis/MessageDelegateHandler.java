package com.github.jusm.redis;

import java.io.Serializable;

public interface MessageDelegateHandler{
	
	// public void handleMessage(String message);
	//
	// public void handleMessage(Map<?, ?> message);
	//
	// public void handleMessage(byte[] message);
	//
	// public void handleMessage(Serializable message);

	/**
	 * 从频道 接收到的消息 (收到channel主题的消息)
	 * Receive Message from the channel
	 * @param message
	 * @param channel
	 */
	public void handleMessage(Serializable message, String channel);
}