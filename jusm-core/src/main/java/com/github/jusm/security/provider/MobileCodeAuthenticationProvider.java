package com.github.jusm.security.provider;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.jusm.entities.User;
import com.github.jusm.security.service.MobileCodeService;
import com.github.jusm.security.token.MobileCodeAuthenticationToken;
import com.github.jusm.service.UserService;

public class MobileCodeAuthenticationProvider implements AuthenticationProvider {
	
	private MobileCodeService mobileCodeService;
	
	private UserService userService;

	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String mobile = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		String code = (String) authentication.getCredentials();
		if (StringUtils.isBlank(code)) {
			throw new BadCredentialsException("验证码不能为空");
		}
		User user = userService.findByPhonenumber(mobile);
		if (null == user) {
			throw new BadCredentialsException("用户不存在");
		}
		// 手机号验证码业务还没有开发，先用4个0验证
		if (mobileCodeService.verify(mobile, code)) {
			throw new BadCredentialsException("验证码不正确");
		}
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), code,
				listUserGrantedAuthorities(user.getId()));
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		System.out.println(this.getClass().getName() + "---supports");
		return (MobileCodeAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private Set<GrantedAuthority> listUserGrantedAuthorities(String uid) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (StringUtils.isEmpty(uid)) {
			return authorities;
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}
