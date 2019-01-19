package com.github.jusm.model;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "角色", value = "Role")
public class RoleUpdateModel {
	
	@NotBlank(message="角色id必填")
	private String id;

	@ApiModelProperty(example = "厂库管理员")
	@NotBlank(message="名称必填")
	private String name;

	@ApiModelProperty(example = "role_")
	@NotBlank(message="编码必填")
	private String authority;

	@ApiModelProperty(example = "厂库管理员负责厂库模块")
	@NotBlank(message="描述必填")
	private String description;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
