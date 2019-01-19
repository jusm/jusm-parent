package com.github.jusm.web.thymeleaf.dialect;

import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import com.github.jusm.web.thymeleaf.expression.UsmExpressionObjectFactory;

public class UsmExpressionObjectDialect implements IExpressionObjectDialect {

	public static final String NAME = "UsmStandard";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new UsmExpressionObjectFactory();
	}

}
