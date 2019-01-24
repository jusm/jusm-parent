package com.github.jusm.wx.pay.service;


import com.github.jusm.model.R;
import com.github.jusm.wx.pay.model.BasicOrder;

public interface UnifiedOrderService {

	/**
	 * 统一下单
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	R unifiedOrder(BasicOrder order) throws Exception;
	
	/**
	 * 异步通知
	 * @param xml
	 * @return
	 */
	String notify(String xml);
}
