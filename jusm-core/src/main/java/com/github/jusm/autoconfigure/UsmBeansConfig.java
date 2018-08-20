package com.github.jusm.autoconfigure;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.jusm.controller.AuthController;
import com.github.jusm.controller.BasicConsoleController;
import com.github.jusm.controller.PermissionController;
import com.github.jusm.controller.RoleController;
import com.github.jusm.web.ConsoleController;

@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication
@AutoConfigureBefore(UsmAutoConfiguration.class)
@Configuration
@EntityScan(basePackages="com.github.jusm.entities")
@EnableJpaRepositories(basePackages = {"com.github.jusm.repository"})
public class UsmBeansConfig {

	@ConditionalOnMissingBean(value = AuthController.class)
	public AuthController authController() {
		return new AuthController();
	}
	
	@ConditionalOnMissingBean(value = ConsoleController.class)
	public BasicConsoleController basicConsoleController() {
		return new  BasicConsoleController();
	}
	
	@ConditionalOnMissingBean(value = PermissionController.class)
	public PermissionController permissionController() {
		return new PermissionController();
	}
	
	@ConditionalOnMissingBean(value = RoleController.class)
	public RoleController roleController() {
		return new RoleController();
	}
}
