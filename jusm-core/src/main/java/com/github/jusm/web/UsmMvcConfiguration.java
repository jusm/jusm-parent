package com.github.jusm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.context.UsmContext;

@Configuration
@ConditionalOnWebApplication
public class UsmMvcConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private UsmProperties usmProperties;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error").setViewName("error");
		registry.addViewController("/video").setViewName("video");
		registry.addViewController("/403").setViewName("error/403");
		registry.addViewController("/401").setViewName("error/401");
		registry.addViewController("/404").setViewName("error/404");
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler(usmProperties.getPublicPathPatterns()).addResourceLocations(
				"file:" + UsmContext.getSharePath(), "file:" + UsmContext.getUploadPath());
		registry.addResourceHandler(usmProperties.getPretectPathPatterns())
				.addResourceLocations("file:" + UsmContext.getLocalPath());

		// registry.addResourceHandler("/keGkiZkhlF.txt").addResourceLocations("classpath:/keGkiZkhlF.txt");
		// registry.addResourceHandler("/Ia7DOVmNCk.txt").addResourceLocations("classpath:/Ia7DOVmNCk.txt");
		// registry.addResourceHandler("/lister.html").addResourceLocations("classpath:/lister.html");
		super.addResourceHandlers(registry);
	}
}
