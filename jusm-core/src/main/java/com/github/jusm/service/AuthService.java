package com.github.jusm.service;

import com.github.jusm.entities.User;
import com.github.jusm.model.R;

public interface AuthService {

	R login(String account, String password);

	String refresh(String token);

	R register(User convert);

	boolean exist(String username);

	Object addApiUserIntoUSM(String username, String password);

	R register(User user, boolean enabled);

	R register(User user, boolean enabled, String roleAuthority);

	User createUser(User user, boolean enabled, String roleAuthority);

}