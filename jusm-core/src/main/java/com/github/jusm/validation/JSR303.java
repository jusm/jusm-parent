package com.github.jusm.validation;

import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.springframework.context.annotation.Bean;

import com.github.jusm.exception.ValidException;

/**
 * https://www.ibm.com/developerworks/cn/java/j-lo-jsr303/
 *
 */
public class JSR303 {
	
	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

	public static void validate(Object object, Class<?>... groups) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<Object>> violations = validator.validate(object, groups);
		if (!violations.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (ConstraintViolation<Object> violation : violations) {
				buf.append("\n-" + violation.getPropertyPath().toString());//TODO
				buf.append(violation.getMessage());
			}
			throw new ValidException(buf.toString());
		}
	}

}
	