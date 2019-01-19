package com.github.jusm.signature;

/**
 * 前端请求后返回对象集
 * @author lyj 2015年12月9日
 *
 */
public class Result {

	/**
	 * 1 成功 0 失败
	 */
	private int retStatus;
	
	/**
	 * 请求结果返回码
	 */
	private String errCode;
	
	/**
	 * 备注信息 如请求失败原因等
	 */
	private String errMsg;
	
	/**
	 * 请求成功返回数据
	 */
	private Object result;

	public int getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(int retStatus) {
		this.retStatus = retStatus;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
