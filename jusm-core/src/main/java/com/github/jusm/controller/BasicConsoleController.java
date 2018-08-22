package com.github.jusm.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.github.jusm.domain.InitializerRootDO;
import com.github.jusm.entities.Permission;
import com.github.jusm.security.CurrentUser;
import com.github.jusm.service.ParameterService;
import com.github.jusm.service.PermissionService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.web.ConsoleController;

@Controller
public class BasicConsoleController implements ConsoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = { "console" })
	public String console(HttpSession session) {
		Collection<String> authorities = CurrentUser.getGrantedAuthorities();
		// 初始化菜单数据
		List<Permission> treeMenus = permissionService.treeMenus(authorities);
		session.setAttribute("menus", treeMenus);
		return getConsolePath();
	}

	/**
	 * @return
	 */
	@RequestMapping(value = Conts.initialize, method = RequestMethod.POST)
	public String initializeUsm(@Valid InitializerRootDO initializerRootDO, BindingResult result,
			RedirectAttributesModelMap model) {
		if (result.hasErrors()) {
			List<FieldError> fieldErrors = result.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				model.addFlashAttribute(fieldError.getField(), fieldError.getDefaultMessage());
				model.addFlashAttribute(result.getTarget());
			}
			return "redirect:install";
		}
		if (parameterService.isSetup()) {
			return "redirect:login";
		}

		logger.info("init system root");
		userService.createRootUser(initializerRootDO.getPassword(), initializerRootDO.getEmail(),
				initializerRootDO.getPhonenumber());
		return "redirect:login";

	}

	@RequestMapping(value = { Conts.login }, method = { RequestMethod.GET })
	public String login() {
		return "login";
	}

	@RequestMapping(value = { Conts.install }, method = { RequestMethod.GET })
	public String install() {
		return "install";
	}

	@RequestMapping(value = { Conts.register }, method = { RequestMethod.GET })
	public String register() {
		return "register";
	}

	@RequestMapping(value = { Conts.default_page, "/" })
	public String index() {
		return "index";
	}

	@Override
	public String getConsolePath() {
		// nothing to do
		return "console";
	}
}
