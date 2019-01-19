package com.github.jusm.web.thymeleaf.expression;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class UsmExpressionObjectFactory implements IExpressionObjectFactory {
	public static final String OSS_EXPRESSION_OBJECT_NAME = "oss";

	protected static final Set<String> ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(
			new LinkedHashSet<String>(java.util.Arrays.asList(new String[] { OSS_EXPRESSION_OBJECT_NAME })));

	@Override
	public Set<String> getAllExpressionObjectNames() {
		return ALL_EXPRESSION_OBJECT_NAMES;
	}

	@Override
	public Object buildObject(IExpressionContext context, String expressionObjectName) {
		 if (context instanceof ITemplateContext) {
             return new Oss((ITemplateContext)context);
         }
		return null;
	}

	@Override
	public boolean isCacheable(String expressionObjectName) {
		return (expressionObjectName != null);
	}

}
