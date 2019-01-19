package com.github.jusm.autoconfigure;

import java.io.File;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.LocaleResolver;

import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.component.UsmErrorAttributes;
import com.github.jusm.context.CurrentUser;
import com.github.jusm.context.UsmContext;
import com.github.jusm.controller.AuthorizeController;
import com.github.jusm.controller.BasicConsoleController;
import com.github.jusm.controller.PermissionController;
import com.github.jusm.controller.ProfilesController;
import com.github.jusm.controller.RoleController;
import com.github.jusm.http.DefaultJusmClient;
import com.github.jusm.http.HeaderBuilder;
import com.github.jusm.http.JusmClient;
import com.github.jusm.hystrix.UsmHystrixCommandWapper;
import com.github.jusm.mail.UsmMailConfiguration;
import com.github.jusm.redis.RedisConfig;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;
import com.github.jusm.security.AntPatternProperties;
import com.github.jusm.security.JwtProperties;
import com.github.jusm.security.JwtTokenHandler;
import com.github.jusm.security.JwtUserDetailsServiceImpl;
import com.github.jusm.security.MultiWebSecurityConfiguration;
import com.github.jusm.security.SwaggerProperties;
import com.github.jusm.security.UsmRequestMatcher;
import com.github.jusm.security.filter.UsmInstallFilter;
import com.github.jusm.security.filter.UsmLoginFilter;
import com.github.jusm.service.AuthorizeService;
import com.github.jusm.service.GroupService;
import com.github.jusm.service.LoggingLevelService;
import com.github.jusm.service.ParameterService;
import com.github.jusm.service.PermissionService;
import com.github.jusm.service.ResourceService;
import com.github.jusm.service.RoleService;
import com.github.jusm.service.UserService;
import com.github.jusm.service.impl.AuthorizeServiceImpl;
import com.github.jusm.service.impl.GroupServiceImpl;
import com.github.jusm.service.impl.LoggingLevelServiceImpl;
import com.github.jusm.service.impl.ParameterServiceImpl;
import com.github.jusm.service.impl.PermissionServiceImpl;
import com.github.jusm.service.impl.ResourceServiceImpl;
import com.github.jusm.service.impl.RoleServiceImpl;
import com.github.jusm.service.impl.UserServiceImpl;
import com.github.jusm.signature.filter.HttpServletRequestWrapperFilter;
import com.github.jusm.util.Conts;
import com.github.jusm.web.ConsoleController;
import com.github.jusm.web.UsmLocaleResolver;
import com.github.jusm.web.UsmMvcConfiguration;
import com.github.jusm.web.interceptor.InterceptorConfiguration;
import com.github.jusm.web.thymeleaf.dialect.UsmExpressionObjectDialect;
import com.github.jusm.web.xxs.XssFilter;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.google.common.base.Optional;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

import io.swagger.models.Swagger;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

//@EnableTransactionManagement(proxyTargetClass=true)
@ComponentScan(basePackages = "com.github.jusm.**.component")
@EntityScan(basePackages = { "com.github.jusm.**.entity" })
@EnableJpaRepositories(basePackages = { "com.github.jusm.**.repository" })
@ConditionalOnWebApplication
@EnableConfigurationProperties(UsmProperties.class)
@ImportAutoConfiguration(value = { CorsConfiguration.class, UsmMailConfiguration.class, UsmMvcConfiguration.class,
		InterceptorConfiguration.class, MultiWebSecurityConfiguration.class, RedisConfig.class })
@EnableCaching
@EnableSwagger2
@ConditionalOnClass({ WebMvcAutoConfiguration.class })
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@Order(100 - 10)
public class UsmAutoConfiguration extends WebSecurityConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean("jwtUserDetailsServiceImpl")
	@DependsOn({ "usmRequestMatcher", "userRepository", "roleRepository" })
	public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		return new JwtUserDetailsServiceImpl(userRepository, roleRepository);
	}

	// ~ Instance Bean
	// ================================================================================================
	@Bean("jwtTokenHandler")
	public JwtTokenHandler jwtTokenHandler() {
		JwtProperties jwtProperties = usmProperties.getJwt();
		return new JwtTokenHandler(jwtProperties.getTokenPrefix(), jwtProperties.getTokenHeaderKey(),
				jwtProperties.getSecret(), jwtProperties.getExpiration(), jwtProperties.isValidation());
	}

	// ~ Instance Filter Bean
	// ================================================================================================

	@Bean
	public UsmLoginFilter usmLoginFilter(UsmRequestMatcher usmRequestMatcher) {
		return new UsmLoginFilter(usmProperties.isEnableDynamicCode(), usmProperties.getLoginFailureTimes(),
				antPatternProperties.getLogin());
	}

	@Bean
	public UsmInstallFilter usmInstallFilter(UsmRequestMatcher usmRequestMatcher) {
		String contextPath = serverProperties.getContextPath() == null ? Conts.DEFAULT_BLANK_CONTEXT_PATH
				: serverProperties.getContextPath();
		return new UsmInstallFilter(parameterService, antPatternProperties.getIndex(),
				antPatternProperties.getInstall(), antPatternProperties.getInit(), contextPath, usmRequestMatcher);
	}

	@Bean("usmRequestMatcher")
	public UsmRequestMatcher usmRequestMatcher() {
		return new UsmRequestMatcher(antPatternProperties.getPermitAllAntPatterns(),
				antPatternProperties.getWebIngoreAntPatterns(), antPatternProperties.getAuthorizeAntPatterns());
	}

	@Override
	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Value("${http.timeout.connect:5000}")
	private int connectTimeOut;

	@Value("${http.timeout.request:15000}")
	private int requestTimeout;

	@Value("${http.timeout.read:15000}")
	private int readTimeout;

	@Bean
	public AsyncHttpClient asyncHttpClient() {

		AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setConnectTimeout(connectTimeOut);
		builder.setReadTimeout(readTimeout);
		builder.setRequestTimeout(requestTimeout);
		return new AsyncHttpClient(builder.build());
	}

	@Bean
	public UsmHystrixCommandWapper usmHystrixCommandWapper() {
		return new UsmHystrixCommandWapper();
	}

	@Bean
	public DefaultJusmClient defaultJusmClient() {
		return new DefaultJusmClient();
	}

	@Bean
	public HeaderBuilder usmHeaderBuilder() {
		return new HeaderBuilder();
	}

	@Bean
	public JusmClient usmHttpClient() {
		return new JusmClient();
	}

	@Bean
	public SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}

	@Bean
	@ConditionalOnMissingBean(value = ResourceService.class)
	public ResourceService resourceService() {
		return new ResourceServiceImpl();
	}

	@Autowired
	private ParameterService parameterService;

	@Bean
	public ParameterService parameterService() {
		return new ParameterServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = PermissionService.class)
	public PermissionService permissionService() {
		return new PermissionServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = RoleService.class)
	public RoleService roleService() {
		return new RoleServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = GroupService.class)
	public GroupService groupService() {
		return new GroupServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = LoggingLevelService.class)
	public LoggingLevelService loggerLevelsService() {
		return new LoggingLevelServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = AuthorizeService.class)
	@ConditionalOnBean(AuthenticationManager.class)
	public AuthorizeService authService(AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, JwtTokenHandler jwtTokenHandler, UserService userService,
			RoleService roleService, GroupService groupService, PasswordEncoder passwordEncoder) {
		return new AuthorizeServiceImpl(authenticationManager, userDetailsService, jwtTokenHandler, userService,
				roleService, groupService, passwordEncoder);
	}

	@Bean
	@ConditionalOnMissingBean(value = UserService.class)
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = AuthorizeController.class)
	public AuthorizeController authorizeController() {
		return new AuthorizeController();
	}

	@Bean
	@ConditionalOnMissingBean(value = ConsoleController.class)
	public BasicConsoleController basicConsoleController() {
		return new BasicConsoleController();
	}

	@Bean
	@ConditionalOnMissingBean(value = PermissionController.class)
	public PermissionController permissionController() {
		return new PermissionController();
	}

	@Bean
	@ConditionalOnMissingBean(value = ProfilesController.class)
	public ProfilesController profilesController() {
		return new ProfilesController();
	}

	@Bean
	@ConditionalOnMissingBean(value = RoleController.class)
	public RoleController roleController() {
		return new RoleController();
	}

	@Bean
	@ConditionalOnMissingBean
	public UsmExpressionObjectDialect usmExpressionObjectDialect() {
		return new UsmExpressionObjectDialect();
	}

	private ServletContext servletContext;

	private ServerProperties serverProperties;

	private UsmProperties usmProperties;

	private AntPatternProperties antPatternProperties;

	public UsmAutoConfiguration(ServletContext servletContext, ServerProperties serverProperties,
			UsmProperties usmProperties) {
		logger.debug("UsmAutoConfiguration instance!");
		this.servletContext = servletContext;
		this.serverProperties = serverProperties;
		this.usmProperties = usmProperties;
		this.antPatternProperties = usmProperties.getAntPattern();
	}

	@Bean
	public ServletRegistrationBean kaptchaServletRegistrationBean() {
		ServletRegistrationBean srb = new ServletRegistrationBean(new KaptchaServlet(), Conts.DEFAULT_KAPTCHA);
		srb.setName("KaptchaServlet");
		return srb;
	}

	// @Bean
	public FilterRegistrationBean xssFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		XssFilter filter = new XssFilter();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setName("xssFilter");
		registration.setOrder(Integer.MAX_VALUE - 1);
		return registration;
	}

	@Bean
	public FilterRegistrationBean requestFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new HttpServletRequestWrapperFilter());
		// registration.addUrlPatterns("/*");
		registration.addUrlPatterns(Conts.DEFAULT_REST_SIGN_API_ANT_PATTERN);
		registration.setName("requestWrapperFilter");
		registration.setOrder(Integer.MAX_VALUE);
		return registration;
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new UsmLocaleResolver();
	}

	@Bean
	public ErrorAttributes errorAttributes() {
		return new UsmErrorAttributes();
	}

	@Bean
	public UsmContext usmContext() {
		return new UsmContext(antPatternProperties, usmProperties, serverProperties);
	}

	/**
	 * // @Bean这个添加之后就不能上传了 // public MultipartResolver multipartResolver() { //
	 * CommonsMultipartResolver bean = new CommonsMultipartResolver(); //
	 * bean.setDefaultEncoding("UTF-8"); // bean.setMaxUploadSize(8388608); //
	 * return bean; // } 上传文件时候回使用这个作为临时文件目录
	 * 
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// String tempPath = UsmContextVariables.getTempPath();
		File userDirectory = FileUtils.getUserDirectory();
		factory.setLocation(userDirectory.getPath());
		return factory.createMultipartConfig();
	}

	@Bean
	@ConditionalOnProperty(value = "usm.jpa-auditing.enable", havingValue = "true", matchIfMissing = true)
	public CurrentUser currentAuditor() {
		return new CurrentUser(usmProperties.getIdentification());
	}

	class SwaggerConfiguration {

		@Autowired
		private DocumentationCache documentationCache;

		@Autowired
		private ServiceModelToSwagger2Mapper mapper;

//		@Bean
//		public Swagger swagger() {
//			String groupName = Optional.fromNullable((String) null).or(Docket.DEFAULT_GROUP_NAME);
//			Documentation documentation = documentationCache.documentationByGroup(groupName);
//			Map<String, Documentation> all = documentationCache.all();
//			if (documentation == null) {
//				return null;
//			}
//			return mapper.mapDocumentation(documentation);
//		}

		@Bean
		public Docket createRestApi(Contact contact, UsmProperties usmProperties) {
			// final SwaggerProperties swaggerProperties = usmProperties.getSwagger();
			return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
					.apiInfo(apiInfo(contact, usmProperties)).select()
					// .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getScanPackage()))
					.paths(PathSelectors.ant(Conts.DEFAULT_REST_API_ANT_PATTERN)).build();
		}

		private ApiInfo apiInfo(Contact contact, UsmProperties usmProperties) {
			final SwaggerProperties swaggerProperties = usmProperties.getSwagger();
			return new ApiInfoBuilder().title(swaggerProperties.getTitle())
					.description(swaggerProperties.getDescription())
					.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).contact(contact)
					.version(swaggerProperties.getVersion()).build();
		}

		@Bean
		public Contact createContact(UsmProperties usmProperties) {
			final SwaggerProperties swaggerProperties = usmProperties.getSwagger();
			Contact contact = new Contact(swaggerProperties.getName(), "", "");
			return contact;
		}
	}

}
