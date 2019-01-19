package com.github.jusm.mail;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import com.github.jusm.service.MailService;
import com.github.jusm.service.impl.MailServiceImpl;

public class UsmMailConfiguration {

	@Bean
	public MailService mailService(JavaMailSender javaMailSender, TemplateEngine templateEngine, MailProperties mailProperties) {
		return new MailServiceImpl(javaMailSender, templateEngine, mailProperties.getUsername());
	}
}
