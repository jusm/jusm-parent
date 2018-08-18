package com.github.jusm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Table(name = "usm_parameter")
@Entity
@ApiModel("系统参数表")
public class Parameter extends UuidEntity {
	public static final String SYS_CREATE_DATE_KEY = "system_create_date";
	public static final String SYS_LASTSTART_DATE_KEY = "system_upgrade_date";

	public static Parameter SYS_CREATE_DATE = new Parameter(SYS_CREATE_DATE_KEY, "系统初始化时间");
	public static Parameter SYS_LASTSTART_DATE = new Parameter(SYS_LASTSTART_DATE_KEY, "系统最近更新时间");

	private static final long serialVersionUID = -5248288870584319787L;

	public Parameter(String paramKey, String paramDesc) {
		this.paramKey = paramKey;
		this.paramDesc = paramDesc;
	}

	public Parameter() {}

	@Column(unique = true)
	private String paramKey;

	private String paramValue;

	private String paramDesc;

	public String getParamKey() {
		return paramKey;
	}

	public Parameter setParamKey(String paramKey) {
		this.paramKey = paramKey;
		return this;
	}

	public String getParamValue() {
		return paramValue;
	}

	public Parameter setParamValue(String paramValue) {
		this.paramValue = paramValue;
		return this;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public Parameter setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
		return this;
	}

}
