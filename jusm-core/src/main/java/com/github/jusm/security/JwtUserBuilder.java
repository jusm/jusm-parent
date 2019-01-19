package com.github.jusm.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.jusm.entity.Role;
import com.github.jusm.entity.User;

public final class JwtUserBuilder {
	public static JwtUser builder(User user) {
		if (user != null) {
			JwtUser jwtUser = new JwtUser();
			jwtUser.setUsername(user.getUsername());
			jwtUser.setPassword(user.getPassword());
			jwtUser.setRealname(user.getRealname());
			jwtUser.setAccountNonExpired(user.isAccountNonExpired());
			jwtUser.setAccountNonLocked(user.isAccountNonLocked());
			jwtUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
			jwtUser.setEnabled(user.isEnabled());
			jwtUser.setId(user.getId());
			jwtUser.setNickname(user.getNickname());
			jwtUser.setPhonenumber(user.getPhonenumber());
			jwtUser.setEmail(user.getEmail());
			jwtUser.setLastLoginTime(user.getLastLoginTime());
			jwtUser.setLastLogoutTime(user.getLastLogoutTime());
			jwtUser.setLastPasswordResetDate(user.getLastPasswordResetDate());
			Role defaultRole = user.getDefaultRole();
			if (defaultRole != null) {
				jwtUser.setDefaultGrantedAuthority(new JwtGrantedAuthority(defaultRole.getName(), defaultRole.getAuthority()));
			}
			Collection<GrantedAuthority> authorities = user.getAuthorities();
			SimpleGrantedAuthority simpleGrantedAuthority = null;
			Collection<GrantedAuthority> simpleGrantedAuthorities = new HashSet<>();

			for (GrantedAuthority grantedAuthority : authorities) {
				String authority = grantedAuthority.getAuthority();
				simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
				simpleGrantedAuthorities.add(simpleGrantedAuthority);
			}
			jwtUser.setAuthorities(simpleGrantedAuthorities);

			return jwtUser;
		}
		return null;

	}
}
