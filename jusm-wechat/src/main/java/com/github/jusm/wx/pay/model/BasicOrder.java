package com.github.jusm.wx.pay.model;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class BasicOrder {

	@NotBlank(message = "商品描述必填")
	private String body;

	@NotBlank(message = "商品描述必填")
	private String detail;

	@NotBlank(message = "商品描述必填")
	private int total_fee;

	@NotBlank(message = "商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一")
	private String out_trade_no;
	
	@ApiModelProperty(name="设备号", example="WEB", notes="自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传\"WEB\"")
	private String device_info;

	private String product_id;
	
	/**
	 * 附加数据
	 */
	@ApiModelProperty(example = "梅江分店")
	private String attach;

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

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}


	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

}
