package com.github.jusm.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.jusm.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>,JpaSpecificationExecutor<OrderItem> {

	List<OrderItem> findByOrderId(String id);

}
