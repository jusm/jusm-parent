package com.github.jusm.order.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.jusm.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

	List<Order> findByStatusInAndPaymentTimeBetween(int[] status, Date stime, Date etime);


	
}
