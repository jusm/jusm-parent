package com.github.jusm.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="收回角色")
public class RevokeRoleCriteria {
	
	@ApiModelProperty(name="用户名")
	private String username;

	@ApiModelProperty(name="要收回的角色")
	private String roleCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}
