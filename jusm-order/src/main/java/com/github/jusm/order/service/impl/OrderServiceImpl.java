package com.github.jusm.order.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.jusm.order.entity.Order;
import com.github.jusm.order.entity.OrderItem;
import com.github.jusm.order.entity.OrderShipping;
import com.github.jusm.order.repository.OrderItemRepository;
import com.github.jusm.order.repository.OrderRepository;
import com.github.jusm.order.repository.OrderShippingRepository;
import com.github.jusm.order.service.OrderService;
import com.github.jusm.redis.RedisKey;
import com.github.jusm.redis.RedisRepository;
import com.github.jusm.validation.JSR303;

public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderShippingRepository orderShippingRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String create(Order order, List<OrderItem> orderItems, OrderShipping orderShipping) {

		if (order == null || orderItems.size() == 0) {
			throw new IllegalArgumentException("订单项必填");
		}

		if (orderItems == null || orderItems.size() == 0) {
			throw new IllegalArgumentException("订单项必填");
		}
		order.setId(generateID());
		order.setStatus(NOT_PAY);
		Order save = orderRepository.save(order);
		String id = save.getId();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(id);
		}
		orderItemRepository.save(orderItems);

		orderShipping.setOrderId(id);
		orderShippingRepository.save(orderShipping);
		return save.getId();
	}

	public static void main(String[] args) {
		Order order = new Order();
		order.setPayment(new BigDecimal("10.02"));
		order.setPaymentType(23);
		JSR303.validate(order);
	}

	@Override
	public String create(Order order, List<OrderItem> orderItems) {
		JSR303.validate(order);
		if (order == null) {
			throw new IllegalArgumentException("订单必填");
		}
		if (orderItems == null || orderItems.size() == 0) {
			throw new IllegalArgumentException("订单项必填");
		}
		order.setStatus(NOT_PAY);
		order.setId(generateID());
		Order save = orderRepository.save(order);
		String id = save.getId();
		for (OrderItem orderItem : orderItems) {
			JSR303.validate(orderItem);
			orderItem.setOrderId(id);
		}
		orderItemRepository.save(orderItems);
		return save.getId();
	}

	@Override
	public String generateID() {
		String string = DateTime.now().toString("yyyyMMdd");
		Long incr = redisRepository.incr(RedisKey.SEQUENCE_ORDER_ID.getKey(string), 24, TimeUnit.HOURS);
		DecimalFormat df = new DecimalFormat("00000");
		return string + df.format(incr.longValue());
	}

	@Override
	public Order findByOut_trade_no(String out_trade_no) {
		return orderRepository.findOne(out_trade_no);
	}

	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}

}
