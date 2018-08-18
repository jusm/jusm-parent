package com.github.jusm.redis;

import java.io.Serializable;

import com.github.jusm.util.AddressUtil;


public class Message<T extends Serializable> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 消息子topic
	 */
	private String subtopic;
	
	
	/**
	 * 消息发布者
	 */
	private String publisher;
	
	/**
	 * 消息体
	 */
	private T body;
	
	public Message(T body) {
		this.body = body;
		this.publisher = AddressUtil.getHostAddress();
	}

	public Message(String subtopic, T body) {
		this.subtopic = subtopic;
		this.body = body;
		this.publisher = AddressUtil.getHostAddress();
	}

	
	public String getSubtopic() {
		return subtopic;
	}

	public void setSubtopic(String subtopic) {
		this.subtopic = subtopic;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	
}
