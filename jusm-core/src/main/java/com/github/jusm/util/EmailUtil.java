package com.github.jusm.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.github.jusm.entity.EmailAccount;
import com.github.jusm.entity.MessageInfo;

 
public class EmailUtil {
	
	@SuppressWarnings("restriction")
	public static boolean sslSend(MessageInfo msgInfo, EmailAccount emailAccount) 
            throws AddressException, MessagingException, IOException{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
          final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
          // Get a Properties object
          Properties props = new Properties();
          props.setProperty("mail.smtp.host", emailAccount.getPlace());
          props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
          props.setProperty("mail.smtp.socketFactory.fallback", "false");
          props.setProperty("mail.smtp.port", "465");
          props.setProperty("mail.smtp.socketFactory.port", "465");
          props.put("mail.smtp.auth", "true");
          
          final String username = emailAccount.getUsername();
          final String password = emailAccount.getPassword();
          Session session = Session.getDefaultInstance(props, new Authenticator(){
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(username, password);
              }});
          Message msg = new MimeMessage(session);
         
          // 设置发件人和收件人
          msg.setFrom(new InternetAddress(emailAccount.getUsername()));
          List<String> tos = msgInfo.getTo();
          Address to[] = new InternetAddress[tos.size()];
          for(int i=0;i<tos.size();i++){
              to[i] = new InternetAddress(tos.get(i));
          }
          // 多个收件人地址
          msg.setRecipients(Message.RecipientType.TO, to);
          msg.setSubject(msgInfo.getSubject()); // 标题
          msg.setText(msgInfo.getMsg());// 内容
          msg.setSentDate(new Date());
          Transport.send(msg);
          System.out.println("EmailUtil ssl协议邮件发送打印" +msg.toString());
          return true;
         }

    private EmailUtil() {
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return 正确返回 true
     */
    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     *  Gravatar 头像
     * @param message email
     * @return hash
     */
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {
        }
        return null;
    }

    public static void main(String[] args) throws AddressException, MessagingException, IOException {
//        System.out.println(checkEmail("910@q.com"));
        MessageInfo msg= new MessageInfo();
		msg.setFrom("18565826287@163.com");
		msg.setSubject("邮件主题");
		msg.setTo(Arrays.asList("haoran.wen@7road.com"));
		msg.setMsg("测试测试邮件");
		msg.setSendDate(new Date());
		EmailAccount emailAccount = new EmailAccount();
		emailAccount.setPassword("wenhaoran163");
		emailAccount.setUsername("18565826287@163.com");
		emailAccount.setPlace("smtp.163.com");
		boolean sslSend = EmailUtil.sslSend(msg, emailAccount);
    }
}
