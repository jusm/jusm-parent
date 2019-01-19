package com.github.jusm.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.github.jusm.validation.constraints.Matches;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "用户信息")
@Matches(field = "password", verifyField = "confirmPassword", message = "{constraint.confirmNewPassword.not.match.newPassword}")
public class UserModel {

	@NotBlank(message = "用户名必填")
	@Size(min = 1, max = 20, message = "{items.name.length.error}")
	@ApiModelProperty(example = "wen")
	private String username;

	@NotBlank(message = "真实姓名必填")
	@ApiModelProperty(example = "张胜男")
	private String realname;

	@NotBlank(message = "手机号必填")
	@ApiModelProperty(example = "18565826256")
	private String phonenumber;

	@NotBlank(message = "密码必填")
	@ApiModelProperty(example = "123456")
	private String password;

	@NotBlank(message = "确认密码必填")
	@ApiModelProperty(example = "123456")
	private String confirmPassword;

	@NotBlank(message = "电子邮箱必填")
	@Size(min = 1, max = 20, message = "{items.name.length.error}")
	@ApiModelProperty(example = "804918076@qq.com")
	private String email;
	
	@ApiModelProperty(example="role_manager")
	private String role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
