package com.github.jusm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.jusm.entities.Permission;
import com.github.jusm.model.Menu;
import com.github.jusm.service.PermissionService;

@Controller
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;

	@GetMapping(value = "menu.html")
	public String list(HttpSession session) {
		return "menu";
	}
	
	@PostMapping(value = "menu")
	public ModelAndView menu(Menu menu) {
		ModelAndView mav = new ModelAndView();
		System.out.println("123344545");
		mav.setViewName("menu");  
//		mav.setViewName("menu");
		return mav;
	}
	
	@GetMapping(value = "menu-add.html")
	public String menuAdd(HttpSession session) {
		return "menu-add";
	}

	@GetMapping(value = "menulist")
	@ResponseBody
	public List<Menu> treeMenus(HttpSession session) {
		List<Permission> treeMenus = permissionService.treeMenus();
		List<Menu> ret = new ArrayList<>();
		for (Permission menu : treeMenus) {
			Menu vo = new Menu();
			vo.setId(menu.getId());
			vo.setUri(menu.getUri());
			vo.setIcon(menu.getIcon());
			vo.setName(menu.getName());
			vo.setNumber(menu.getNumber());
			vo.setParentId(menu.getParent() == null ? null :menu.getParent().getId());
			vo.setParentName(menu.getParent() == null ? null :menu.getParent().getName());
			vo.setPosition(menu.getPosition());
			vo.setType(menu.getType().name());
			vo.setDescription(menu.getDescription());
			ret.add(vo);
			Collection<Permission> children = menu.getChildren();
			for (Permission menu2 : children) {
				vo = new Menu();
				vo.setId(menu2.getId());
				vo.setUri(menu2.getUri());
				vo.setNumber(menu2.getNumber());
				vo.setIcon(menu2.getIcon());
				vo.setName(menu2.getName());
				vo.setParentId(menu2.getParent() == null ? null :menu2.getParent().getId());
				vo.setParentName(menu2.getParent() == null ? null :menu2.getParent().getName());
				vo.setPosition(menu2.getPosition());
				vo.setType(menu2.getType().name());
				vo.setDescription(menu2.getDescription());
				ret.add(vo);
				children = menu2.getChildren();
				for (Permission menu3 : children) {
					vo = new Menu();
					vo.setId(menu3.getId());
					vo.setUri(menu3.getUri());
					vo.setNumber(menu3.getNumber());
					vo.setIcon(menu3.getIcon());
					vo.setName(menu3.getName());
					vo.setParentId(menu3.getParent() == null ? null :menu3.getParent().getId());
					vo.setParentName(menu3.getParent() == null ? null :menu3.getParent().getName());
					vo.setPosition(menu3.getPosition());
					vo.setType(menu3.getType().name());
					vo.setDescription(menu3.getDescription());
					ret.add(vo);
				}
			}
		}
		return ret;
	}

}
