package com.github.jusm.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.annotations.ApiModel;

/**
 * 角色 基于角色的权限控制
 */
@Table(name = "usm_role")
@Entity
@ApiModel(description="系统用户角色 也是 Authority")
public class Role extends UuidEntity implements GrantedAuthority {

	private static final long serialVersionUID = 1426130233601528955L;

	/** 角色名 */
	@Column(unique = true)
	private String name;

	/**
	 * 职权编码
	 */
	@Column(unique = true)
	private String authority;

	/**
	 * 当用户为多个角色的时候 默认给一个优选级比较高的角色 数字越小优先级越高
	 */
	private Integer priority;

	private boolean enabled;

	/** 描述 */
	private String description;

		 

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usm_role_permission", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "permission_id", referencedColumnName = "id") })
	private Set<Permission> permissions = new HashSet<Permission>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usm_role_group", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", referencedColumnName = "id") })
	private Set<Group> groups = new HashSet<Group>();

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Role setName(String name) {
		this.name = name;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public Role setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getName() {
		return name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}
}
