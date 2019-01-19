package com.github.jusm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import com.github.jusm.entity.Group;
import com.github.jusm.entity.Role;
import com.github.jusm.entity.User;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;

/**
 * 此类禁止使用缓存!!!!!!!!!!!!!!!!!!!!!!
 * 
 * @author haoran.wen
 */
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	/**
	 * 此类是service层直接调用dao
	 */
	protected UserRepository userRepository;

	protected RoleRepository roleRepository;

	public JwtUserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * 提供一种从用户名可以查到用户并返回的方法
	 * 
	 * @param username
	 *            帐号
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("此用户名不存在!");
		}
		JwtUser userDetails = JwtUserBuilder.builder(user);
		return userDetails;
	}

	protected Collection<Role> grantedAuthorities(String userId) {
		final User user = userRepository.findOne(userId);
		List<Role> allRoles = roleRepository.findAll();
		Set<Role> roles = new HashSet<Role>(user.getRoles());
		Set<Group> groups = user.getGroups();
		for (Group group : groups) {
			for (Role role : allRoles) {
				if (role.getGroups().contains(group)) {
					roles.add(role);
				}
			}
		}
		if (CollectionUtils.isEmpty(roles)) {
			return new ArrayList<>();
		}
		Collection<Role> authorities = new HashSet<>();
		roles.stream().filter(role -> role.isEnabled()).forEach((entity -> {
			// authorities.add(new SimpleGrantedAuthority(entity.getNumber()));
			authorities.add(entity);
		}));
		return authorities;
	}

}
