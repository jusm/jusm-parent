package com.github.jusm.service;

import java.util.Collection;
import java.util.List;

import com.github.jusm.entities.Permission;

/**
 * 1,菜单操作
 * 2,权限操作
 *
 */
public interface PermissionService {

	
	/**
	 * 菜单树查询
	 * @return
	 */
	List<Permission> treeMenus();

	/**
	 * 
	 * @param authority 职权编码
	 * @return
	 */
	List<Permission> treeMenus(String authority);
	
	/**
	 * 
	 * @param authorities 职权编码
	 * @return
	 */
	List<Permission> treeMenus(Collection<String> authorities);
	
	
}
