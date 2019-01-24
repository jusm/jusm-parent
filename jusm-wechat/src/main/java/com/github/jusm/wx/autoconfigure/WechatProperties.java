package com.github.jusm.wx.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "usm.wx", ignoreUnknownFields = true)
public class WechatProperties {

	private String appid;

	private String appsecret;

	private String mch_id;

	/**
	 * 微信支付API key
	 */
	private String key;

	private String notify_url;

	private boolean autoReport;

	private boolean useSandbox;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public boolean isUseSandbox() {
		return useSandbox;
	}

	public void setUseSandbox(boolean useSandbox) {
		this.useSandbox = useSandbox;
	}

	public boolean isAutoReport() {
		return autoReport;
	}

	public void setAutoReport(boolean autoReport) {
		this.autoReport = autoReport;
	}

}
