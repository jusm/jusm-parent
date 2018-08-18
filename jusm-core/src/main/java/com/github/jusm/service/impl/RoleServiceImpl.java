package com.github.jusm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.jusm.entities.Role;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	@Cacheable(cacheNames="usm.role",unless="#result == null")
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

}
