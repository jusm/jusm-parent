package com.github.jusm.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 责任链模式增加消息处理
 * 
 * @author Administrator
 *
 */
public abstract class AbstractMessageDelegateHandler implements MessageDelegateHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected AbstractMessageDelegateHandler successor;

	public AbstractMessageDelegateHandler getSuccessor() {
		return successor;
	}

	public void setSuccessor(AbstractMessageDelegateHandler successor) {
		this.successor = successor;
	}

	/**
	 * 将信息传递给他的下一位继承者
	 * 
	 * @param message
	 * @param channel
	 */
	protected void handleMessageToSuccessor(Serializable message, String channel ) {
		if (null != getSuccessor()) {
			successor.handleMessage(message,channel);
		} else {
			logger.info("没有继承者 消息处理到此结束");
		}
	}

}