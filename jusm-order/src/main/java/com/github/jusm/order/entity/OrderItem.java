package com.github.jusm.order.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.jusm.entity.UuidEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "usm_order_item")
@JsonIgnoreProperties(value="orderId",allowSetters=true)
public class OrderItem extends UuidEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name = "订单项ID([商品ID  ... ])",example="1")
	private String itemId;
	
	@JsonIgnore
	private String orderId;
	
	@ApiModelProperty(name = "订单项ID([商品标题])",example="蓝月亮洗衣液500ml")
	private String title;
	
	@ApiModelProperty(name = "订单项ID([商品标题])",example="2")
	private int num;
	
	@ApiModelProperty(name = "订单项ID([商品单价])",example="0.01")
	private BigDecimal price;
	
	@ApiModelProperty(name = "订单项(商品总价])",example="0.02")
	private BigDecimal totalFee;
	
	@ApiModelProperty(name = "订单项(图片路径))",example="https://www.mzchuangweilai.com:8443/res/4CGbbXB74pD24UM9QsfHnAx4kkfallNH/20190122160139.png")
	private String picture;


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
}
