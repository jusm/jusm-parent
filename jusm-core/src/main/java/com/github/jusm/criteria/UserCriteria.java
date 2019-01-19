package com.github.jusm.criteria;

import com.github.jusm.support.PaginationSupport;

import io.swagger.annotations.ApiModelProperty;

public class UserCriteria extends PaginationSupport {
	
	@ApiModelProperty(example="role_boss")
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
