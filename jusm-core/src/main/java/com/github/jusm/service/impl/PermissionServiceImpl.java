package com.github.jusm.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.context.UsmContext;
import com.github.jusm.entity.Permission;
import com.github.jusm.entity.Role;
import com.github.jusm.repository.ParameterRepository;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.security.UsmFilterInvocationSecurityMetadataSource.MetadataSourceCacheManager;
import com.github.jusm.service.PermissionService;
import com.github.jusm.util.ResourcesUtil;

@Service
@CacheConfig(cacheNames = UsmCacheNames.PERMISSION)
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private ParameterRepository parameterRepository;

	@Autowired
	@Qualifier("fastJsonRedisTemplate")
	private RedisTemplate<String, Object> fastJsonRedisTemplate;

	@Cacheable
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	// @Cacheable(unless="#result == null")
	public List<Permission> treeMenus() {
		return permissionRepository.findFirstLevelByType(Permission.Type.MENU);
	}

	@PostConstruct
	private void initPermission() {
		if (permissionRepository.count() == 0) {
			List<Permission> permissions = readPermissionConfig();
			save(permissions, null);
			MetadataSourceCacheManager.metadata_source_map.clear();
			List<Permission> list = permissionRepository.findAll();
			MetadataSourceCacheManager.put(list);
		}
	}

	public static List<Permission> readPermissionConfig() {
		List<Permission> permissions = Collections.emptyList();
		String config = readConfig("permission-config.json");
		permissions = JSON.parseArray(config, Permission.class);
		return permissions;
	}

	private static String readConfig(final String filename) {
		return ResourcesUtil.getFileContent(UsmContext.getConfigPath() + filename);
	}

	private void save(List<Permission> permissions, Permission parent) {
		for (Permission permission : permissions) {
			permission.setParent(parent);
		}
		if (permissions != null && !permissions.isEmpty()) {
			List<Permission> p = permissionRepository.save(permissions);
			for (Permission permission : p) {
				save(permission.getChildren(), permission);
			}
		}
	}

	/**
	 * 只支持两级菜单
	 */
	@Override
	@Cacheable(key = "#root.methodName+':'+#authority", unless = "#result == null or #result.size() == 0")
	public List<Permission> treeMenus(String authority) {
		return treeMenus(Arrays.asList(authority));
	}

	public static void main(String[] args) {
		Set<String> list = new HashSet<>(Arrays.asList("role_dba", "role_user", "role_admin", "role_actor"));
		List<String> collect = list.stream().sorted().collect(Collectors.toList());
		String message = String.join("|", collect);
		System.out.println(message);
	}

	@Override
	@Cacheable(key = "#root.methodName+':'+#authorities", unless = "#result == null or #result.size() == 0")
	public List<Permission> treeMenus(Collection<String> authorities) {
		if (authorities == null || authorities.isEmpty())
			return Collections.emptyList();
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
				recursionProcessing(authorities, children);
			} else {
				iterator.remove();
			}
		}
	}

	private boolean hasAuthority(Collection<Role> expectedRoles, Collection<String> authorities) {
		if (expectedRoles == null || authorities == null || expectedRoles.isEmpty() || authorities.isEmpty()) {
			return false;
		} else {
			Collection<String> expectedAuthoritys = extractAuthorities(expectedRoles);
			expectedAuthoritys.retainAll(authorities);
			return !expectedAuthoritys.isEmpty();
		}
	}

	private Collection<String> extractAuthorities(Collection<Role> roles) {
		if (roles == null || roles.isEmpty()) {
			return Collections.emptySet();
		}
		Set<String> authoritys = new HashSet<>();
		for (Role role : roles) {
			authoritys.add(role.getAuthority());
		}
		return authoritys;
	}
}
