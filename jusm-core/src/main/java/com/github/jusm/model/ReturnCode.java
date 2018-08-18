package com.github.jusm.model;

public enum ReturnCode {
//	↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓正确的错误码↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	SUCCESS(true, 1, "成功"),

	FAILTURE(false, 0, "失败"),

	OPER_SUCCESS(true, 1000, "操作成功"),

	REGIST_SUCCESS(true, 1001, "注册成功,管理员激活后方可使用"),

	
	
//**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓失败的错误码↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
	
	OPER_FAILTURE(false, 5000, "操作失败"),
	
	
	
	
	TOKEN_VALID_FAILED(false, 9978, "无效的Token"),
	
	WXCXX_NO_SIGN_IN(false, 9979, "微信小程序未登录"),
	
	NEED_APP_TOKEN(false, 9980, "请求需携带用户token!"),
	
	ILLEGAL_APPNUMBER_ERROR(false, 9981, "不存在的应用编码!"),
	
	ILLEGAL_APPCERT_ERROR(false, 9982, "不合法的APP证书或应用未运行,请到开发平台控制台查看应用状态!"),
	
	ILLEGALARGUMENT_ERROR(false, 9983, "不合法的参数!"),
	
	NO_RESULT_ERROR(false, 9984, "没有返回值!"),

	PARSE_SHORTURL_ERROR(false, 9985, "短链接解析失败!"),

	NOT_LOGGED_IN_ERROR(false, 9986, "请登录"),

	UNAUTHORIZED_ERROR(false, 9987, "访问被拒绝"),

	FILE_FORMAT_ERROR(false, 9988, "文件格式错误"),

	JSON_ERROR(false, 9989, "Json数据格式错误"),

	AUTH_ERROR(false, 9990, "鉴权失败"),

	REFERENCE_ERROR(false, 9991, "该记录已经被引用"),

	DISABLED_USER_ERROR(false, 9992, "用户未激活或禁用，请先激活"),

	LOGIN_ACCOUNT_ERROR(false, 9993, "账号或密码错误"),

	NO_EXIST_ERROR(false, 9994, "不存在的记录"),

	TOKEN_ERROR(false, 9995, "Token失效"),

	REPEAT_ERROR(false, 9996, "已存在的记录"),

	VALID_ERROR(false, 9997, "参数校验失败"),

	USM_ERROR(false, 9998, "系统异常,请联系管理员"),

	UNKNOW_ERROR(false, 9999, "未知异常,请联系管理员");

	/**
	 * 成功失败标示
	 */
	private boolean success;

	/**
	 * 返回码
	 */
	private Integer code;

	/**
	 * 返回消息
	 */
	private String msg;

	private ReturnCode(boolean success, Integer code, String msg) {
		this.success = success;
		this.code = code;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 根据返回码找返回枚举
	 * 
	 * @param code
	 * @return
	 */
	public static ReturnCode resultOfCode(Integer code) {
		for (ReturnCode result : ReturnCode.values()) {
			if (result.code.equals(code)) {
				return result;
			}
		}
		return null;
	}

	public static boolean isSuccessOfCode(Integer code) {
		for (ReturnCode result : ReturnCode.values()) {
			if (result.code.equals(code)) {
				return result.success;
			}
		}
		return false;
	}

	public static String msgOfCode(Integer code) {
		for (ReturnCode result : ReturnCode.values()) {
			if (result.code.equals(code)) {
				return result.msg;
			}
		}
		return null;
	}
}