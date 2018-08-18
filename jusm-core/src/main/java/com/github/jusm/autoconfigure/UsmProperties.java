package com.github.jusm.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import com.github.jusm.security.AntPatternProperties;
import com.github.jusm.security.CorsProperties;
import com.github.jusm.security.JwtProperties;
import com.github.jusm.security.SwaggerProperties;
import com.github.jusm.util.Conts;

/**
 * 统一系统管理配置属性
 */
@ConfigurationProperties(prefix = "usm", ignoreUnknownFields = true)
public class UsmProperties {
	
	private String root = Conts.DEFAULT_SUPER_ADMIN_USERNAME;
	
	private String email = Conts.DEFAULT_SUPER_ADMIN_EMAIL;
	
	
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
	 * USM 包之外的资源目录
	 */
	@Value("${usm.resources.outside:D://USM-Resources}")
	private String outside;
	
	/**
	 * USM 包之内的资源目录
	 */
	@Value("${usm.resources.inside:classpath:/" + Conts.DEFAULT_STATIC_FLODER+ "/}")
	private String inside ;

	
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

	public String getOutside() {
		return outside;
	}

	public void setOutside(String outside) {
		this.outside = outside;
	}

	public String getInside() {
		return inside;
	}

	public void setInside(String inside) {
		this.inside = inside;
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
	
	
}
