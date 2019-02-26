package com.github.jusm.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.context.UsmContext;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.security.filter.JwtAuthenticationTokenFilter;
import com.github.jusm.security.filter.KaptchaAuthenticationFilter;
import com.github.jusm.security.filter.SmsCodeAuthenticationProcessingFilter;
import com.github.jusm.security.filter.UsmInstallFilter;
import com.github.jusm.security.filter.UsmLoginFilter;
import com.github.jusm.security.filter.UsmSecurityInterceptorFilter;
import com.github.jusm.security.handler.UsmAccessDeniedHandler;
import com.github.jusm.security.handler.UsmAuthenticationFailureHandler;
import com.github.jusm.security.handler.UsmAuthenticationSuccessHandler;
import com.github.jusm.security.handler.UsmLogoutHandler;
import com.github.jusm.security.handler.UsmLogoutSuccessHandler;
import com.github.jusm.security.provider.MobileCodeAuthenticationProvider;
import com.github.jusm.security.provider.UsernamePasswordAuthenticationProvider;
import com.github.jusm.service.ParameterService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.web.cors.CorsProperties;

/**
 * @formatter:off
 * 
 * 				TODO 这个类写的真是差 等基础功能做好了之后回来再重构这个类
 * 
 * 
 * 
 * 
 *                我们可以配置多个HttpSecurity实例，就像我们可以有多个<http>块.
 *                关键在于对WebSecurityConfigurationAdapter进行多次扩展。 例如下面是
 *                一个对/api/开头的URL进行的不同的设置。 一个对/db-console/开头的URL进行的不同的设置。
 *                其他URL走表单 方便使用成员变量写在了一个类中
 * 
 * @Resource(name="JwtUserDetailsServiceImpl") 构建war包的时候一定不要用Resource @Qualifier("JwtUserDetailsServiceImpl")
 * @Autowired private UserDetailsService userDetailsService;
 * @formatter:on
 */
// @Configuration
// @ConditionalOnWebApplication
// @AutoConfigureAfter(UsmBeansConfig.class)
@EnableConfigurationProperties({ UsmProperties.class })
@ConditionalOnClass({ WebMvcAutoConfiguration.class })
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MultiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	// ~ Instance fields
	// ================================================================================================

	private AntPatternProperties antPatternProperties;

	private CorsProperties corsProperties;

	private ServerProperties serverProperties;

	private UsmProperties usmProperties;

	private ParameterService parameterService;

	@Autowired
	@Qualifier("jwtUserDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	// ~ Instance Constructor
	// ================================================================================================

	public MultiWebSecurityConfiguration(@Autowired UsmContext usmContext) {
		logger.debug(this.getClass().getSimpleName() + "instance!");
		this.usmProperties = usmContext.getUsmProperties();
		this.antPatternProperties = usmProperties.getAntPattern();
		this.corsProperties = usmProperties.getCors();
		this.serverProperties = usmContext.getServerProperties();
		// this.parameterService = usmContextVariables.getParameterService();
	}

	@Bean
	public SmsCodeAuthenticationProcessingFilter mobileCodeAuthenticationProcessingFilter() throws Exception {
		SmsCodeAuthenticationProcessingFilter filter = new SmsCodeAuthenticationProcessingFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}

	@Bean
	@DependsOn({ "usmRequestMatcher", "jwtUserDetailsServiceImpl", "jwtTokenHandler" })
	public DaoAuthenticationProvider usernamePasswordAuthenticationProvider() {
		DaoAuthenticationProvider usernamePasswordAuthenticationProvider = new UsernamePasswordAuthenticationProvider();
		usernamePasswordAuthenticationProvider.setUserDetailsService(userDetailsService);
		usernamePasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return usernamePasswordAuthenticationProvider;
	}

	@Bean
	public MobileCodeAuthenticationProvider mobileCodeAuthenticationProvider() {
		return new MobileCodeAuthenticationProvider();
	}

	@Bean
	public UsmAuthenticationSuccessHandler usmAuthenticationSuccessHandler() {
		return new UsmAuthenticationSuccessHandler(serverProperties.getContextPath(),
				usmProperties.getSuccessLoginDispatcherMap());
	}

	@Bean
	public UsmLogoutSuccessHandler usmLogoutSuccessHandler() {
		return new UsmLogoutSuccessHandler();
	}

	@Bean
	public UsmLogoutHandler usmLogoutHandler() {
		return new UsmLogoutHandler();
	}

	@Bean
	public UsmAuthenticationFailureHandler usmAuthenticationFailureHandler() {
		return new UsmAuthenticationFailureHandler();
	}

	@Bean
	public KaptchaAuthenticationFilter kaptchaAuthenticationFilter(AuthenticationManager authenticationManager) {
		return new KaptchaAuthenticationFilter(antPatternProperties.getLogin(), "POST", antPatternProperties.getLogin(),
				authenticationManager);
	}

	@Bean
	@DependsOn({ "usmRequestMatcher", "jwtUserDetailsServiceImpl", "jwtTokenHandler", "userService" })
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(UserService userService,
			UsmRequestMatcher usmRequestMatcher, JwtTokenHandler jwtTokenHandler) {
		return new JwtAuthenticationTokenFilter(userDetailsService, userService, usmRequestMatcher, jwtTokenHandler,
				corsProperties);
	}

	@Bean
	public UsmFilterInvocationSecurityMetadataSource usmFilterInvocationSecurityMetadataSource(
			PermissionRepository permissionRepository, UsmRequestMatcher usmRequestMatcher) {
		return new UsmFilterInvocationSecurityMetadataSource(permissionRepository, usmRequestMatcher);
	}

	@Bean
	public UsmSecurityInterceptorFilter usmSecurityInterceptorFilter(
			FilterInvocationSecurityMetadataSource securityMetadataSource,
			JwtAccessDecisionManager jwtAccessDecisionManager, AuthenticationManager authenticationManager) {
		return new UsmSecurityInterceptorFilter(securityMetadataSource, jwtAccessDecisionManager,
				authenticationManager);
	}

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public JwtAccessDecisionManager jwtAccessDecisionManager() {
		return new JwtAccessDecisionManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JdbcTokenRepositoryImpl tokenRepository(@Autowired DataSource dataSource) {
		JdbcTokenRepositoryImpl j = new JdbcTokenRepositoryImpl();
		j.setDataSource(dataSource);
		return j;
	}

	/**
	 * 全局配置
	 * 
	 * @throws Exception
	 */
	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(mobileCodeAuthenticationProvider())
				.authenticationProvider(usernamePasswordAuthenticationProvider());
		auth.userDetailsService(userDetailsService);
	}

	// @Configuration
	// @ConditionalOnBean(FilterInvocationSecurityMetadataSource.class)
	// @Order(Ordered.HIGHEST_PRECEDENCE + 100)
	// public class ApiWebSecurityConfigurationAdapter extends
	// WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private UsmSecurityInterceptorFilter usmSecurityInterceptorFilter;

	@Autowired
	private UsmFilterInvocationSecurityMetadataSource usmFilterInvocationSecurityMetadataSource;
	//
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * /api/** /sign-api/**
	 */
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		String[] authorizedAntPatterns = antPatternProperties.getAuthorizeAntPatterns();
		logger.info("Restful API request mapping pattern:  " + Arrays.toString(authorizedAntPatterns));
		String[] antPatterns = antPatternProperties.getPermitAllAntPatterns();
		httpSecurity.authorizeRequests().antMatchers(antPatterns).permitAll();
		for (String antPattern : antPatterns) {
			httpSecurity.antMatcher(antPattern).cors().and().authorizeRequests().antMatchers(antPattern).permitAll();
		}
		httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		// httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		// .authorizeRequests()
		// // 自定义FilterInvocationSecurityMetadataSource
		// .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>()
		// {
		// @Override
		// public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
		// usmFilterInvocationSecurityMetadataSource
		// .setDefaultFilterInvocationSecurityMetadataSource(fsi.getSecurityMetadataSource());
		// fsi.setSecurityMetadataSource(usmFilterInvocationSecurityMetadataSource);
		// return fsi;
		// }
		// });
		httpSecurity.authorizeRequests().antMatchers(authorizedAntPatterns).authenticated();
		httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and();
		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterBefore(usmSecurityInterceptorFilter, FilterSecurityInterceptor.class);
		httpSecurity.httpBasic().and().headers().cacheControl();
		// }
	}

	/**
	 * 数据库管理员登录
	 */
	// @Configuration
	// @Order(Ordered.HIGHEST_PRECEDENCE + 200)
	// public class DBWebSecurityConfigurationAdapter extends
	// WebSecurityConfigurerAdapter {
	// @Autowired
	// private UsmRequestMatcher usmRequestMatcher;
	//
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().requestMatchers(usmRequestMatcher.getAuthorizeRequestMatcher()).permitAll();
	// http.csrf().disable();// 这个想办法去掉这个
	// http.antMatcher("/h2-console/**").authorizeRequests().anyRequest().hasAuthority(Conts.RoleNumber.ROLE_DBA)
	// .and().headers().frameOptions().sameOrigin();
	// }
	// }

	private void configIgnoringAntMatcher(WebSecurity web) {
		web.ignoring().antMatchers(antPatternProperties.getWebIngoreAntPatterns()).antMatchers("/js/**", "/css/**",
				Conts.DEFAULT_OUT_STATIC_PATH_PATTERN, "/img/**", "/imgs/**", "/images/**", "/**/*.js", "/**/*.css",
				"/**/*.png", "/**/*.jpg", Conts.DEFAULT_REST_API_ANT_PATTERN, Conts.DEFAULT_REST_SIGN_API_ANT_PATTERN);
	}

	@Configuration
	@Order(Ordered.HIGHEST_PRECEDENCE + 300)
	public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			configIgnoringAntMatcher(web);
		}

		@Autowired
		private UsmRequestMatcher usmRequestMatcher;

		@Autowired
		private UsmAuthenticationSuccessHandler usmAuthenticationSuccessHandler;

		@Autowired
		private UsmLogoutSuccessHandler usmLogoutSuccessHandler;

		@Autowired
		private UsmLogoutHandler usmLogoutHandler;

		@Autowired
		private UsmAuthenticationFailureHandler failureHandler;

		private String key = "<haoran_wen,jing_zhu>";

		@Autowired
		private PersistentTokenRepository tokenRepository;

		@Autowired
		private UsmSecurityInterceptorFilter usmSecurityInterceptorFilter;

		@Autowired
		private UsmLoginFilter usmLoginFilter;

		@Autowired
		private UsmInstallFilter usmInstallFilter;

		@Autowired
		private KaptchaAuthenticationFilter kaptchaAuthenticationFilter;

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {

			failureHandler.setDefaultFailureUrl(antPatternProperties.getLogin());
			httpSecurity.exceptionHandling().authenticationEntryPoint(new UsmAuthenticationEntryPoint())
					.accessDeniedHandler(new UsmAccessDeniedHandler());
			 httpSecurity.csrf().ignoringAntMatchers("/deploy.html");
			// CORS ant-pattern
			httpSecurity.authorizeRequests().antMatchers(antPatternProperties.getAuthorizeAntPatterns()).permitAll();
			httpSecurity.authorizeRequests().requestMatchers(usmRequestMatcher.getAuthorizeRequestMatcher())
					.permitAll();
			// httpSecurity.authorizeRequests().antMatchers("/swagger-resources/**","/swagger-ui.html","/v2/api-docs").permitAll();

			// USM 平台内部的允许访问路径
			String[] antPatterns = Arrays
					.asList(antPatternProperties.getAuth(), antPatternProperties.getInstall(),
							antPatternProperties.getIndex(), "/", antPatternProperties.getLogin(),
							antPatternProperties.getRegister(), antPatternProperties.getInit())
					.toArray(new String[] {});
			httpSecurity.authorizeRequests().antMatchers(antPatterns).permitAll();
			// httpSecurity.authorizeRequests().regexMatchers(HttpMethod.GET,
			// "/login").permitAll();

			httpSecurity.authorizeRequests().anyRequest().authenticated().and().formLogin()
					.loginPage(antPatternProperties.getLogin()).loginProcessingUrl(antPatternProperties.getSignIn())
					/* .defaultSuccessUrl("/home",true) */.successHandler(usmAuthenticationSuccessHandler)
					.failureHandler(failureHandler).permitAll().and().headers().frameOptions().sameOrigin()// 表示该页面可以在相同域名页面的frame
																											// 中展示
					.and().sessionManagement().invalidSessionUrl(antPatternProperties.getLogin()).maximumSessions(1)// 最大session并发数量1
					.maxSessionsPreventsLogin(false)// false之后登录踢掉之前登录,true则不允许之后登录
					.expiredSessionStrategy(new UsmExpiredSessionStrategy())// 登录被踢掉时的自定义操作
					.expiredUrl(antPatternProperties.getIndex()).sessionRegistry(new SessionRegistryImpl()).and().and()
					.logout().addLogoutHandler(usmLogoutHandler).permitAll()
					.logoutSuccessUrl(antPatternProperties.getIndex()).invalidateHttpSession(true).and().rememberMe()
					.key(key).tokenValiditySeconds(Conts.TOKEN_VALIDITY_SECONDS).tokenRepository(tokenRepository);// 指定记住登录信息所使用的数据源
			httpSecurity.addFilterBefore(usmSecurityInterceptorFilter, FilterSecurityInterceptor.class);
			httpSecurity.addFilterBefore(usmInstallFilter, ChannelProcessingFilter.class);
			httpSecurity.addFilterAfter(usmLoginFilter, AnonymousAuthenticationFilter.class);
			if (usmLoginFilter.isVerificationCode()) {
				httpSecurity.addFilterBefore(kaptchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			}
		}
	}

	class UsmExpiredSessionStrategy implements SessionInformationExpiredStrategy {
		@Override
		public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
				throws IOException, ServletException {
			event.getResponse().setContentType("application/json;charset=UTF-8");
			event.getResponse().getWriter().write("您的账号在另一处登录!");
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		configIgnoringAntMatcher(web);
	}

}
