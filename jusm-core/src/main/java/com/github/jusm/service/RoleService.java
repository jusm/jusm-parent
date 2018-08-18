package com.github.jusm.service;

import java.util.List;

import com.github.jusm.entities.Role;

public interface RoleService {

	/**
	 * 查询所有角色
	 * @return
	 */
	List<Role> findAll();
	
	
	Role newAdd(Role role);
}
