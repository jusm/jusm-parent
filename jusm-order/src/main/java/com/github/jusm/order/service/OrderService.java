package com.github.jusm.order.service;

import java.util.List;

import com.github.jusm.order.entity.Order;
import com.github.jusm.order.entity.OrderItem;
import com.github.jusm.order.entity.OrderShipping;

public interface OrderService {

	/**
	 * 未支付
	 */
	int NOT_PAY = 1;

	/**
	 * 已付款
	 */
	int PAYED = 2;

	/**
	 * 未发货
	 */
	int NOT_SEND = 3;

	/**
	 * 已发货
	 */
	int SENDED = 4;

	/**
	 * 交易完成
	 */
	int COMPLETE = 5;

	/**
	 * 订单关闭
	 */
	int COLSE = 6;

	/**
	 * 需要快递
	 * 
	 * @param order
	 * @param orderItems
	 * @param orderShipping
	 * @return
	 */
	String create(Order order, List<OrderItem> orderItems, OrderShipping orderShipping);

	/**
	 * 不需要快递
	 * 
	 * @param order
	 * @param orderItems
	 * @return
	 */
	String create(Order order, List<OrderItem> orderItems);

	/**
	 * 生成订单编码
	 * 
	 * @return
	 */
	String generateID();

	Order findByOut_trade_no(String out_trade_no);
	
	

	Order save(Order order);
}
