package com.github.jusm.service;

import java.util.Map;

import com.github.jusm.entity.User;
import com.github.jusm.model.R;

public interface AuthorizeService {

	Map<String,String> login(String account, String password);

	String refresh(String token);

	R register(User convert);

	boolean exist(String username);
	
	R register(User user, boolean enabled);

	R register(User user, boolean enabled, String roleAuthority);

	User createUser(User user, boolean enabled, String roleAuthority);

	Map<String,String> addAndLoginApiUserIntoUSM(String username, String password);

	Map<String,String> addAndLoginApiUserIntoUSM(String username, String password, String nickname);
	
	Map<String,String> addAndLoginApiUserIntoUSM(String username, String password, String nickname, String phonenumber);
	
	Map<String,String> addAndLoginApiUserIntoUSM(String username, String password, String nickname, String phonenumber, String email);
	
	Map<String,String> addAndLoginApiUserIntoUSM(String username, String password, String nickname, String phonenumber, String email, String role);

	R logout();

	String getUsernameFromToken(String token);
}