package com.github.jusm.security;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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
import org.springframework.security.config.annotation.ObjectPostProcessor;
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

import com.github.jusm.autoconfigure.UsmAutoConfiguration;
import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;
import com.github.jusm.security.filter.JwtAuthenticationTokenFilter;
import com.github.jusm.security.filter.KaptchaAuthenticationFilter;
import com.github.jusm.security.filter.MobileCodeAuthenticationProcessingFilter;
import com.github.jusm.security.filter.UsmInstallFilter;
import com.github.jusm.security.filter.UsmLoginFilter;
import com.github.jusm.security.filter.UsmSecurityInterceptorFilter;
import com.github.jusm.security.handler.UsmAccessDeniedHandler;
import com.github.jusm.security.handler.UsmAuthenticationFailureHandler;
import com.github.jusm.security.handler.UsmAuthenticationSuccessHandler;
import com.github.jusm.security.provider.MobileCodeAuthenticationProvider;
import com.github.jusm.security.provider.UsernamePasswordAuthenticationProvider;
import com.github.jusm.service.ParameterService;

/**
 * @formatter:off
 * 
 * TODO 这个类写的真是差    等基础功能做好了之后回来再重构这个类
 * 
 * 
 * 
 * 
 * 我们可以配置多个HttpSecurity实例，就像我们可以有多个<http>块.
 * 关键在于对WebSecurityConfigurationAdapter进行多次扩展。
 *  例如下面是 一个对/api/开头的URL进行的不同的设置。
 *  一个对/db-console/开头的URL进行的不同的设置。 
 *  其他URL走表单 方便使用成员变量写在了一个类中
 *  
 * @Resource(name="JwtUserDetailsServiceImpl") 构建war包的时候一定不要用Resource 
 * @Qualifier("JwtUserDetailsServiceImpl")
 * @Autowired private UserDetailsService userDetailsService;
 * @formatter:on
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(UsmAutoConfiguration.class)
@EnableConfigurationProperties({ UsmProperties.class })
@ConditionalOnClass({ WebMvcAutoConfiguration.class })
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
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
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	// ~ Instance Constructor
	// ================================================================================================

	public MultiWebSecurityConfiguration(@Autowired UsmContextVariables usmContextVariables) {
		this.usmProperties = usmContextVariables.getUsmProperties();
		this.antPatternProperties = usmProperties.getAntPattern();
		this.corsProperties = usmProperties.getCors();
		this.serverProperties = usmContextVariables.getServerProperties();
		this.parameterService = usmContextVariables.getParameterService();
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
		String contextPath = serverProperties.getContextPath() == null ? "/" : serverProperties.getContextPath();
		return new UsmInstallFilter(parameterService,antPatternProperties.getIndex(), antPatternProperties.getInstall(), antPatternProperties.getInit(),
				contextPath, usmRequestMatcher);
	}

	@Bean("jwtUserDetailsServiceImpl")
	@DependsOn({ "usmRequestMatcher", "userRepository", "roleRepository" })
	public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		return new JwtUserDetailsServiceImpl(userRepository, roleRepository);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public MobileCodeAuthenticationProcessingFilter mobileCodeAuthenticationProcessingFilter() {
		MobileCodeAuthenticationProcessingFilter filter = new MobileCodeAuthenticationProcessingFilter();
		filter.setAuthenticationManager(authenticationManager);
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
		return new UsmAuthenticationSuccessHandler(serverProperties.getContextPath());
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
	@DependsOn({ "usmRequestMatcher", "jwtUserDetailsServiceImpl", "jwtTokenHandler" })
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(
			@Qualifier("jwtUserDetailsServiceImpl") UserDetailsService userDetailsService,
			UsmRequestMatcher usmRequestMatcher, JwtTokenHandler jwtTokenHandler) {
		return new JwtAuthenticationTokenFilter(userDetailsService, usmRequestMatcher, jwtTokenHandler, corsProperties);
	}

	@Bean("usmRequestMatcher")
	public UsmRequestMatcher usmRequestMatcher() {
		return new UsmRequestMatcher(antPatternProperties.getPermitAllAntPatterns(),
				antPatternProperties.getWebIngoreAntPatterns(), antPatternProperties.getAuthorizeAntPatterns());
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

	// @Autowired
	// private UsmSecurityInterceptorFilter usmSecurityInterceptorFilter;

	@Autowired
	private UsmFilterInvocationSecurityMetadataSource usmFilterInvocationSecurityMetadataSource;
	//
	private Logger logger = LoggerFactory.getLogger(getClass());
	//
	// @Override
	// public void configure(WebSecurity web) throws Exception {
	// web.ignoring().antMatchers(antPatternProperties.getWebIngoreAntPatterns()).antMatchers("/js/**",
	// "/css/**", "/img/**");
	// }

	protected void configure(HttpSecurity httpSecurity) throws Exception {

		String[] authorizedAntPatterns = antPatternProperties.getAuthorizeAntPatterns();
		logger.info("Restful API request mapping pattern:  " + Arrays.toString(authorizedAntPatterns));
		String[] antPatterns = antPatternProperties.getPermitAllAntPatterns();
		httpSecurity.authorizeRequests().antMatchers(antPatterns).permitAll();
		for (String antPattern : antPatterns) {
			httpSecurity.antMatcher(antPattern).cors().and().authorizeRequests().antMatchers(antPattern).permitAll();
		}
		httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				// 自定义FilterInvocationSecurityMetadataSource
				.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
						usmFilterInvocationSecurityMetadataSource
								.setDefaultFilterInvocationSecurityMetadataSource(fsi.getSecurityMetadataSource());
						fsi.setSecurityMetadataSource(usmFilterInvocationSecurityMetadataSource);
						return fsi;
					}
				});
		httpSecurity.authorizeRequests().antMatchers(authorizedAntPatterns).authenticated();
		httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and();
		httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		// httpSecurity.addFilterBefore(usmSecurityInterceptorFilter,
		// FilterSecurityInterceptor.class);
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

	@Configuration
	@Order(Ordered.HIGHEST_PRECEDENCE + 300)
	public class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers(antPatternProperties.getWebIngoreAntPatterns()).antMatchers("/js/**", "/css/**",
					"/img/**");
		}

		@Autowired
		private UsmRequestMatcher usmRequestMatcher;

		@Autowired
		private UsmAuthenticationSuccessHandler successHandler;

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
			// CORS ant-pattern
			// httpSecurity.authorizeRequests().antMatchers(antPatternProperties.getAuthorizeAntPatterns()).permitAll();
			httpSecurity.authorizeRequests().anyRequest().authenticated().and().formLogin()
					.loginPage(antPatternProperties.getLogin()).loginProcessingUrl(antPatternProperties.getSignIn())
					/* .defaultSuccessUrl("/home",true) */.successHandler(successHandler).failureHandler(failureHandler)
					.permitAll().and().headers().frameOptions().sameOrigin()// 表示该页面可以在相同域名页面的frame 中展示
					.and().sessionManagement().invalidSessionUrl(antPatternProperties.getLogin()).maximumSessions(1)
					.expiredUrl(antPatternProperties.getLogin()).sessionRegistry(new SessionRegistryImpl()).and().and()
					.logout().permitAll().logoutSuccessUrl(antPatternProperties.getIndex()).invalidateHttpSession(true)
					.and().rememberMe().key(key).tokenValiditySeconds(7 * 24 * 60 * 60)
					.tokenRepository(tokenRepository);// 指定记住登录信息所使用的数据源
			httpSecurity.addFilterBefore(usmSecurityInterceptorFilter, FilterSecurityInterceptor.class);
			httpSecurity.addFilterBefore(usmInstallFilter, ChannelProcessingFilter.class);
			httpSecurity.addFilterAfter(usmLoginFilter, AnonymousAuthenticationFilter.class);
			if (usmLoginFilter.isVerificationCode()) {
				httpSecurity.addFilterBefore(kaptchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			}
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(antPatternProperties.getWebIngoreAntPatterns()).antMatchers("/js/**", "/css/**",
				"/img/**");
	}

}
