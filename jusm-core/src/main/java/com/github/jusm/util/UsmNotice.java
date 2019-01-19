package com.github.jusm.util;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jusm.context.UsmContext;

public class UsmNotice{
	public static void notice(Class<?> clazz, String... messages) {
		Logger logger = LoggerFactory.getLogger(clazz);
		if(messages == null)
			return;
		int max = 0;
		for (String message : messages) {
			if(message.length()>max) {
				max = message.length();
			}
		}
		StringBuilder decollators = new  StringBuilder();
		for (int i = 0; i < max+8; i++) {
			decollators.append("~");
		}
		StringBuilder sb = new  StringBuilder();
		sb.append("\n").append(decollators);
		Locale locale = Locale.getDefault(); 
		locale.getCountry();
		String message = UsmContext.getMessage("USM.NOTICE");
		sb.append("\n"+message+"\n\n");
		sb.append(String.join("\n", messages));
		sb.append("\n").append(decollators);
		logger.info(sb.toString());
	}
}
