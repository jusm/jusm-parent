package com.github.jusm.util;

/**
 * 常量类
 */
public final class Conts {
	
	private Conts() {
		throw new UnsupportedOperationException("此类拒绝实例化");
	}
	
	public static final class  RoleNumber{
		
		public static final String ROLE_DBA ="role_dba";
		
		public static final String ROLE_ADMIN ="role_admin";
		
		public static final String ROLE_USER ="role_user";
		
		public static final String ROLE_PREFIX ="role_";
		
	}


	public static final String TOKEN_PREFIX = "Bearer-";

	public static final String TOKENHEADERKEY = "Authorization";

	public static final String DEFAULT_STATIC_FLODER = "static";
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
	 * 默认的RESTful API requestMapping
	 */
	public static final String DEFAULT_REST_API_ANT_PATTERN = "/api";

	/**
	 * WebContext 即 servletContext
	 */

	public static final String USM_CONTEXT_PATH = "contextPath";

	public static final String USM_CONTEXT_SHORTNAME = "shortname";

	public static final String USM_CONTEXT_FULLNAME = "fullname";

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
	public static final String[] PERMIT_ALL_ANT_PATTERNS = { DEFAULT_REST_API_ANT_PATTERN + login,
			DEFAULT_REST_API_ANT_PATTERN + "/init-root", DEFAULT_REST_API_ANT_PATTERN + Conts.register,
			DEFAULT_REST_API_ANT_PATTERN + "/**/permit-all" };

	/**
	 * 静态资源的后缀名称
	 */
	public static final String[] STATIC_RESOURCES_SUFFIX = { ".css", ".js", ".map", ".woff", ".ttf", ".woff2", ".ico" };

	/**
	 * 
	 */
	public static final String[] WEB_INGORE_ANT_PATTERNS = { "/" + Conts.DEFAULT_STATIC_FLODER + "/**",  "/**/*.css",
			"/**/*.js", "/**/*.js.map", "/**/*.ts", "/**/*.css.map", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.fco",
			"/**/*.woff", "/**/*.woff2", "/**/*.font", "/**/*.svg", "/**/*.ttf", "/*.ico" };

}
