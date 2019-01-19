package com.github.jusm.service;

import java.util.Collection;
import java.util.List;

import com.github.jusm.entity.Role;

public interface RoleService {

	/**
	 * 查询所有角色
	 * @return
	 */
	List<Role> findAll();
	
	Role findByName(String string);

	Role findByAuthority(String authority);

	Role save(Role role);

	Role addRole(String name, String authority, String description);

	List<Role> findByAuthorityIn(Collection<String> authority);
}
