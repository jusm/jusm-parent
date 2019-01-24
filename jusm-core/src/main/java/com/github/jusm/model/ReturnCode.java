package com.github.jusm.model;

public enum ReturnCode {
	// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓正确的错误码↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	SUCCESS(true, 1, "成功"),

	FAILTURE(false, 0, "失败"),

	OPER_SUCCESS(true, 1000, "操作成功"),

	REGIST_SUCCESS(true, 1001, "注册成功,管理员激活后方可使用"),
	
	UNIFIEDORDER_SUCCESS(true, 1002, "微信支付统一下单成功"),

	// **↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓失败的错误码↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/

	OPER_FAILTURE(false, 5000, "操作失败"),

	BIZ_ERROR(false, 5001, "操作失败"),

	UPLOAD_ERROR(false, 5002, "上传失败"),

	ACCESS_DENIED(false, 5003, "没有权限"),
	
	UNIQUE_INDEX_DUPLICATE(false, 5004, "唯一索引冲突"),

	// "登录已经失效,请重新验证码登录"
	// "短信验证码不正确"
	// "不存在的设备码"
	// 不存在的用户
	// 6000
	WEIXIN_ACCESSTOKEN_FAILED(false, 6000, "获取Accesstoken失败"),
	
	UNIFIEDORDER_FAILED(false, 6001, "微信支付统一下单失败"),

	ACCOUNT_TRADE_EXCEPTION(false, 6999, "转账失败"),
	
	// 7000
	NOT_SUFFICIENT_BALANCE(false, 7000, "账户余额不足"),

	FAILURE_OF_RECHARGE(false, 7001, "账户充值失败"),

	NOT_FIND_ACCOUNT_EXCEPTION(false, 7002, "账户不存在"),

	ACCOUNT_LOCKED_EXCEPTION(false, 7003, "账户被锁"),

	TRANSFER_LIMIT_EXCEPTION(false, 7004, "转账限额"),

	NOT_FIND_BILL_EXCEPTION(false, 7005, "单据不存在"),

	NOT_FIND_BANKACCOUNT_EXCEPTION(false, 7006, "请填写您的提现到银行账户信息"),
	// 8000

	CREDENTIALS_EXPIRED_BY_LOGOUT(false, 9964, "用户已退出，请重新登录"),

	NOT_EXIST_USER(false, 9965, "不存在的用户"),

	CHECK_FAILED_SMS_CODE(false, 9966, "短信验证码不正确"),

	NOT_EXIST_DEVICE_CODE(false, 9967, "不存在的设备码"),

	LOGIN_IS_INVALID(false, 9968, "登录已经失效,请重新验证码登录"),

	NO_OBTAINED_SMS_VERIFICATION_CODE(false, 9969, "请获取短信验证码"),

	VERIFY_SIGN_ERROR(false, 9970, "验证签名失败"),

	GENERATE_SIGN_ERROR(false, 9971, "生成签名失败"),

	TIMEOUT_ERROR(false, 9972, "第三方服务响应超时"),

	NEED_BIND_PHONENUMBER(false, 9973, "请绑定手机号"),

	UNSUPPORT_PRINTING(false, 9974, "该下单码不支持直接打印"),

	NEED_ID_CARD(false, 9975, "需要您实名认证"),

	NOT_EXIST_PRINT_NUMBER(false, 9976, "您扫描的打印码不存在或已被删除"),

	VALID_ERROR_BY_CONFIG(false, 9977, "包裹订单的数据与打印码里的信息不一致"),

	TOKEN_VALID_FAILED(false, 9978, "无效的令牌"),

	WXCXX_NO_SIGN_IN(false, 9979, "微信小程序未登录"),

	NEED_APP_TOKEN(false, 9980, "请求头需携带用户令牌!"),

	ILLEGAL_APPNUMBER_ERROR(false, 9981, "不存在的应用编码!"),

	ILLEGAL_APPCERT_ERROR(false, 9982, "不合法的APP证书或应用未运行,请到开发平台控制台查看应用状态!"),

	ILLEGALARGUMENT_ERROR(false, 9983, "不合法的参数!"),

	NO_RESULT_ERROR(false, 9984, "没有返回值!"),

	PARSE_SHORTURL_ERROR(false, 9985, "短链接解析失败!"),

	NOT_LOGGED_IN_ERROR(false, 9986, "请登录"),

	UNAUTHORIZED_ERROR(false, 9987, "访问被拒绝"),

	FILE_FORMAT_ERROR(false, 9988, "文件格式错误"),

	JSON_ERROR(false, 9989, "JSON数据格式错误"),

	AUTH_ERROR(false, 9990, "请求头需携带以Authorization为key, 'Bearer-'为前缀的令牌参数"),

	REFERENCE_ERROR(false, 9991, "该记录已经被引用"),

	DISABLED_USER_ERROR(false, 9992, "用户未激活或禁用，请先激活"),

	LOGIN_ACCOUNT_ERROR(false, 9993, "账号或密码错误"),

	NO_EXIST_ERROR(false, 9994, "不存在的记录"),

	TOKEN_ERROR(false, 9995, "令牌失效，请重新登录"),

	REPEAT_ERROR(false, 9996, "已存在的记录"),

	VALID_ERROR(false, 9997, "参数校验失败"),

	KYE_API_EXCEPTION(false, 9998, "开放平台异常,请联系开放平台管理员"),

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