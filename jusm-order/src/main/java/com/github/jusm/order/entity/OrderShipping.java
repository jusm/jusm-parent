package com.github.jusm.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.jusm.entity.UuidEntity;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 订单配送表
 *
 */
@Entity
@Table(name="usm_order_shipping")
public class OrderShipping extends UuidEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="订单编码")
	@JsonIgnore
	private String orderId;
	
	@ApiModelProperty(name="收件人",example="张三")
	private String receiverName;
	
	@ApiModelProperty(name="收件人电话",example="0755-8568569")
	private String receiverPhone;
	
	@ApiModelProperty(name="收件人手机号",example="188888889")
	private String receiverMobile;
	
	@ApiModelProperty(name="收件地址-省",example="广东省")
	private String receiverProvince;
	
	@ApiModelProperty(name="收件地址-市",example="梅州市")
	private String receiverCity;
	
	@ApiModelProperty(name="收件地址-区/县",example="梅江区")
	private String receiverDistricts;
	
	@ApiModelProperty(name="收件地址-详细地址",example="解放路23号")
	private String receiverAddress;
	
	@ApiModelProperty(name="收件地址-邮政编码",example="554452")
	private String postCode;
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverDistricts() {
		return receiverDistricts;
	}

	public void setReceiverDistricts(String receiverDistricts) {
		this.receiverDistricts = receiverDistricts;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	
	
}
