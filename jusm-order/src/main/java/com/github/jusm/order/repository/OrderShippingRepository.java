package com.github.jusm.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.order.entity.OrderShipping;

public interface OrderShippingRepository extends JpaRepository<OrderShipping, String> {

}
