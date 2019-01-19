package com.github.jusm.service.impl;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateTime;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;

import com.github.jusm.exception.UsmException;
import com.github.jusm.service.MailService;
import com.github.jusm.web.WebContextHolder;

public class MailServiceImpl implements MailService {

	private JavaMailSender javaMailSender;

	private TemplateEngine templateEngine;

	private String from;

	public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, String from) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
		this.from = from;
	}

	/**
	 * 发送简单邮件
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 */
	@Override
	@Async
	public void sendSimpleMailMessage(String to, String subject, String text) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setSubject(subject);
		smm.setTo(to);
		smm.setFrom(from);
		smm.setText(text);
		javaMailSender.send(smm);
	}

	@Override
	@Async
	public void sendHtmlMailMessage(String to, String subject, String text) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new UsmException("Html邮件发送失败", e);
		}
	}

	@Override
//	@Async
	public void sendTemplateMailMessage(String to, String subject, Map<String, Object> variables) {
		try {
//			HttpServletRequest request = RequestContextHolderUtil.getRequest();
//			HttpServletResponse response = RequestContextHolderUtil.getResponse();
//			ServletContext servletContext = RequestContextHolderUtil.getServletContext();
			variables.put("adminemail", "jsum@126.com");
			variables.put("time", DateTime.now().toString("yyyyMMdd HHmmss"));
			IWebContext context = new WebContext(WebContextHolder.getRequest(), WebContextHolder.getResponse(),
					WebContextHolder.getContext(), Locale.getDefault(), variables);
			String content = this.templateEngine.process("mail", context);
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new UsmException("Html邮件发送失败", e);
		}
	}

	@Override
	@Async
	public void sendAttachmentMailMessage(String to, String subject, String filePath, String text) {
		try {
			File attachmentFile = new File(filePath);
			FileSystemResource fileResource = new FileSystemResource(attachmentFile);
			if (attachmentFile.exists()) {
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(from);
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(text, true);
				helper.addAttachment(fileResource.getFilename(), fileResource);
				javaMailSender.send(mimeMessage);
			} else {
				// throw new Y
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new UsmException("Html邮件发送失败", e);
		}
	}
}
