package com.github.jusm.order.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.jusm.entity.BasicEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "usm_order")
public class Order extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ApiModelProperty(name = "应付总额(单位元)")
	@NotNull(message = "应付总额(单位元) 没有请填写 0.00 ")
	private BigDecimal payment;

	@ApiModelProperty(name = "总商品金额(单位元)")
	@NotNull(message = "总商品金额(单位元) 没有请填写 0.00 ")
	private BigDecimal commodityAmount;

	@ApiModelProperty(name = "支付类型   1：在线付款  2：货到付款 ")
	@Column(length = 2)
	@DecimalMin(value = "1", message = "最小值为1")
	@DecimalMax(value = "2", message = "最小值为2")
	private int paymentType;

	@ApiModelProperty(name = "收货方式   1：邮寄快递  2：门店自提 ")
	@Column(length = 2)
	@DecimalMin(value = "1", message = "最小值为1")
	@DecimalMax(value = "2", message = "最小值为2")
	private int receiveType;

	@ApiModelProperty(name = "运费(单位元) 没有请填写 0.00")
	private BigDecimal postFee;

	@ApiModelProperty(name = "抵扣积分(单位元)")
	private BigDecimal deduction;

	@Column(length = 2)
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "支付时间", value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE
			- 199, hidden = true)
	@JsonIgnoreProperties(allowGetters = true)
	private Date paymentTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "发货时间", value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE
			- 199, hidden = true)
	@JsonIgnoreProperties(allowGetters = true)
	private Date consignTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "交易完成时间", value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE
			- 199, hidden = true)
	@JsonIgnoreProperties(allowGetters = true)
	private Date endTime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "交易关闭时间", value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE
			- 199, hidden = true)
	@JsonIgnoreProperties(allowGetters = true)
	private Date colseTime;

	@ApiModelProperty(name = "物流名称")
	private String shippingName;

	@ApiModelProperty(name = "物流单号")
	private String shippingCode;

	private String userId;

	private String buyerMessage;

	private String buyerNickName;
	
	private String sellerMessage;

	private boolean buyerRate;

	@Transient
	private List<OrderItem> orderItems = new ArrayList<>();

	@Transient
	private OrderShipping orderShipping;

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public String getBuyerNickName() {
		return buyerNickName;
	}

	public Date getColseTime() {
		return colseTime;
	}

	public BigDecimal getCommodityAmount() {
		return commodityAmount;
	}

	public Date getConsignTime() {
		return consignTime;
	}

	public BigDecimal getDeduction() {
		return deduction;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getId() {
		return id;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public BigDecimal getPostFee() {
		return postFee;
	}

	public int getReceiveType() {
		return receiveType;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public String getShippingName() {
		return shippingName;
	}

	public int getStatus() {
		return status;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isBuyerRate() {
		return buyerRate;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public void setBuyerNickName(String buyerNickName) {
		this.buyerNickName = buyerNickName;
	}

	public void setBuyerRate(boolean buyerRate) {
		this.buyerRate = buyerRate;
	}

	public void setColseTime(Date colseTime) {
		this.colseTime = colseTime;
	}

	public void setCommodityAmount(BigDecimal commodityAmount) {
		this.commodityAmount = commodityAmount;
	}

	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}

	public void setDeduction(BigDecimal deduction) {
		this.deduction = deduction;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public void setPostFee(BigDecimal postFee) {
		this.postFee = postFee;
	}

	public void setReceiveType(int receiveType) {
		this.receiveType = receiveType;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSellerMessage() {
		return sellerMessage;
	}

	public void setSellerMessage(String sellerMessage) {
		this.sellerMessage = sellerMessage;
	}

}
