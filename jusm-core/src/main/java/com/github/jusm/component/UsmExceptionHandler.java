package com.github.jusm.component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.jusm.entities.ApiLog;
import com.github.jusm.util.AddressUtil;
import com.github.jusm.util.IPUtil;
import com.github.jusm.xxs.XssHttpServletRequestWrapper;


/**
 * USM异常处理程序
 */
@ControllerAdvice
public class UsmExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(UsmExceptionHandler.class);

//	@Autowired
//	private MailUtil mailUtil;

	@ExceptionHandler(value = Exception.class)
	public String errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		handler(request, e);
		request.setAttribute("javax.servlet.error.exception", e);
		return "forward:/error";
	}

	private void handler(HttpServletRequest request, Exception e) throws InterruptedException {
		String requestURL = request.getRequestURL().toString();
		String ipAddress = IPUtil.getIpAddress(request);
		logger.error("RequestIP: " + ipAddress);
		String authorization = request.getHeader("Authorization");
		logger.error("Authorization: " + authorization);
		ApiLog entity = new ApiLog();
		if (request != null && request instanceof XssHttpServletRequestWrapper) {
			XssHttpServletRequestWrapper xsrw = (XssHttpServletRequestWrapper) request;
			String originalBody = xsrw.getOriginalBody();
			logger.error(String.format("入参:\n %s", originalBody));
			entity.setParams(originalBody);
		}
		entity.setToken(authorization);
		logger.error(requestURL, e);
		entity.setException(e);
		entity.setRequestIP(ipAddress);
		entity.setRequestUrl(requestURL);
		entity.setServerIP(serverIp);
		entity.setSuccess(false);
		queue.put(entity);
	}

	private final BlockingQueue<ApiLog> queue = new ArrayBlockingQueue<ApiLog>(1);
	private String serverIp;

	@PostConstruct // 定义在构造方法完毕后，执行这个初始化方法
	public void init() {
		serverIp = AddressUtil.getHostAddress();
		logger.info("IP:{} startup saving apilog thread.", serverIp);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					ApiLog log = null;
					try {
						log = queue.take();
						if (!log.isSuccess()) {
							String trakeError = "";
							if (log.getException() != null) {
								trakeError = printStackTraceToString(log.getException());
							}
							String mailSubject = "《接口故障监控报警》(WEB服务接口平台USM)";
							String mailBody = MessageFormat.format(
									"请求IP:{0} \n 请求地址: {1}\n  入参: \n {2}\n  Authorization: \n {3} \n  服务器IP地址:{4}\n  请求时间:{5}\n  异常堆栈:\n{6}",
									log.getRequestIP(), log.getRequestUrl(), log.getParams(), log.getToken(),
									log.getServerIP(), log.getRequestTime(), trakeError);
							logger.info("send mail");
//							boolean success = mailUtil.sendMail(null, mailSubject, mailBody, false, null);
//							if(!success) {
//								logger.info("send mail failed");
//							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						logger.error("Send mail failed!", e);
					}
				}
			}
		}).start();
	}

	private static String printStackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}
}
