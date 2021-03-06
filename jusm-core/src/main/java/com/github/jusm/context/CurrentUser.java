package com.github.jusm.context;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.github.jusm.security.JwtUser;
import com.github.jusm.security.NullUser;


public class CurrentUser implements AuditorAware<String> {

	private String identification;

	/**
	 * @param identification
	 *            使用哪个字段作为标识
	 */
	public CurrentUser(String identification) {
		this.identification = identification;
	}

	@Override
	public String getCurrentAuditor() {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(identification) && identification.equalsIgnoreCase("username")) {
			return getUsername();
		}
		return getUserId();
	}

	public static JwtUser getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			return NullUser.getInstance(); 
		}
		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof JwtUser && principal.getClass().isAssignableFrom(JwtUser.class)) {
			return (JwtUser) principal;
		}else {
			return NullUser.getInstance(); 
		}
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public static String getUserId() {
		JwtUser user = getUser();
		Assert.notNull(user, "用户应该不为空");
		return user.getId();
	}

	/**
	 * 获取用户名称
	 * 
	 * @return
	 */
	public static String getUsername() {
		JwtUser user = getUser();
		Assert.notNull(user, "用户应该不为空");
		return user.getUsername();
	}
	
	public static String getPhonenumber() {
		JwtUser user = getUser();
		Assert.notNull(user, "用户应该不为空");
		return user.getPhonenumber();
	}

	/**
	 * 获取用户昵称
	 * 
	 * @return
	 */
	public static String getUserNickname() {
		JwtUser user = getUser();
		Assert.notNull(user, "用户应该不为空");
		return user.getNickname();
	}

	/**
	 * 获取用户Email
	 * 
	 * @return
	 */
	public static String getEmail() {
		JwtUser user = getUser();
		Assert.notNull(user, "用户应该不为空");
		return user.getEmail();
	}

	/**
	 * 获取已经赋角色编码集合
	 * 
	 * @return
	 */
	public static Collection<String> getGrantedAuthorities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return Collections.emptySet();
		}
		Collection<String> result = new HashSet<>();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			String authority = grantedAuthority.getAuthority();
			result.add(authority);
		}
		return result;
	}

	/**
	 * 获取已经赋角色集合
	 * 
	 * @return
	 */
	public static Collection<? extends GrantedAuthority> getRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return Collections.emptySet();
		}
		return authentication.getAuthorities();
	}

	/**
	 * 是否有某个角色权限
	 * @param authority
	 * @return
	 */
	public static Boolean hasAuthorities(String authority) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (StringUtils.isEmpty(authority) || authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
		for (GrantedAuthority role : roles) {
			if (authority.equals(role.getAuthority())) {
				return true;
			}
		}
		return false;
	}
}