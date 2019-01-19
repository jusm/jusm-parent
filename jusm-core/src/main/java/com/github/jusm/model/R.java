package com.github.jusm.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.exception.BizException;
import com.github.jusm.exception.UsmException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果类
 */
@ApiModel("Restful API Ajax返回的json对象")
public class R implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "是否成功 true:成功;false:失败;", position = 1)
	private boolean success;

	@ApiModelProperty(value = "返回码", position = 2)
	private int code;

	@ApiModelProperty(value = "返回信息", position = 3)
	private String msg;

	@ApiModelProperty(value = "数据对象", position = 4)
	private Object data;

	public R(boolean success, Object data, int code, String msg) {
		super();
		this.success = success;
		this.data = data;
		this.code = code;
		this.msg = msg;
	}

	private R(Object t) {
		this.data = t;
		this.success = ReturnCode.OPER_SUCCESS.isSuccess();
		this.code = ReturnCode.OPER_SUCCESS.getCode();
		this.msg = ReturnCode.OPER_SUCCESS.getMsg();
	}

	private R(ReturnCode enums) {
		this.success = enums.isSuccess();
		this.code = enums.getCode();
		this.msg = enums.getMsg();
	}

	private R(ReturnCode enums, Object t) {
		this.success = enums.isSuccess();
		this.code = enums.getCode();
		this.msg = enums.getMsg();
		this.data = t;
	}

	public static R success() {
		return new R(ReturnCode.OPER_SUCCESS);
	}

	public static R success(Object t) {
		return new R(t);
	}

	public static R result(boolean result) {
		return result ? success() : failure();
	}

	public static R result(ReturnCode enums) {
		return new R(enums);
	}

	public static R result(ReturnCode enums, Object t) {
		return new R(enums, t);
	}

	public static R failure(Throwable e) {
		if (e == null) {
			return R.result(ReturnCode.FAILTURE);
		} else if (e instanceof UsmException) {
			return new R(((UsmException) e).getReturnCode(), e.getMessage());
		} else if (e instanceof BizException) {
			return new R(((BizException) e).getResult(), e.getMessage());
		} else if (e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
			return R.failure(ex.getBindingResult());
		} else if (e instanceof AccessDeniedException) {
			AccessDeniedException ex = (AccessDeniedException) e;
			return R.result(ReturnCode.ACCESS_DENIED, ex.getMessage());
		} else if (e instanceof HystrixRuntimeException) {
			HystrixRuntimeException ex = (HystrixRuntimeException) e;
			return R.result(ReturnCode.TIMEOUT_ERROR, ex.getMessage());
		} else {
			return new R(ReturnCode.UNKNOW_ERROR, e.getMessage());
		}
	}

	public static R failure() {
		return new R(ReturnCode.OPER_FAILTURE);
	}

	public static R failure(String errorMessage) {
		return new R(ReturnCode.OPER_FAILTURE, errorMessage);
	}

	public static R failure(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<String, String>();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return R.result(ReturnCode.VALID_ERROR, errorMap);
		}
		return null;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		ObjectUtils.defaultIfNull(data, "");
		return data;
	}

	public void setData(Object data) {
		ObjectUtils.defaultIfNull(data, "");
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
