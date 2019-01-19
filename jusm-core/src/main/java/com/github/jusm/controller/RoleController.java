package com.github.jusm.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.jusm.entity.Role;
import com.github.jusm.exception.ValidException;
import com.github.jusm.model.R;
import com.github.jusm.model.RoleModel;
import com.github.jusm.service.RoleService;

@Controller
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RoleService roleService;

	@GetMapping(value = "role.html")
	public String list(Model model) {
		logger.debug("list roles");
		Collection<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);
		return "role";
	}

	@PostMapping(value = "role")
	public String save(@RequestBody RoleModel model, BindingResult bindingResult) {
		logger.debug("save roles");
		if (bindingResult.hasErrors()) {
			throw  new ValidException(bindingResult);
		}
		String authority = model.getAuthority();
		if (!authority.startsWith("role_")) {
			authority = "role_" + authority;
		}
		roleService.addRole(model.getName(), authority, model.getDescription());
		return "role";
	}
}
