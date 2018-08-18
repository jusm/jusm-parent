package com.github.jusm.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.jusm.entities.Role;
import com.github.jusm.service.RoleService;

@Controller
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RoleService roleService;
	
	@GetMapping(value="role.html")
	public String list(Model model) {
		logger.debug("list roles");
		Collection<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);
		return "role";
	}
}
