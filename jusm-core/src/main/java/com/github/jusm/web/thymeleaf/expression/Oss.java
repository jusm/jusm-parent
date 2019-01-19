package com.github.jusm.web.thymeleaf.expression;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.spring4.context.SpringContextUtils;

import com.github.jusm.service.ResourceService;

public final class Oss {

	private final ITemplateContext context;

	private final ResourceService resourceService;

	public Oss(ITemplateContext context) {
		super();
		this.context = context;
		this.resourceService = SpringContextUtils.getApplicationContext(context).getBean(ResourceService.class);
	}

	public String select(int id) {
		String url = resourceService.generateURI(id);
		return url;
	}

	public String select(String name) {
		String url = resourceService.generateURI(name);
		return url;
	}
}
