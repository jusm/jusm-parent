package com.github.jusm.context;

import static com.github.jusm.util.Conts.USM_CONTEXT_FULLNAME;
import static com.github.jusm.util.Conts.USM_CONTEXT_PATH;
import static com.github.jusm.util.Conts.USM_CONTEXT_REQUEST_MAPPING_INITIALIZE;
import static com.github.jusm.util.Conts.USM_CONTEXT_REQUEST_MAPPING_REGISTER;
import static com.github.jusm.util.Conts.USM_CONTEXT_REQUEST_MAPPING_SIGNIN;
import static com.github.jusm.util.Conts.USM_CONTEXT_REQUEST_MAPPING_SIGNOUT;
import static com.github.jusm.util.Conts.USM_CONTEXT_SHORTNAME;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.entity.Parameter;
import com.github.jusm.listener.StartupApplicationListener;
import com.github.jusm.security.AntPatternProperties;
import com.github.jusm.service.ParameterService;
import com.github.jusm.util.Conts;

import io.swagger.models.Swagger;

/**
 * 这个类的作用主要是讲后台应用范围内数据传递给前端页面
 */
public class UsmContext implements StartupApplicationListener.StartupJob {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

	private final static Map<String, Object> variables = new ConcurrentHashMap<>();

	private final static String TEMP_PATH = "TEMP_PATH";

	private final static String SHARE_PATH = "SHARE_PATH";

	private final static String LOCAL_PATH = "LOCAL_PATH";

	private final static String UPLOAD_PATH = "UPLOAD_PATH";

	private final static String USER_DIRECTORY = "USER_DIRECTORY";

	private final static String ROOT_USERNAME = "ROOT_USERNAME";

	private final static String ROOT_USERMAIL = "ROOT_USERMAIL";

	private final static String CONFIG_PATH = "CONFIG_PATH";

	@Autowired
	private ServletContext servletContext;

	private AntPatternProperties antPatternProperties;

	private UsmProperties usmProperties;

	private ServerProperties serverProperties;

	@Autowired
	private ParameterService parameterService;

	private static boolean systemInitialized = false;

	public UsmContext(AntPatternProperties antPatternProperties, UsmProperties usmProperties,
			ServerProperties serverProperties) {
		this.antPatternProperties = antPatternProperties;
		this.usmProperties = usmProperties;
		this.serverProperties = serverProperties;
	}

	@PostConstruct
	public void refreshUsmContextVariables() {
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_INITIALIZE, antPatternProperties.getInit());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_SIGNIN, antPatternProperties.getLogin());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_SIGNOUT, antPatternProperties.getSignOut());
		servletContext.setAttribute(USM_CONTEXT_REQUEST_MAPPING_REGISTER, antPatternProperties.getRegister());
		servletContext.setAttribute(USM_CONTEXT_FULLNAME, usmProperties.getFullname());
		servletContext.setAttribute(USM_CONTEXT_SHORTNAME, usmProperties.getShortname());
		servletContext.setAttribute(Conts.USM_CONTEXT_SYSTEM_VER, DateTime.now().toString("yyyy.MM.dd.HH:mm"));
		servletContext.setAttribute(Conts.USM_CONTEXT_CURRENT_YEAR, DateTime.now().toString("yyyy"));
		String contextPath = serverProperties.getContextPath() == null ? "/" : serverProperties.getContextPath();
		servletContext.setAttribute(USM_CONTEXT_PATH, contextPath);

		// 系统数据文件路径
		initDateDirector();

		// 加载根用户信息
		loadRootUserInfo();

		// 加载刷新Parameter
		refreshParameter();
	}

	private void refreshParameter() {
		variables.put(ROOT_USERNAME, usmProperties.getRoot());
		parameterService
				.save(Parameter.SYS_HTTP_SESSION_TIMEOUT.setParamValue(String.valueOf(Conts.HTTPSESSION_TIMEOUT)));
		parameterService.save(Parameter.SYS_MAX_IMUM_SESSIONS.setParamValue(String.valueOf(Conts.MAX_IMUM_SESSIONS)));
	}

	private void loadRootUserInfo() {
		variables.put(ROOT_USERNAME, usmProperties.getRoot());
		variables.put(ROOT_USERMAIL, usmProperties.getEmail());
		variables.put(CONFIG_PATH, usmProperties.getConfigPath());
	}

	// @Scheduled(fixedDelay = 5000)
	public void testTaskWithDate() {
		logger.info("testTaskWithDate");
	}

	/**
	 * 初始化系统路径
	 */
	private void initDateDirector() {
		try {
			String activeProfile = usmProperties.getActiveProfile();
			String activeProfilePath = File.separator + activeProfile;
			String os = System.getProperty("os.name");
			String sharePath = activeProfilePath + usmProperties.getOutsidePublicRes();
			if (os.toLowerCase().startsWith("win")) {
				sharePath = Conts.DEFAULT_DIST + sharePath;
				File file = new File(sharePath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			} else {
				File file = new File(sharePath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			}

			String localPath = activeProfilePath + usmProperties.getOutsideProtectedRes();
			if (os.toLowerCase().startsWith("win")) {
				localPath = Conts.DEFAULT_DIST + localPath;
				File file = new File(localPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			} else {
				File file = new File(localPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			}

			String uploadPath = activeProfilePath + usmProperties.getUpload();
			if (os.toLowerCase().startsWith("win")) {
				uploadPath = Conts.DEFAULT_DIST + uploadPath;
				File file = new File(uploadPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			} else {
				File file = new File(uploadPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			}

			String tempPath = activeProfilePath + usmProperties.getTemp();
			if (os.toLowerCase().startsWith("win")) {
				tempPath = Conts.DEFAULT_DIST + tempPath;
				File file = new File(tempPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			} else {
				File file = new File(tempPath);
				if (!file.exists() || !file.isDirectory()) {
					FileUtils.forceMkdir(file);
					logger.info(file.getAbsolutePath() + "created OK!");
				}
			}
			variables.put(SHARE_PATH, sharePath);
			variables.put(TEMP_PATH, tempPath);
			variables.put(LOCAL_PATH, localPath);
			variables.put(UPLOAD_PATH, uploadPath);
			variables.put(USER_DIRECTORY, FileUtils.getUserDirectory());

			Collection<Parameter> collection = new HashSet<>();
			collection.add(Parameter.SYS_LOCAL_PATH.setParamValue(localPath));
			collection.add(Parameter.SYS_SHARE_PATH.setParamValue(sharePath));
			collection.add(Parameter.SYS_UPLOAD_PATH.setParamValue(uploadPath));
			collection.add(Parameter.SYS_TEMP_PATH.setParamValue(tempPath));
			parameterService.save(collection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 系统是否初始化
	 * 
	 * @return
	 */
	public static boolean isSystemInitialized() {
		return systemInitialized;
	}

	/**
	 * 设置系统是否初始化标识
	 * 
	 * @param initialized
	 */
	public static synchronized void setSystemInitialized(boolean initialized) {
		systemInitialized = initialized;
	}

	/**
	 * 超级用户名称
	 * 
	 * @return
	 */
	public static String getRootUsername() {
		return (String) variables.get(ROOT_USERNAME);
	}

	/**
	 * 超级用户名称
	 * 
	 * @return
	 */
	public static String getRootEmail() {
		return (String) variables.get(ROOT_USERNAME);
	}

	/**
	 * 配置文件路径
	 * 
	 * @return
	 */
	public static String getConfigPath() {
		return (String) variables.get(CONFIG_PATH);
	}

	public static String getUploadPath() {
		Object object = variables.get(UPLOAD_PATH);
		return object == null ? "" : object.toString();
	}

	public static File getUserDirectory() {
		Object object = variables.get(USER_DIRECTORY);
		return object == null || !(object instanceof File) ? null : (File) object;
	}

	public static String getSharePath() {
		Object object = variables.get(SHARE_PATH);
		return object == null ? "" : object.toString();
	}

	public static String getLocalPath() {
		Object object = variables.get(LOCAL_PATH);
		return object == null ? "" : object.toString();
	}

	public static String getTempPath() {
		Object object = variables.get(TEMP_PATH);
		return object == null ? "" : object.toString();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public AntPatternProperties getAntPatternProperties() {
		return antPatternProperties;
	}

	public UsmProperties getUsmProperties() {
		return usmProperties;
	}

	// public ParameterService getParameterService() {
	// return parameterService;
	// }

	public ServerProperties getServerProperties() {
		return serverProperties;
	}

	@Override
	public void run(ContextRefreshedEvent event) {
		Swagger swagger = SpringContextHolder.getBean(Swagger.class);
		servletContext.setAttribute(Conts.USM_CONTEXT_API_COUNT, swagger.getPaths().size());
	}

	public static final Locale LOCALE = Locale.getDefault();

	public static final String LANGUAGE = Locale.getDefault().getLanguage();

	public static final String COUNTRY = Locale.getDefault().getCountry();

	public static final String getMessage(String code) {
		return getMessage(code, null, null, LOCALE);
	}

	public static final String getMessage(String code, Object[] args) {
		return getMessage(code, args, null, LOCALE);
	}

	public static final String getMessage(String code, String defaultMessage) {
		return getMessage(code, null, defaultMessage, LOCALE);
	}

	public static final String getMessage(String code, Object[] args, String defaultMessage) {
		return getMessage(code, args, defaultMessage, LOCALE);
	}

	public static final String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}
}