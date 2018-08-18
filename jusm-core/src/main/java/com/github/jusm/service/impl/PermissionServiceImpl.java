package com.github.jusm.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.jusm.entities.Permission;
import com.github.jusm.entities.Role;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.service.PermissionService;

@Service
@CacheConfig(cacheNames = "usm.permission")
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Cacheable
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	@Cacheable(cacheNames="usm.permission",unless="#result == null")
	public List<Permission> treeMenus() {
		return permissionRepository.findFirstLevelByType(Permission.Type.MENU);
	}

	/**
	 * 只支持两级菜单
	 */
	@Override
	@Cacheable(cacheNames="usm.permission",unless="#result == null")
	public List<Permission> treeMenus(String authority) {
		return treeMenus(Arrays.asList(authority));
	}
 
	@Override
	@Cacheable(cacheNames="usm.permission", unless="#result.size() == 0")
	public List<Permission> treeMenus(Collection<String> authorities) {
		List<Permission> permissions = permissionRepository.findFirstLevelByType(Permission.Type.MENU);
		recursionProcessing(authorities, permissions);
		return permissions;
	}

	private void recursionProcessing(Collection<String> authorities, List<Permission> permissions) {
		for (Iterator<Permission> iterator = permissions.iterator(); iterator.hasNext();) {
			Permission permission = (Permission) iterator.next();
			Set<Role> roles = permission.getRoles();
			if (hasAuthority(roles, authorities)) {
				List<Permission> children = permission.getChildren();
				recursionProcessing(authorities,children);
			} else {
				iterator.remove();
			}
		}
	}
	
	private boolean hasAuthority(Collection<Role> expectedRoles,Collection<String> authorities){
		if(expectedRoles == null || authorities ==null || expectedRoles.isEmpty() || authorities.isEmpty()) {
			return false;
		}else {
			Collection<String> 	expectedAuthoritys = extractAuthorities(expectedRoles);
			expectedAuthoritys.retainAll(authorities);
			return !expectedAuthoritys.isEmpty();
		}
	}
	
	private Collection<String> extractAuthorities(Collection<Role> roles){
		if(roles == null || roles.isEmpty()) {
			return  Collections.emptySet();
		}
		Set<String> authoritys = new HashSet<>();
		for (Role role : roles) {
			authoritys.add(role.getAuthority());
		}
		return authoritys;
	}
}
