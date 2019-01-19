package com.github.jusm.security.initializer;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.github.jusm.redis.RedisHttpSessionConfig;

/**
 * 
 * 分布式下基于Redis的session共享配置
 *
 */
public class UsmSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	public UsmSecurityInitializer() {
		super(SecurityConfig.class, RedisHttpSessionConfig.class);
	}
}