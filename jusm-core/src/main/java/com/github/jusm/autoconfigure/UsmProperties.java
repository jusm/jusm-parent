package com.github.jusm.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.github.jusm.security.AntPatternProperties;
import com.github.jusm.security.JwtProperties;
import com.github.jusm.security.SwaggerProperties;
import com.github.jusm.util.Conts;
import com.github.jusm.web.cors.CorsProperties;

/**
 * 统一系统管理配置属性
 */
@ConfigurationProperties(prefix = "usm", ignoreUnknownFields = true)
public class UsmProperties {

	private String root = Conts.DEFAULT_SUPER_ADMIN_USERNAME;

	private String email = Conts.DEFAULT_SUPER_ADMIN_EMAIL;

	private Map<String, String> successLoginDispatcherMap = new HashMap<>();

	@Value("${usm.alarm:false}")
	private boolean alarm;

	public Map<String, String> getSuccessLoginDispatcherMap() {
		return successLoginDispatcherMap;
	}

	public void setSuccessLoginDispatcherMap(Map<String, String> successLoginDispatcherMap) {
		this.successLoginDispatcherMap = successLoginDispatcherMap;
	}

	@Value("${spring.profiles.active:dev}")
	private String activeProfile;

	@NestedConfigurationProperty
	private SwaggerProperties swagger = new SwaggerProperties();

	@NestedConfigurationProperty
	private AntPatternProperties antPattern = new AntPatternProperties();

	@NestedConfigurationProperty
	private CorsProperties cors = new CorsProperties();

	public CorsProperties getCors() {
		return cors;
	}

	@NestedConfigurationProperty
	private JwtProperties jwt = new JwtProperties();

	public SwaggerProperties getSwagger() {
		return swagger;
	}

	public JwtProperties getJwt() {
		return jwt;
	}

	public AntPatternProperties getAntPattern() {
		return antPattern;
	}

	@Value("${usm.config-path:classpath:META-INF/config/}")
	private String configPath;

	private Map<String, String> topicMap = new HashMap<>();

	@Value("${usm.dynamic-code.enabled:true}")
	private boolean enableDynamicCode;

	@Value("${usm.dynamic-code.failure:3}")
	private int loginFailureTimes;

	@Value("${usm.title.fullname:统一系统管理}")
	private String fullname;

	@Value("${usm.title.shortname:USM}")
	private String shortname;

	/**
	 * USM 包之外的资源目录 一定后面需要一个/ 访问不需要本系统的RBAC机制 /res/**
	 */
	@Value("${usm.resources.path-patterns.share:" + Conts.DEFAULT_OUT_STATIC_PATH_PATTERN + "}")
	private String publicPathPatterns;

	/**
	 * 访问资源时本系统权限(RBAC) /local/**
	 */
	@Value("${usm.resources.path-patterns.local:" + Conts.DEFAULT_OUT_FILE_PATH_PATTERN + "}")
	private String pretectPathPatterns;

	/**
	 * windows: D:/UsmData/share mac unix /UsmData/share
	 */
	@Value("${usm.resources.data.share:" + Conts.DEFAULT_OUT_FILE_PATH + Conts.DEFAULT_OUT_PUBLIC_FLODER + "/}")
	private String outsidePublicRes;

	@Value("${usm.resources.data.local:" + Conts.DEFAULT_OUT_FILE_PATH + Conts.DEFAULT_OUT_PROTECTED_FLODER + "/}")
	private String outsideProtectedRes;

	@Value("${usm.resources.data.temp:" + Conts.DEFAULT_OUT_FILE_PATH + "Temp/}")
	private String temp;

	@Value("${usm.resources.data.upload:" + Conts.DEFAULT_OUT_FILE_PATH + "Upload/}")
	private String upload;

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	@Value("${usm.jpa-auditing.enable:true}")
	private boolean enableJpaAuditing;

	@Value("${usm.jpa-auditing.identification:id}")
	private String identification;
	/**
	 * 是否集群
	 */
	private boolean cluster = Conts.DEFAULT_CLUSTER;

	/**
	 * 是否集群
	 * 
	 * @return
	 */
	public boolean isCluster() {
		return cluster;
	}

	/**
	 * 设置是否集群
	 * 
	 * @param cluster
	 */
	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}

	public boolean isEnableDynamicCode() {
		return enableDynamicCode;
	}

	public void setEnableDynamicCode(boolean enableDynamicCode) {
		this.enableDynamicCode = enableDynamicCode;
	}

	public int getLoginFailureTimes() {
		return loginFailureTimes;
	}

	public void setLoginFailureTimes(int loginFailureTimes) {
		this.loginFailureTimes = loginFailureTimes;
	}

	public String getActiveProfile() {
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}

	public void setSwagger(SwaggerProperties swagger) {
		this.swagger = swagger;
	}

	public void setCors(CorsProperties cors) {
		this.cors = cors;
	}

	public void setJwt(JwtProperties jwt) {
		this.jwt = jwt;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Map<String, String> getTopicMap() {
		return topicMap;
	}

	public void setTopicMap(Map<String, String> topicMap) {
		this.topicMap = topicMap;
	}

	public void setAntPattern(AntPatternProperties antPattern) {
		this.antPattern = antPattern;
	}

	/**
	 * 默认 /res/**
	 * 
	 * @return
	 */
	public String getPublicPathPatterns() {
		return publicPathPatterns;
	}

	public void setPublicPathPatterns(String publicPathPatterns) {
		this.publicPathPatterns = publicPathPatterns;
	}

	/**
	 * 默认 /local/**
	 * 
	 * @return
	 */
	public String getPretectPathPatterns() {
		return pretectPathPatterns;
	}

	public void setPretectPathPatterns(String pretectPathPatterns) {
		this.pretectPathPatterns = pretectPathPatterns;
	}

	/**
	 * <code> windows D:/UsmData/share <br>mac unix /UsmData/share </code>
	 * 
	 * @return
	 */
	public String getOutsidePublicRes() {
		return outsidePublicRes;
	}

	public void setOutsidePublicRes(String outsidePublicRes) {
		this.outsidePublicRes = outsidePublicRes;
	}

	/**
	 * window D:/UsmData/share mac unix /UsmData/share
	 * 
	 * @return
	 */
	public String getOutsideProtectedRes() {
		return outsideProtectedRes;
	}

	public void setOutsideProtectedRes(String outsideProtectedRes) {
		this.outsideProtectedRes = outsideProtectedRes;
	}

	public boolean isEnableJpaAuditing() {
		return enableJpaAuditing;
	}

	public void setEnableJpaAuditing(boolean enableJpaAuditing) {
		this.enableJpaAuditing = enableJpaAuditing;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
	}

}
