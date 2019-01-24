package com.github.jusm.controller;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.github.jusm.context.CurrentUser;
import com.github.jusm.domain.InitializerRootDO;
import com.github.jusm.entity.Permission;
import com.github.jusm.redis.RedisRepository;
import com.github.jusm.service.ParameterService;
import com.github.jusm.service.PermissionService;
import com.github.jusm.service.ResourceService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.web.ConsoleController;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
@Controller
public class BasicConsoleController implements ConsoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private UserService userService;

	@Autowired
	private RedisRepository redisRepository;

	@GetMapping(value = { "console" })
	public String console(HttpSession session, Model model) {
		Collection<String> authorities = CurrentUser.getGrantedAuthorities();
		// 初始化菜单数据
		List<Permission> treeMenus = permissionService.treeMenus(authorities);
		session.setAttribute("menus", treeMenus);
		int size = redisRepository.getClientList().size();
		model.addAttribute("redisClient", size);
		GenericObjectPoolConfig jedisPoolConfig = redisRepository.getRedisPoolConfig();
		int maxTotal = jedisPoolConfig.getMaxTotal();
		model.addAttribute("maxTotal", maxTotal);
		BigDecimal num = new BigDecimal(size);
		BigDecimal max = new BigDecimal(maxTotal);
		BigDecimal divide = num.divide(max);
		model.addAttribute("totalRate", divide.multiply(new BigDecimal(100)));

		@SuppressWarnings("restriction")
		OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		@SuppressWarnings("restriction")
		long totalPhysicalMemorySize = mem.getTotalPhysicalMemorySize();
		@SuppressWarnings("restriction")
		long freePhysicalMemorySize = mem.getFreePhysicalMemorySize();
		long l = totalPhysicalMemorySize - freePhysicalMemorySize;
		model.addAttribute("usedPhysicalMemory", l / 1024 / 1024);
		BigDecimal multiply = new BigDecimal(l)
				.divide(new BigDecimal(totalPhysicalMemorySize), 4, BigDecimal.ROUND_HALF_DOWN)
				.multiply(new BigDecimal(100));
		model.addAttribute("usedPhysicalMemoryRate", multiply);

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

	@GetMapping(value = { Conts.default_page })
	public String index(Model model) throws IOException {
		return "index";
	}

	@GetMapping(value = { "/admin" })
	public String defaultIndex(Model model) throws IOException {
		return "redirect:/res/build/index.html";
	}

	@GetMapping(value = { "video" })
	public String video(Model model) throws IOException {
		return "video";
	}

	@Override
	public String getConsolePath() {
		return "console";
	}

	@GetMapping(value = { Conts.DEFAULT_ACCESS_LOCAL + "{token}" })
	public String forward(@PathVariable String token) {
		return resourceService.parseURI(token);
	}
}
