package com.github.jusm.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.jusm.entity.Permission;
import com.github.jusm.entity.Role;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.service.RoleService;

public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Role findByAuthority(String authority) {
		return roleRepository.findByAuthority(authority);
	}
	
	@Override
	public List<Role> findByAuthorityIn(Collection<String> authority) {
		return roleRepository.findByAuthorityIn(authority);
	}

	@Override
	public Role addRole(String name, String authority, String description) {
		Role customerRole = new Role();
		customerRole.setDescription(description);
		customerRole.setName(name);
		customerRole.setAuthority(authority);
		customerRole.setPriority(50);
		List<Permission> permissions = permissionRepository.findFirstLevelByType(Permission.Type.FUNCTION);
		UserServiceImpl.grantPermissionToRole(permissions, customerRole, Permission.PREFIX_OF_NUMBER + 5);
		roleRepository.save(customerRole);
		return roleRepository.findByAuthority(authority);
	}

}
