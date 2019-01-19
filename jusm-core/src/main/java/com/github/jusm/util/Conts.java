package com.github.jusm.util;

/**
 * 常量类
 */
public final class Conts {

	private Conts() {
		throw new UnsupportedOperationException("此类拒绝实例化");
	}

	/**
	 * API TAGS
	 * 
	 * @author haoran.wen
	 *
	 */
	public static final class Tags {

		public static final String RZ = "01.登录与认证";

		public static final String WX = "02.小程序接口";

		public static final String PC = "03.控制台接口";

	}

	public static final class RoleNumber {

		public static final String ROLE_DBA = "role_dba";

		public static final String ROLE_ADMIN = "role_admin";

		public static final String ROLE_USER = "role_user";

		public static final String ROLE_PREFIX = "role_";

	}

	public static final String TOKEN_PREFIX = "Bearer-";

	public static final String TOKENHEADERKEY = "Authorization";

	public static final String DEFAULT_STATIC_FLODER = "static";

	public static final String DEFAULT_ACCESS_LOCAL = "/access/";

	public static final String DEFAULT_OUT_RES_FILE_PATH = "/local/";

	public static final String DEFAULT_OUT_STATIC_PATH_PATTERN = "/res/**";

	public static final String DEFAULT_OUT_FILE_PATH_PATTERN = DEFAULT_OUT_RES_FILE_PATH + "**";

	public static final String DEFAULT_OUT_FILE_PATH = "/UsmData/";

	public static final String DEFAULT_OUT_PUBLIC_FLODER = "Share";

	public static final String DEFAULT_OUT_PROTECTED_FLODER = "Local";
	/**
	 * 默认不是集群
	 */
	public static final boolean DEFAULT_CLUSTER = false;

	/**
	 * 初始换系统的requestMapping
	 */
	public static final String login = "/login";

	public static final String install = "/install";

	public static final String initialize = "/init";

	public static final String default_page = "/index";

	public static final String register = "/register";

	public static final String signin = "/login";

	public static final String signout = "/loginout";

	/**
	 * 默认的RESTful API 应用程序接口
	 * 
	 */
	public static final String DEFAULT_REST_API_PATH = "/api";

	public static final String DEFAULT_REST_API_ANT_PATTERN = DEFAULT_REST_API_PATH + "/**";

	/**
	 * 公共网关接口 Common Gateway Interface
	 */
	public static final String DEFAULT_REST_CGI_PATH = "/cgi";

	public static final String DEFAULT_REST_SIGN_API_ANT_PATTERN = DEFAULT_REST_CGI_PATH + "/**";

	/**
	 * WebContext 即 servletContext
	 */

	public static final String USM_CONTEXT_PATH = "contextPath";

	public static final String USM_CONTEXT_SHORTNAME = "shortname";

	public static final String USM_CONTEXT_FULLNAME = "fullname";

	public static final String USM_CONTEXT_SYSTEM_VER = "systemver";

	public static final String USM_CONTEXT_CURRENT_YEAR = "currentyear";

	public static final String USM_CONTEXT_API_COUNT = "apiCount";

	public static final String USM_CONTEXT_SERVER_DIST = "serverdist";

	public static final String USM_CONTEXT_SERVER_OS = "serveros";

	/**
	 * 初始换路径 Application key
	 */
	public static final String USM_CONTEXT_REQUEST_MAPPING_INITIALIZE = "initialize";

	/**
	 * 登录路径 Application key
	 */
	public static final String USM_CONTEXT_REQUEST_MAPPING_SIGNIN = "sign-in";

	/**
	 * 登出路径 Application key
	 */
	public static final String USM_CONTEXT_REQUEST_MAPPING_SIGNOUT = "sign-out";

	/**
	 * 注册手机
	 */
	public static final String USM_CONTEXT_REQUEST_MAPPING_REGISTER = "register";

	/**
	 * 是否显示验证码
	 */
	public static final String SESSION_USM_VERIFICATIONCODE_ENABLED_KEY = "USM_VERIFICATIONCODE_ENABLED";

	public static final String SESSION_USM_PASSWORD_PUB_KEY = "USM_PASSWORD_PUB_KEY";

	public static final String SESSION_USM_PASSWORD_PRI_KEY = "USM_PASSWORD_PRI_KEY";

	/**
	 * 失败多少次就显示验证码
	 */
	public static final String SESSION_USM_LOGIN_FAILURE_TIMES_KEY = "USM_LOGIN_FAILURE_TIMES";

	/**
	 * 
	 */
	public static final String SESSION_USM_LOGIN_FAILURE_MSG_KEY = "USM_LOGIN_FAILURE_MSG";

	/**
	 * 默认超级管理员用户名称 root
	 */
	public static final String DEFAULT_SUPER_ADMIN_USERNAME = "root";

	/**
	 * 超级管理员的默认邮箱
	 */
	public static final String DEFAULT_SUPER_ADMIN_EMAIL = "master@qq.com";

	/**
	 * 允许所有人访问的ant-patterns
	 */
	public static final String[] PERMIT_ALL_ANT_PATTERNS = { DEFAULT_REST_API_PATH + login,
			DEFAULT_REST_API_PATH + "/init-root", DEFAULT_REST_API_PATH + Conts.register,
			DEFAULT_REST_API_PATH + "/**/permit-all", "/free/**", DEFAULT_REST_SIGN_API_ANT_PATTERN };

	/**
	 * 静态资源的后缀名称
	 */
	public static final String[] STATIC_RESOURCES_SUFFIX = { ".css", ".js", ".map", ".woff", ".ttf", ".woff2", ".ico" };

	/**
	 * 
	 */
	public static final String[] WEB_INGORE_ANT_PATTERNS = { "/" + Conts.DEFAULT_STATIC_FLODER + "/**", "/**/*.css",
			"/**/*.js", "/**/*.js.map", "/**/*.ts", "/**/*.css.map", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.fco",
			"/**/*.woff", "/**/*.woff2", "/**/*.font", "/**/*.svg", "/**/*.ttf", "/*.ico", "/error" };

	public static final String DEFAULT_DIST = "D:";

	public static final String DEFAULT_KAPTCHA = "/auth/kaptcha";

	/**
	 * 跨域30天
	 */
	public static final Long DEFAULT_MAX_AGE = 2592000L;

	public static final int HTTPSESSION_TIMEOUT = 1800;

	public static final String DEFAULT_BLANK_CONTEXT_PATH = "/";

	/**
	 * 记住我 token有效期限
	 */
	public static final int TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60;

	/**
	 * 最大的session b并发数量
	 */
	public static final int MAX_IMUM_SESSIONS = 1;

}
