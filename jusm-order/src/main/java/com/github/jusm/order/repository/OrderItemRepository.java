package com.github.jusm.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.jusm.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>,JpaSpecificationExecutor<OrderItem> {

}
