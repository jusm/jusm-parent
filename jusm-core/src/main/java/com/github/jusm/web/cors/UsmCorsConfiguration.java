package com.github.jusm.web.cors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "usm.cors.enabled", havingValue = "true")
public class UsmCorsConfiguration extends WebMvcConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public UsmCorsConfiguration() {
		logger.debug("UsmCorsConfiguration instance!");
	}
	
	@Autowired
	private CorsProperties corsProperties;
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping(corsProperties.getPathPattern()).allowedOrigins(corsProperties.getAllowedOrigins())
		.allowedMethods(corsProperties.getAllowedMethods()).maxAge(corsProperties.getMaxAge())
		.allowCredentials(corsProperties.isAllowCredentials());
    }
}
