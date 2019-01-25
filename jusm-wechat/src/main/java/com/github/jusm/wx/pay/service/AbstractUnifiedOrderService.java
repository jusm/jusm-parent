package com.github.jusm.wx.pay.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jusm.context.CurrentUser;
import com.github.jusm.exception.ValidException;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.util.AddressUtil;
import com.github.jusm.wx.pay.entity.UnifiedOrder;
import com.github.jusm.wx.pay.model.BasicOrder;
import com.github.jusm.wx.pay.sdk.WXPay;
import com.github.jusm.wx.pay.sdk.WXPayConfig;
import com.github.jusm.wx.pay.sdk.WXPayConstants;
import com.github.jusm.wx.pay.sdk.WXPayConstants.SignType;
import com.github.jusm.wx.pay.sdk.WXPayUtil;

public abstract class AbstractUnifiedOrderService implements UnifiedOrderService {

	public static final String SUCCESS = "SUCCESS";

	public static final String NOTPAY = "NOTPAY";

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WXPay wxPay;

	@Autowired
	private WXPayConfig wxPayConfig;

	@Override
	public Map<String, String> closeOrder(String out_trade_no) throws Exception {
		Map<String, String> reqData = new HashMap<>();
		if (StringUtils.isNotBlank(out_trade_no)) {
			reqData.put("out_trade_no", out_trade_no);
		} else {
			throw new ValidException("商户订单号必填");
		}
		return wxPay.closeOrder(reqData);
	}

	@Override
	public Map<String, String> orderQuery(String out_trade_no, String transaction_id) throws Exception {
		Map<String, String> reqData = new HashMap<>();
		if (StringUtils.isBlank(transaction_id) && StringUtils.isBlank(out_trade_no)) {
			throw new ValidException("商户订单号  微信订单号  二选一 必填");
		}
		if (StringUtils.isNotBlank(out_trade_no)) {
			reqData.put("out_trade_no", out_trade_no);
		} else {
			reqData.put("transaction_id ", transaction_id);
		}
		return wxPay.orderQuery(reqData);
	}

	@Override
	public String notify(String notifyData) {
		logger.info("notify() start, notifyData={}", notifyData);
		String xmlBack = "";
		Map<String, String> notifyMap = null;
		try {

			notifyMap = WXPayUtil.xmlToMap(notifyData); // 转换成map
			if (wxPay.isPayResultNotifySignatureValid(notifyMap)) {
				// 签名正确
				// 进行处理。
				// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
				String return_code = notifyMap.get("return_code");// 状态
				String out_trade_no = notifyMap.get("out_trade_no");// 订单号
				String total_fee = notifyMap.get("total_fee");

				if (out_trade_no == null) {
					logger.info("微信支付回调失败订单号: {}", notifyMap);
					xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
					return xmlBack;
				}

				callback(out_trade_no, return_code,Integer.valueOf(total_fee));
				logger.info("微信支付回调成功订单号: {}", notifyMap);
				xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
				return xmlBack;
			} else {
				logger.error("微信支付回调通知签名错误");
				xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				return xmlBack;
			}
		} catch (Exception e) {
			logger.error("微信支付回调通知失败", e);
			xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		return xmlBack;
	}

	/**
	 * 根据异步回调结果修改订单状态
	 * 
	 * @param out_trade_no
	 * @param return_code
	 * @return
	 */
	protected abstract boolean callback(String out_trade_no, String return_code,int total_fee);

	@Override
	public R unifiedOrder(BasicOrder order) throws Exception {
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		unifiedOrder.setAppid(getAppid());
		unifiedOrder.setMch_id(getMch_id());
		unifiedOrder.setDevice_info("WEB");
		unifiedOrder.setNonce_str(RandomStringUtils.randomAlphanumeric(32));
		unifiedOrder.setSign(StringUtils.EMPTY);
		unifiedOrder.setSign_type(StringUtils.EMPTY);
		unifiedOrder.setBody(order.getBody());
		unifiedOrder.setDetail(order.getDetail());
		unifiedOrder.setAttach(order.getAttach());
		unifiedOrder.setOut_trade_no(order.getOut_trade_no());
		unifiedOrder.setFee_type("CNY");
		unifiedOrder.setTotal_fee(order.getTotal_fee());
		unifiedOrder.setSpbill_create_ip(getSpbill_create_ip());
		unifiedOrder.setTime_start(DateTime.now().toString("yyyyMMddHHmmss"));
		unifiedOrder.setTime_expire(DateTime.now().plusHours(1).toString("yyyyMMddHHmmss"));
		unifiedOrder.setGoods_tag(StringUtils.EMPTY);
		unifiedOrder.setLimit_pay(getNotify_url());
		unifiedOrder.setTrade_type("JSAPI");
		unifiedOrder.setProduct_id(order.getProduct_id());
		unifiedOrder.setLimit_pay(getLimit_pay());
		unifiedOrder.setOpenid(getOpenid());
		unifiedOrder.setReceipt("N");
		unifiedOrder.setScene_info(StringUtils.EMPTY);

		String jsonString = JSON.toJSONString(unifiedOrder);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);

		Map<String, String> reqData = new HashMap<>();
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			reqData.put(key, value);
		}

		logger.info("发起微信支付下单接口, request={}", reqData);
		Map<String, String> response = wxPay.unifiedOrder(reqData);
		logger.info("微信支付下单返回值  response={}", response);
		String returnCode = response.get("return_code");
		if (!SUCCESS.equals(returnCode)) {
			return R.result(ReturnCode.UNIFIEDORDER_FAILED, response);
		}
		String resultCode = response.get("result_code");
		if (!SUCCESS.equals(resultCode)) {
			return R.result(ReturnCode.UNIFIEDORDER_FAILED, response);
		}
		String prepay_id = response.get("prepay_id");
		if (prepay_id == null) {
			return R.result(ReturnCode.UNIFIEDORDER_FAILED, response);
		}
		// 业务处理(保存微信下单结果)
		Map<String, String> result = new HashMap<>();
		String packages = "prepay_id=" + prepay_id;
		Map<String, String> wxPayMap = new HashMap<String, String>();
		wxPayMap.put("appId", response.get("appid"));
		wxPayMap.put("timeStamp", getCurrentTimeStamp());
		wxPayMap.put("nonceStr", response.get("nonce_str"));// TODO RandomStringUtils.randomAlphanumeric(32));//
		wxPayMap.put("package", packages);
		String signType = SignType.HMACSHA256.equals(wxPay.getSignType()) ? WXPayConstants.HMACSHA256
				: WXPayConstants.MD5;
		wxPayMap.put("signType", signType);// 签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
		String sign = WXPayUtil.generateSignature(wxPayMap, wxPayConfig.getKey(), wxPay.getSignType());
		result.put("paySign", sign);
		result.putAll(wxPayMap);
		logger.debug("统一支付返回：{}", result);
		return R.result(ReturnCode.UNIFIEDORDER_SUCCESS, result);

	}

	private static String getCurrentTimeStamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	protected String getLimit_pay() {
		return StringUtils.EMPTY;
	}

	protected String getAppid() {
		return StringUtils.EMPTY;
	}

	protected String getMch_id() {
		return StringUtils.EMPTY;
	}

	protected String getOpenid() {
		String username = CurrentUser.getUsername();
		if ("root".equals(username)) {
			return "o-tLH5eUFL97wMQTu8xgsG_Cwloc";
		}
		return username;
	}

	/**
	 * 
	 * @return
	 */
	protected String getNotify_url() {
		return StringUtils.EMPTY;
	}

	protected String getSpbill_create_ip() {
		return AddressUtil.getHostAddress();
	}

}
