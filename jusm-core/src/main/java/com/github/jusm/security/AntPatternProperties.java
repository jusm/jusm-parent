package com.github.jusm.security;

import com.github.jusm.util.Conts;

/**
 * 系统安全配置属性
 */
public class AntPatternProperties {

	/**
	 * 系统 defalut 页面
	 */
	private String index = Conts.default_page;

	/**
	 * 系统登录页面路径
	 */
	private String login = Conts.login;

	/**
	 * 系统安装页面路径
	 */
	private String install = Conts.install;

	/**
	 * 注册地址
	 */
	private String register = Conts.register;

	/**
	 * 系统初始化路径
	 */
	private String init = Conts.initialize;

	/**
	 * 登录
	 */
	private String signIn = Conts.signin;

	/**
	 * 登出
	 */
	private String signOut = Conts.signout;

	/**
	 * 获取鉴权信息路径
	 */
	private String auth = "/auth/**";

	/**
	 * 顾名思义，WebSecurity主要是配置跟web资源相关的，比如css、js、images等等，但是这个还不是本质的区别，关键的区别如下：
	 * ingore是完全绕过了spring security的所有filter，相当于不走spring security permitall没有绕过spring
	 * security，其中包含了登录的以及匿名的 这里增加这个没有卵用 {@value}
	 */
	private String[] permitAllAntPatterns = Conts.PERMIT_ALL_ANT_PATTERNS;

	/**
	 * 静态资源前缀
	 */
	private String[] staticResourcesPrefix = { "/" + Conts.DEFAULT_STATIC_FLODER };

	/**
	 * 静态资源后缀
	 */
	private String[] staticResourcesSuffix = Conts.STATIC_RESOURCES_SUFFIX;

	/**
	 * 静态资源文件 ingore是完全绕过了spring security的所有filter
	 */
	private String[] webIngoreAntPatterns = Conts.WEB_INGORE_ANT_PATTERNS;

	private String restAntPattern;

	private String[] authorizeAntPatterns = { Conts.DEFAULT_REST_API_ANT_PATTERN + "/**" };

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSignOut() {
		return signOut;
	}

	public void setSignOut(String signOut) {
		this.signOut = signOut;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * 默认返回 /api/login,/api/init-root,/api/register,/api/** permit-all
	 * 
	 * @return
	 */
	public String[] getPermitAllAntPatterns() {
		return permitAllAntPatterns;
	}

	public void setPermitAllAntPatterns(String[] permitAllAntPatterns) {
		this.permitAllAntPatterns = permitAllAntPatterns;
	}

	public String[] getWebIngoreAntPatterns() {
		return webIngoreAntPatterns;
	}

	public void setWebIngoreAntPatterns(String[] webIngoreAntPatterns) {
		this.webIngoreAntPatterns = webIngoreAntPatterns;
	}

	/**
	 * 默认为: /api/**
	 * 
	 * @return
	 */
	public String[] getAuthorizeAntPatterns() {
		return authorizeAntPatterns;
	}

	public void setAuthorizeAntPatterns(String[] authorizeAntPatterns) {
		this.authorizeAntPatterns = authorizeAntPatterns;
	}

	public String getInstall() {
		return install;
	}

	public void setInstall(String install) {
		this.install = install;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * 默认 /sign-in
	 * 
	 * @return
	 */
	public String getSignIn() {
		return signIn;
	}

	public void setSignIn(String signIn) {
		this.signIn = signIn;
	}

	public String getRestAntPattern() {
		return restAntPattern;
	}

	public void setRestAntPattern(String restAntPattern) {
		this.restAntPattern = restAntPattern;
	}

	/**
	 * 默认返回 : /static
	 * 
	 * @return
	 */
	public String[] getStaticResourcesPrefix() {
		return staticResourcesPrefix;
	}

	public void setStaticResourcesPrefix(String[] staticResourcesPrefix) {
		this.staticResourcesPrefix = staticResourcesPrefix;
	}

	public String[] getStaticResourcesSuffix() {
		return staticResourcesSuffix;
	}

	public void setStaticResourcesSuffix(String[] staticResourcesSuffix) {
		this.staticResourcesSuffix = staticResourcesSuffix;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

}
