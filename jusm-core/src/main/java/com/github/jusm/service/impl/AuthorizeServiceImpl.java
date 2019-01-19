package com.github.jusm.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.jusm.context.CurrentUser;
import com.github.jusm.entity.Group;
import com.github.jusm.entity.Role;
import com.github.jusm.entity.User;
import com.github.jusm.exception.AuthException;
import com.github.jusm.exception.RepeatException;
import com.github.jusm.exception.UsmException;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.security.JwtTokenHandler;
import com.github.jusm.security.JwtUser;
import com.github.jusm.service.AuthorizeService;
import com.github.jusm.service.GroupService;
import com.github.jusm.service.RoleService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.web.WebContextHolder;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private JwtTokenHandler jwtTokenHandler;
	private UserService userService;
	private GroupService groupService;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AuthorizeServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtTokenHandler jwtTokenHandler, UserService userService, RoleService roleService,
			GroupService groupService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenHandler = jwtTokenHandler;
		this.userService = userService;
		this.roleService = roleService;
		this.groupService = groupService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Map<String, String> login(String username, String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		try {
			final Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Object principal = authentication.getPrincipal();
			if (principal != null && principal instanceof JwtUser) {
				JwtUser jwtUser = (JwtUser) principal;
				if (!jwtUser.isEnabled()) {
					throw new UsmException("用户未启动");
				}
				Date lastLoginTime = DateUtils.truncate(new Date(), Calendar.SECOND);
				jwtUser.setLastLoginTime(lastLoginTime);
				String authorization = jwtTokenHandler.getTokenPrefix() + jwtTokenHandler.generateToken(jwtUser);
				userService.updateLastLoginTime(jwtUser.getId(), lastLoginTime);
				WebContextHolder.getResponse().addCookie(new Cookie(Conts.TOKENHEADERKEY, authorization));
				Map<String, String> result = new HashMap<>();
				result.put(Conts.TOKENHEADERKEY, authorization);
				result.put("Role",
						jwtUser.getDefaultGrantedAuthority() != null ? jwtUser.getDefaultGrantedAuthority().getName()
								: "");
				result.put("Authority",
						jwtUser.getDefaultGrantedAuthority() != null
								? jwtUser.getDefaultGrantedAuthority().getAuthority()
								: "");
				result.put("nickname", jwtUser.getNickname());
				result.put("username", jwtUser.getUsername());
				result.put("realname", jwtUser.getRealname());
				return result;
			} else {
				throw new UsmException(ReturnCode.LOGIN_ACCOUNT_ERROR, "请输入正确的账号密码");
			}
		} catch (BadCredentialsException e) {
			logger.debug(e.getMessage());
			throw new UsmException(ReturnCode.LOGIN_ACCOUNT_ERROR, "请输入正确的账号密码");
		} catch (DisabledException e) {
			logger.debug(e.getMessage());
			throw new UsmException(ReturnCode.DISABLED_USER_ERROR, "请联系管理解禁");
		}
	}

	@Override
	public String refresh(String authorization) {
		if (authorization != null && authorization.startsWith(jwtTokenHandler.getTokenPrefix())) {
			final String token = authorization.substring(jwtTokenHandler.getTokenPrefix().length()); // The part after
																										// "Bearer "
			String username = jwtTokenHandler.getUsernameFromToken(token);
			JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
			if (jwtTokenHandler.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
				Date lastLoginTime = DateUtils.truncate(new Date(), Calendar.SECOND);
				userService.updateLastLoginTime(user.getId(), lastLoginTime);
				return jwtTokenHandler.getTokenPrefix() + jwtTokenHandler.refreshToken(token, lastLoginTime);
			} else {
				throw new AuthException("失效的 token");
			}
		}
		throw new AuthException("Token格式不正确");
	}

	@Override
	@Transactional
	public R register(User user) {
		try {
			if (userService.findByUsername(user.getUsername()) != null) {
				throw new RepeatException("用户名称已经存在");
			}
			String password = passwordEncoder.encode(user.getPassword());
			user.setPassword(password);
			user.setEnabled(false);
			Role userRole = roleService.findByAuthority("role_user");
			user.getRoles().add(userRole);
			user.setDefaultRole(userRole);
			userService.newAdd(user);// TODO 返回user 内存溢出以后查找原因
			return R.result(ReturnCode.REGIST_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsmException("注册失败", e);
		}
	}

	@Override
	@Transactional
	public R register(User user, boolean enabled) {
		try {
			if (userService.findByUsername(user.getUsername()) != null) {
				throw new RepeatException("用户名称已经存在");
			}
			String password = passwordEncoder.encode(user.getPassword());
			user.setPassword(password);
			user.setEnabled(enabled);
			Role userRole = roleService.findByAuthority("role_user");
			user.getRoles().add(userRole);
			user.setDefaultRole(userRole);
			Group groupCaller = groupService.findByNumber("group_caller");
			user.getGroups().add(groupCaller);
			userService.newAdd(user);
			return R.result(ReturnCode.OPER_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsmException("注册失败", e);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R register(User user, boolean enabled, String roleAuthority) {
		try {
			if (userService.findByUsername(user.getUsername()) != null) {
				throw new RepeatException("用户名称已经存在");
			}
			String password = passwordEncoder.encode(user.getPassword());
			user.setPassword(password);
			user.setEnabled(enabled);
			Role userRole = roleService.findByAuthority(roleAuthority);
			user.getRoles().add(userRole);
			user.setDefaultRole(userRole);
			userService.newAdd(user);
			return R.result(ReturnCode.OPER_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return R.failure(e);
		}
	}

	@Override
	@Transactional
	public User createUser(User user, boolean enabled, String roleAuthority) {
		if (userService.findByUsername(user.getUsername()) != null) {
			throw new RepeatException("用户名称已经存在");
		}
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setEnabled(enabled);
		Role userRole = roleService.findByAuthority(roleAuthority);
		user.getRoles().add(userRole);
		user.setDefaultRole(userRole);
		return userService.newAdd(user);

	}

	@Override
	public boolean exist(String username) {
		if (userService.findByUsername(username) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, String> addAndLoginApiUserIntoUSM(String username, String password) {
		return addAndLoginApiUserIntoUSM(username, password, null, null, null);
	}

	@Override
	public Map<String, String> addAndLoginApiUserIntoUSM(String username, String password, String nickname) {
		return addAndLoginApiUserIntoUSM(username, password, nickname, null, null);
	}

	@Override
	public Map<String, String> addAndLoginApiUserIntoUSM(String username, String password, String nickname,
			String phonenumber) {
		return addAndLoginApiUserIntoUSM(username, password, nickname, phonenumber, null);
	}

	@Override
	public Map<String, String> addAndLoginApiUserIntoUSM(String username, String password, String nickname,
			String phonenumber, String email) {
		return addAndLoginApiUserIntoUSM(username, password, nickname, phonenumber, email, "role_customer");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> addAndLoginApiUserIntoUSM(String username, String password, String nickname,
			String phonenumber, String email, String role) {
		if (exist(username)) {
			// 登录
			return login(username, password);
		} else {
			User user = User.withUsername(username).build();
			user.setPassword(password);
			user.setNickname(nickname);
			user.setPhonenumber(phonenumber);
			user.setEmail(email);
			// 注册&激活
			register(user, true, role);
			// user.getRoles().add(roleService.findByAuthority("role_caller"));
			Role userRole = roleService.findByAuthority(role);
			user.setDefaultRole(userRole);
			// 登录
			return login(username, password);
		}
	}

	@Override
	public R logout() {
		boolean result = userService.logoutByUsername(CurrentUser.getUsername());
		return R.result(result);
	}

	@Override
	public String getUsernameFromToken(String token) {
		String username = jwtTokenHandler.getUsernameFromToken(token);
		return username;
	}
}
