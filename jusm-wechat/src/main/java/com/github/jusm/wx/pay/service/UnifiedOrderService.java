package com.github.jusm.wx.pay.service;

import java.util.Map;

import com.github.jusm.model.R;
import com.github.jusm.wx.pay.model.BasicOrder;

public interface UnifiedOrderService {

	/**
	 * 统一下单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	R unifiedOrder(BasicOrder order) throws Exception;

	/**
	 * 异步通知
	 * 
	 * @param xml
	 * @return
	 */
	String notify(String xml);

	/**
	 * 应用场景该接口提供所有微信支付订单的查询，商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。
	 * 
	 * 需要调用查询接口的情况：
	 * 
	 * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知； ◆ 调用支付接口后，返回系统错误或未知交易状态情况； ◆
	 * 调用刷卡支付API，返回USERPAYING的状态； ◆ 调用关单或撤销接口API之前，需确认支付状态；
	 * 
	 * @param out_trade_no
	 *            商户订单号
	 * @param transaction_id微信订单号
	 * @return
	 * @throws Exception
	 */
	Map<String, String> orderQuery(String out_trade_no, String transaction_id) throws Exception;

	/**
	 * 应用场景
	 * 以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
	 * 
	 * 注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
	 * 
	 * @param out_trade_no
	 *            商户订单号
	 * @return
	 * @throws Exception
	 */
	Map<String, String> closeOrder(String out_trade_no) throws Exception;
}
