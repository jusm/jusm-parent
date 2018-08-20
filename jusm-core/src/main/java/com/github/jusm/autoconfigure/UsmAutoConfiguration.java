package com.github.jusm.autoconfigure;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.component.UsmErrorAttributes;
import com.github.jusm.hystrix.UsmHystrixCommandWapper;
import com.github.jusm.redis.RedisConfig;
import com.github.jusm.security.AntPatternProperties;
import com.github.jusm.security.CorsProperties;
import com.github.jusm.security.CurrentUser;
import com.github.jusm.security.MultiWebSecurityConfiguration;
import com.github.jusm.security.SwaggerProperties;
import com.github.jusm.security.UsmContextVariables;
import com.github.jusm.service.ParameterService;
import com.github.jusm.web.UsmLocaleResolver;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter({ WebMvcAutoConfiguration.class })
@EnableConfigurationProperties(UsmProperties.class)
@ImportAutoConfiguration(value={MultiWebSecurityConfiguration.class,RedisConfig.class,UsmBeansConfig.class})
@EnableCaching
@EnableSwagger2
public class UsmAutoConfiguration extends WebMvcConfigurerAdapter {
	@Bean
	public SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}
	
	@Bean
	public UsmHystrixCommandWapper initUsmHystrixRpcWrapper() {
		return new UsmHystrixCommandWapper();
	}

	private ServletContext servletContext;

	private ServerProperties serverProperties;

	private UsmProperties usmProperties;

	private ParameterService parameterService;

	private AntPatternProperties antPatternProperties;

	public UsmAutoConfiguration(ServletContext servletContext, ServerProperties serverProperties,
			UsmProperties usmProperties, ParameterService parameterService) {
		this.servletContext = servletContext;
		this.serverProperties = serverProperties;
		this.usmProperties = usmProperties;
		this.parameterService = parameterService;
		this.antPatternProperties = usmProperties.getAntPattern();
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
	public WebMvcConfigurer corsConfigurer() {
		final CorsProperties corsProperties = usmProperties.getCors();
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping(corsProperties.getPathPattern()).allowedOrigins(corsProperties.getAllowedOrigins())
						.allowedMethods(corsProperties.getAllowedMethods()).maxAge(corsProperties.getMaxAge())
						.allowCredentials(corsProperties.isAllowCredentials());
			}
		};
	}


	@Bean
	@DependsOn()
	public UsmContextVariables usmContextVariables() {
		return new UsmContextVariables(servletContext, antPatternProperties, usmProperties, parameterService,
				serverProperties);
	}

	// @Bean这个添加之后就不能上传了
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver bean = new CommonsMultipartResolver();
		bean.setDefaultEncoding("UTF-8");
		bean.setMaxUploadSize(8388608);
		return bean;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation("/tmp");
		return factory.createMultipartConfig();
	}

	@Bean
	@ConditionalOnProperty(value = "usm.jpa-auditing.enable", havingValue = "true", matchIfMissing = true)
	public CurrentUser currentAuditor() {
		return new CurrentUser(usmProperties.getIdentification());
	}

	@Configuration
	@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400 * 30)
	@ConditionalOnProperty(value = "usm.cluster", havingValue = "true", matchIfMissing = false)
	@ConditionalOnClass(EnableRedisHttpSession.class)
	public static class RedisHttpSessionConfig {

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error").setViewName("error");
		registry.addViewController("/403").setViewName("error/403");
		registry.addViewController("/401").setViewName("error/401");
		registry.addViewController("/404").setViewName("error/404");
	}
	
	@Bean
	public ServletRegistrationBean kaptchaServletRegistrationBean() {
		ServletRegistrationBean srb = new ServletRegistrationBean(new KaptchaServlet(), "/auth/kaptcha");
		srb.setName("KaptchaServlet");
		return srb;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:" + usmProperties.getInside(),
				"file:" + usmProperties.getOutside());
		registry.addResourceHandler("/keGkiZkhlF.txt").addResourceLocations("classpath:/keGkiZkhlF.txt");
		registry.addResourceHandler("/Ia7DOVmNCk.txt").addResourceLocations("classpath:/Ia7DOVmNCk.txt");
		registry.addResourceHandler("/lister.html").addResourceLocations("classpath:/lister.html");
		super.addResourceHandlers(registry);
	}
	
	class HttpClientConfig {

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

	}

	class SwaggerConfiguration {

		@Bean
		public Docket createRestApi(Contact contact, UsmProperties usmProperties) {
			final SwaggerProperties swaggerProperties = usmProperties.getSwagger();
			return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
					.apiInfo(apiInfo(contact, usmProperties)).select()
					.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getScanPackage()))
					.paths(PathSelectors.any()).build();
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
