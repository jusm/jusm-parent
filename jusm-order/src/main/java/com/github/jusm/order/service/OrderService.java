package com.github.jusm.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	/**
	 * 分页查询订单
	 * 
	 * @param id
	 * @param status
	 * @param shippingName
	 * @param userId
	 * @param stime
	 * @param etime
	 * @param pageRequest
	 * @return
	 */
	Page<Order> search(String id, int receiveType, int[] status, String shippingName, String userId, Date stime,
			Date etime, Pageable pageRequest);

	/**
	 * @param status
	 * @param stime
	 * @param etime
	 * @return
	 */
	List<Order> search(int[] status, Date stime, Date etime);

	/**
	 * @param status
	 * @param stime
	 * @param etime
	 * @return
	 */
	Map<String, BigDecimal> stats(int[] status, Date stime, Date etime);
}
