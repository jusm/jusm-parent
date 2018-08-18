package com.github.jusm.redis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DefaultMessageDelegateHandler extends AbstractMessageDelegateHandler {
	
	@Override
	public void handleMessage(Serializable message ,String channel) {
		// 什么都不做,只输出
		if (message == null) {
			System.err.println("null");
		} else if (message.getClass().isArray()) {
			System.err.println(Arrays.toString((Object[]) message));
		} else if (message instanceof List<?>) {
			System.err.println(message);
		} else if (message instanceof Map<?, ?>) {
			System.err.println(message);
		} else {
			System.err.println(ToStringBuilder.reflectionToString(message));
		}
		
	}
}