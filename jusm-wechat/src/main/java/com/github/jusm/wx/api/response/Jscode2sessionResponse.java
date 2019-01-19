package com.github.jusm.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author haoran.wen
 *
 */
public class Jscode2sessionResponse {
	/**
	 * 用户唯一标识
	 */
	private String openid;

	/**
	 * 会话密钥
	 */
	@JSONField(name="session_key")
	private String sessionKey;
	/**
	 * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明
	 */
	private String unionid;
	/**
	 * 错误码
	 */
	private int errcode;
	/**
	 * 错误信息
	 */
	private String errmsg;
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
 
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	

}
