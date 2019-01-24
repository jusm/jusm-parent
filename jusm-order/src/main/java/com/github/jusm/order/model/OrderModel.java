package com.github.jusm.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.jusm.order.entity.OrderItem;
import com.github.jusm.order.entity.OrderShipping;

import io.swagger.annotations.ApiModel;

@ApiModel(description="订单模型")
public class OrderModel {
	
	private BigDecimal payment;
	
	private BigDecimal postFee;

	private String userId;
	
	private String buyerMessage;
	
	private String buyerNickName;
	
	private List<OrderItem> orderItems = new ArrayList<>();

	private OrderShipping orderShipping;

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getPostFee() {
		return postFee;
	}

	public void setPostFee(BigDecimal postFee) {
		this.postFee = postFee;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getBuyerNickName() {
		return buyerNickName;
	}

	public void setBuyerNickName(String buyerNickName) {
		this.buyerNickName = buyerNickName;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
	
	
}
