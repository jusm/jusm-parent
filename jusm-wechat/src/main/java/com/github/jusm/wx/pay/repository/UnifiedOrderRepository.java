package com.github.jusm.wx.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.wx.pay.entity.UnifiedOrder;

public interface UnifiedOrderRepository extends JpaRepository<UnifiedOrder, String> {

}
