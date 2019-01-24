package com.github.jusm.wx.pay.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 微信支付
 *
 */
@Entity
@Table(name = "wx_pay_unifiedorder")
public class UnifiedOrder {

	/**
	 * 小程序ID 微信分配的小程序ID wxd678efh567hg6787
	 */
	@NotBlank(message = "小程序ID必填")
	@Size(max = 32)
	private String appid; // 公众账号ID
	/**
	 * 商户号
	 */
	@NotBlank(message = "小商户号必填")
	@Size(max = 32)
	private String mch_id; // 商户号

	/**
	 * 设备号(否)
	 */
	private String device_info; // 设备号(否)

	/**
	 * 随机字符串
	 */
	@NotBlank(message = "随机字符串必填")
	@Size(max = 32)
	private String nonce_str; // 随机字符串

	/**
	 * 签名
	 */
	private String sign; // 签名

	/**
	 * 签名类型，默认为MD5，支持HMAC-SHA256和MD5。
	 */
	private String sign_type; // 签名

	/**
	 * 商品描述 需传入
	 */
	private String body; // 商品描述 需传入

	/**
	 * 商品详情(否) 需传入
	 */
	@Size(max = 6000)
	private String detail; // 商品详情(否) 需传入

	/**
	 * 附加数据
	 */
	private String attach; // 附加数据(否)

	/**
	 * 商户订单号
	 */
	@NotBlank(message = "商户订单号")
	@Id
	private String out_trade_no; // 商户订单号

	/**
	 * 标价币种 CNY
	 */
	private String fee_type; // 货币类型(否)

	/**
	 * 标价金额 订单总金额，单位为分
	 */
	private int total_fee; // 总金额 需传入

	/**
	 * 终端IP 支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
	 */
	private String spbill_create_ip; // 终端IP 需传入

	/**
	 * 交易起始时间
	 */
	private String time_start; // 交易起始时间(否)

	/**
	 * 交易结束时间
	 */
	private String time_expire; // 交易结束时间(否)

	/**
	 * 订单优惠标记
	 */
	@Size(max = 32)
	private String goods_tag; // 商品标记(否)

	/**
	 * 通知地址
	 */
	@NotBlank(message = "通知地址必填")
	private String notify_url; // 通知地址

	/**
	 * 交易类型 小程序取值如下：JSAPI，详细说明见
	 */
	@NotBlank(message = "交易类型必填")
	@Size(max = 16)
	private String trade_type; // 交易类型

	/**
	 * 商品ID trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
	 */
	@Size(max = 32)
	private String product_id; // 商品ID(否)

	/**
	 * 指定支付方式 上传此参数no_credit--可限制用户不能使用信用卡支付
	 */
	@Size(max = 32)
	private String limit_pay; // 指定支付方式(否)

	/**
	 * 用户标识
	 */
	private String openid; // 用户标识(否)

	/**
	 * 电子发票入口开放标识
	 */
	@Size(max = 8)
	private String receipt;

	/**
	 */
	private String scene_info;

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScene_info() {
		return scene_info;
	}

	public void setScene_info(String scene_info) {
		this.scene_info = scene_info;
	}

//	public SceneInfo getScene_info() {
//		return scene_info;
//	}
//
//	public void setScene_info(SceneInfo scene_info) {
//		this.scene_info = scene_info;
//	}

}
