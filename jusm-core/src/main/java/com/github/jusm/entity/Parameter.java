package com.github.jusm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Table(name = "usm_parameter")
@Entity
@ApiModel("系统参数表")
/**
 * 
 * 系统参数采用多例 不能随便new 如果想增加必填在此类增加一个实例
 */
public class Parameter {

	public static final String SYS_CREATE_DATE_KEY = "SYS_CREATE_DATE_KEY";

	public static final String SYS_LASTSTART_DATE_KEY = "SYS_LASTSTART_DATE_KEY";

	public static final Parameter SYS_SHARE_PATH = new Parameter("SYS_SHARE_PATH", "USM共享目录", 1);

	public static final Parameter SYS_LOCAL_PATH = new Parameter("SYS_LOCAL_PATH", "USM本地目录", 2);

	public static final Parameter SYS_UPLOAD_PATH = new Parameter("SYS_UPLOAD_PATH", "USM上传目录", 3);

	public static final Parameter SYS_TEMP_PATH = new Parameter("SYS_TEMP_PATH", "USM临时目录", 4);

	public static final Parameter SYS_OSUSER_PATH = new Parameter("SYS_OSUSER_PATH", "操作系统用户Home目录", 5);

	public static final Parameter SYS_CREATE_DATE = new Parameter(SYS_CREATE_DATE_KEY, "系统初始化时间", 6);

	public static final Parameter SYS_LASTSTART_DATE = new Parameter(SYS_LASTSTART_DATE_KEY, "系统最近更新时间", 7);

	public static final Parameter SYS_HTTP_SESSION_TIMEOUT = new Parameter("SYS_HTTP_SESSION_TIMEOUT",
			"httpSession有效时间(单位秒)", 8);

	public static final Parameter SYS_TOKEN_VALIDITY_SECONDS = new Parameter("SYS_TOKEN_VALIDITY_SECONDS", "记住我 (单位秒)",
			9);

	public static final Parameter SYS_MAX_IMUM_SESSIONS = new Parameter("SYS_MAX_IMUM_SESSIONS", "session的最大并发数量", 10);

	private Parameter(String paramKey, String paramDesc, int id) {
		this.paramKey = paramKey;
		this.paramDesc = paramDesc;
		this.id = id;
	}

	private Parameter() {

	}

	@Column(unique = true)
	private String paramKey;

	private String paramValue;

	private String paramDesc;

	@Id
	private int id;

	public String getParamKey() {
		return paramKey;
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

	public int getId() {
		return id;
	}

}
