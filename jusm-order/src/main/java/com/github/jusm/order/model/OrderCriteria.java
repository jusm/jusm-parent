package com.github.jusm.order.model;

import com.github.jusm.support.PeriodSupport;

public class OrderCriteria extends PeriodSupport {

	private int[] status;
	
	private int receiveType;

	private String id;
	
	private String shippingName;
	
	private String userId;
	
	public int[] getStatus() {
		return status;
	}
	public void setStatus(int[] status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
	}
	
}
