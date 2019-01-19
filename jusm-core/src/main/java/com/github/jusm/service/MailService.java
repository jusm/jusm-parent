package com.github.jusm.service;

import java.util.Map;

/**
 * 邮件服务
 * 
 * @author haoran.wen
 */
public interface MailService {

	/**
	 * 发送简单邮件
	 * 
	 * @param to
	 *            收件人
	 * @param subject
	 *            主题
	 * @param text
	 *            文本
	 */
	void sendSimpleMailMessage(String to, String subject, String text);

	/**
	 * 发送HTML邮件
	 * 
	 * @param to
	 *            收件人
	 * @param subject
	 *            主题
	 * @param text
	 *            文本
	 */
	void sendHtmlMailMessage(String to, String subject, String text);
	
	
	/**
	 * 发送模板邮件
	 * 
	 * @param to
	 *            收件人
	 * @param subject
	 *            主题
	 * @param params
	 *            文本
	 */
	void sendTemplateMailMessage(String to, String subject, Map<String,Object> params);
	

	/**
	 * 发送附件邮件
	 * 
	 * @param to
	 *            收件人
	 * @param subject
	 *            主题
	 * @param text
	 *            文本
	 */
	void sendAttachmentMailMessage(String to, String subject, String filePath, String text);

}
