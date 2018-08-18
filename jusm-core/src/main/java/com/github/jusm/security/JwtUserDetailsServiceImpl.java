package com.github.jusm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.jusm.entities.Group;
import com.github.jusm.entities.Role;
import com.github.jusm.entities.User;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;


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
	 * @param username
	 *            帐号
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */

	@Override
	@Transactional
	@Cacheable(cacheNames="usm.user",unless="#result == null")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("此用户名不存在!");
		}
//		JwtUser userDetails = new JwtUser(user.getId(), username, user.getPassword(),
//				AbstractEntity.Status.ENABLED.equals(user.getStatus()), true, true, true,
//				grantedAuthorities(user.getId()), user.getEmail(),user.getLastPasswordResetDate(),user.getLastLoginTime());
		return user;
	}

	@Cacheable(cacheNames="usm.user",unless="#result == null")
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
//			authorities.add(new SimpleGrantedAuthority(entity.getNumber()));
			authorities.add(entity);
		}));
		return authorities;
	}

}
