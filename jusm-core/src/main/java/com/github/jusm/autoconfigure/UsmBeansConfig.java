package com.github.jusm.autoconfigure;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.jusm.controller.AuthController;
import com.github.jusm.controller.BasicConsoleController;
import com.github.jusm.controller.PermissionController;
import com.github.jusm.controller.RoleController;
import com.github.jusm.security.JwtTokenHandler;
import com.github.jusm.service.AuthService;
import com.github.jusm.service.GroupService;
import com.github.jusm.service.LogoutService;
import com.github.jusm.service.ParameterService;
import com.github.jusm.service.PermissionService;
import com.github.jusm.service.RoleService;
import com.github.jusm.service.UserService;
import com.github.jusm.service.impl.AuthServiceImpl;
import com.github.jusm.service.impl.GroupServiceImpl;
import com.github.jusm.service.impl.LogoutServiceImpl;
import com.github.jusm.service.impl.ParameterServiceImpl;
import com.github.jusm.service.impl.PermissionServiceImpl;
import com.github.jusm.service.impl.RoleServiceImpl;
import com.github.jusm.service.impl.UserServiceImpl;
import com.github.jusm.web.ConsoleController;

@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@Configuration
@EntityScan(basePackages = "com.github.jusm.entities")
@EnableJpaRepositories(basePackages = { "com.github.jusm.repository" })
public class UsmBeansConfig {
	
	@Bean
	@ConditionalOnMissingBean
	public ParameterService parameterService(){
		return new ParameterServiceImpl();
	}
	@Bean
	@ConditionalOnMissingBean(value = LogoutService.class)
	public LogoutService logoutService(){
		return new LogoutServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = PermissionService.class)
	public PermissionService permissionService(){
		return new PermissionServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = RoleService.class)
	public RoleService roleService(){
		return new RoleServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = GroupService.class)
	public GroupService groupService(){
		return new GroupServiceImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = AuthService.class)
	public AuthService authService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtTokenHandler jwtTokenHandler, UserService userService, RoleService roleService,GroupService groupService,PasswordEncoder passwordEncoder){
		return new AuthServiceImpl(authenticationManager, userDetailsService,
				jwtTokenHandler,  userService, roleService, groupService, passwordEncoder);
	}

	@Bean
	@ConditionalOnMissingBean(value = UserService.class)
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	@ConditionalOnMissingBean(value = AuthController.class)
	public AuthController authController() {
		return new AuthController();
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
	@ConditionalOnMissingBean(value = RoleController.class)
	public RoleController roleController() {
		return new RoleController();
	}
}
