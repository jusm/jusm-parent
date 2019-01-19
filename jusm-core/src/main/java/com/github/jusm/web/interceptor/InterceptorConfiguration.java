package com.github.jusm.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jusm.signature.interceptor.SignatureInterceptor;
import com.github.jusm.util.Conts;

@Configuration
@ConditionalOnWebApplication
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public InterceptorConfiguration() {
		logger.debug(this.getClass() +  "instance!");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		HandlerInterceptor interceptor = new SignatureInterceptor();
		registry.addInterceptor(interceptor).addPathPatterns(Conts.DEFAULT_REST_SIGN_API_ANT_PATTERN);
		super.addInterceptors(registry);
	}

}
