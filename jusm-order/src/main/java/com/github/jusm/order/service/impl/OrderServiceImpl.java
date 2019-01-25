package com.github.jusm.order.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<Order> search(String id, int receiveType, int[] status, String shippingName, String userId, Date stime,
			Date etime, Pageable pageRequest) {

		Specification<Order> querySpecifi = new Specification<Order>() {
			@Override
			public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (StringUtils.isNotBlank(id)) {
					// 模糊查找
					predicates.add(cb.like(root.get("id").as(String.class), "%" + id.trim() + "%"));
				}

				if (receiveType > 0) {
					predicates.add(cb.equal(root.get("receiveType").as(Integer.class), receiveType));
				}

				if (status != null && status.length > 0) {
					// 模糊查找
					In<Integer> in = cb.in(root.get("status"));
					for (Integer e : status) {
						in.value(e);
					}
					predicates.add(in);
					// predicates.add(cb.in(root.get("status").as(Integer.class),status));
				}
				if (StringUtils.isNotBlank(shippingName)) {
					// 模糊查找
					predicates.add(cb.like(root.get("shippingName").as(String.class), shippingName.trim()));
				}
				if (StringUtils.isNotBlank(userId)) {
					// 模糊查找
					predicates.add(cb.like(root.get("userId").as(String.class), userId.trim()));
				}

				if (stime != null) {
					// 大于或等于传入时间
					predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), stime));
				}
				if (etime != null) {
					// 小于或等于传入时间
					predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), etime));
				}
				// and到一起的话所有条件就是且关系，or就是或关系
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		Page<Order> result = orderRepository.findAll(querySpecifi, pageRequest);

		if (result != null) {
			List<Order> content = result.getContent();
			if (content != null) {
				for (Order order : content) {
					List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
					order.setOrderItems(orderItems);
					OrderShipping orderShipping = orderShippingRepository.findByOrderId(order.getId());
					order.setOrderShipping(orderShipping);
				}
			}
		}

		return result;
	}

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
