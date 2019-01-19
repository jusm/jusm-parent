package com.github.jusm.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.github.jusm.validation.constraints.Matches;

@Matches(field = "password", verifyField = "confirmPassword",  
message = "{constraint.confirmNewPassword.not.match.newPassword}") 
public class RegisterDO {
	
	@NotBlank(message="用户名必填")
	@Size(min=1,max=20,message="{items.name.length.error}")
	private  String username;
	
	@NotBlank(message="密码必填")
	@Size(min=1,max=20,message="{items.name.length.error}")
	private  String password;
	
	@NotBlank(message="确认密码必填")
	@Size(min=1,max=20,message="{items.name.length.error}")
	private  String confirmPassword;
	
	@NotBlank(message="电子邮箱必填")
	@Size(min=1,max=20,message="{items.name.length.error}")
	private String email;
	
	@NotBlank(message="手机号码必填")
	@Size(min=1,max=20,message="{items.name.length.error}")
	private String phonenumber;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	

}
