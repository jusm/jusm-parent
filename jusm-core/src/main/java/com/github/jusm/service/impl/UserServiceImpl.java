package com.github.jusm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.jusm.cache.UsmCacheAdapter;
import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.entities.Group;
import com.github.jusm.entities.Parameter;
import com.github.jusm.entities.Permission;
import com.github.jusm.entities.Role;
import com.github.jusm.entities.User;
import com.github.jusm.exception.NoExistException;
import com.github.jusm.exception.UsmException;
import com.github.jusm.exception.ValidException;
import com.github.jusm.repository.GroupRepository;
import com.github.jusm.repository.ParameterRepository;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;
import com.github.jusm.security.UsmContext;
import com.github.jusm.service.UserService;
import com.github.jusm.util.ResourcesUtil;

@Service
@CacheConfig(cacheNames = "sys.user")
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired(required=false)
	private UsmCacheAdapter usmCacheAdapter;
	
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ParameterRepository parameterRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User getOne(String userId) {
		return userRepository.getOne(userId);
	}

	@Override
	public User findOne(String userId) {
		return userRepository.findOne(userId);
	}

	@Override
	public Page<User> findByPageable(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public Collection<User> findAll() {
		return Collections.unmodifiableCollection(userRepository.findAll());
	}

	@Override
	public User newAdd(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void deleteByUsername(String[] usernames) {
		for (String username : usernames) {
			User user = userRepository.findByUsername(username);
			if (user == null) {
				throw new NoExistException("该用户名称不存在");
			}
			userRepository.delete(user.getId());
		}
	}

	@Override
	@Caching(evict = { @CacheEvict(cacheNames = "sys.user", key = "#userName"),
			@CacheEvict(cacheNames = "sys.user", key = "#result") })
	public String deleteByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		userRepository.delete(user.getId());
		return user.getId();
	}

	@Override
	@Caching(put = { @CachePut(cacheNames = "sys.user", key = "#result.username"),
			@CachePut(cacheNames = "sys.user", key = "#result.id") })
	public User disableByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		user.setEnabled(false);
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(cacheNames = "sys.user", key = "#result.username"),
			@CachePut(cacheNames = "sys.user", key = "#result.id") })
	public User disableByUserId(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NoExistException("该用户不存在");
		}
		user.setEnabled(false);
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(cacheNames = "sys.user", key = "#result.username"),
			@CachePut(cacheNames = "sys.user", key = "#result.id") })
	public User enabledByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		user.setEnabled(true);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	@Caching(evict = { @CacheEvict(cacheNames = UsmCacheNames.PARAMTER, allEntries = true) })
	public boolean createRootUser(String password, String email, String phonenumber) {
		//falshDB
		if (parameterRepository.findByParamKey(Parameter.SYS_CREATE_DATE_KEY) == null) {
			String string = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
			parameterRepository.save(Parameter.SYS_CREATE_DATE.setParamValue(string));
			List<Permission> initPermission = initPermission();
			Map<String, Group> initGroup = initGroup();
			Map<String, Role> initRole = initRole(initPermission, initGroup);
			User initUser = initUser(initGroup, initRole, password,email,phonenumber);
			return userRepository.exists(initUser.getId());
		} else {
			throw new UsmException("系统已经初始化");
		}
	}

  

	/**
	 * 建立权限许可
	 */
	private List<Permission> initPermission() {
		List<Permission> permissions = Collections.emptyList();
		String config = readConfig("permission-config.json");
		permissions = JSON.parseArray(config, Permission.class);
		save(permissions, null);
		return permissions;
	}

	private void save(List<Permission> permissions, Permission parent) {
		for (Permission permission : permissions) {
			permission.setParent(parent);
		}
		if (permissions != null && !permissions.isEmpty()) {
			List<Permission> p = permissionRepository.save(permissions);
			for (Permission permission : p) {
				save(permission.getChildren(), permission);
			}
		}
	}

	/**
	 * 建立分组
	 */
	private Map<String, Group> initGroup() {
		Map<String, Group> groups = new HashMap<String, Group>();
		String config = readConfig("group-config.json");
		List<Group> list = JSON.parseArray(config, Group.class);
		save(list, null, groups);
		return groups;
	}

	private void save(List<Group> groups, Group parent, Map<String, Group> groupsMap) {
		for (Group group : groups) {
			group.setParent(parent);
		}
		if (groups != null && !groups.isEmpty()) {
			List<Group> p = groupRepository.save(groups);
			for (Group group : p) {
				groupsMap.put(group.getNumber(), group);
				save(group.getChildren(), group, groupsMap);
			}
		}
	}


	public void grantPermissionToRoleByPermissionNumber(List<Permission> permissions, Role role,
			String permissionNumber) {
		for (Permission permission : permissions) {
			if (permission.getNumber().startsWith(permissionNumber)) {
				role.getPermissions().add(permission);
			}
			grantPermissionToRole(permission.getChildren(), role, permissionNumber);
		}
	}

	public void grantPermissionToRole(List<Permission> permissions, Role role, String prefix) {
		for (Permission permission : permissions) {
			if (permission.getNumber().startsWith(prefix)) {
				role.getPermissions().add(permission);
			}
			grantPermissionToRole(permission.getChildren(), role, prefix);
		}
	}

	/**
	 * 建立角色
	 * 
	 * @param initGroup
	 * @param initPermission
	 */
	private Map<String, Role> initRole(List<Permission> permissions, Map<String, Group> groups) {
		Map<String, Role> roles = new HashMap<>();
		Role adminRole = new Role();
		adminRole.setDescription("系统管理员");
		adminRole.setName("系统管理员");
		adminRole.setAuthority("role_admin");
		adminRole.setPriority(1);
		grantPermissionToRole(permissions, adminRole, Permission.PREFIX_OF_NUMBER + 1);
		adminRole.getGroups().add(groups.get("group_admin"));
		roleRepository.save(adminRole);
		roles.put(adminRole.getAuthority(), adminRole);

		Role dbaRole = new Role();
		dbaRole.setDescription("数据库管理员");
		dbaRole.setName("数据库管理员");
		dbaRole.setAuthority("role_dba");
		dbaRole.setPriority(2);
		grantPermissionToRole(permissions, dbaRole, Permission.PREFIX_OF_NUMBER + 3);
		dbaRole.getGroups().add(groups.get("group_dba"));
		roleRepository.save(dbaRole);
		roles.put(dbaRole.getAuthority(), dbaRole);

		Role userRole = new Role();
		userRole.setDescription("用户");
		userRole.setName("用户");
		userRole.setAuthority("role_user");
		userRole.setPriority(9);
		grantPermissionToRole(permissions, userRole, Permission.PREFIX_OF_NUMBER + 2);
		userRole.getGroups().add(groups.get("group_user"));
		roleRepository.save(userRole);
		roles.put(userRole.getAuthority(), userRole);

		Role actuatorRole = new Role();
		actuatorRole.setDescription("执行者 springboot admin使用");
		actuatorRole.setName("执行者");
		actuatorRole.setAuthority("role_actuator");
		actuatorRole.getGroups().add(groups.get("group_admin"));
		actuatorRole.setPriority(19);
		grantPermissionToRole(permissions, actuatorRole, Permission.PREFIX_OF_NUMBER);
		roleRepository.save(actuatorRole);
		roles.put(actuatorRole.getAuthority(), actuatorRole);

		Role callerRole = new Role();
		callerRole.setDescription("接口来访者角色");
		callerRole.setName("接口来访者角色");
		callerRole.setAuthority("role_caller");
		callerRole.setPriority(29);
		grantPermissionToRole(permissions, callerRole, Permission.PREFIX_OF_NUMBER + 2);
		callerRole.getGroups().add(groups.get("group_caller"));
		roleRepository.save(callerRole);
		roles.put(callerRole.getAuthority(), callerRole);
		return roles;
	}

	/**
	 * 建立用户
	 * 
	 * @param rawPass
	 */
	private User initUser(Map<String, Group> groups, Map<String, Role> roles, String rawPass,String email,String phonenumber) {
		User root = User.withUsername(UsmContext.RootHolder.getUsername()).build();
		String password = passwordEncoder.encode(rawPass);
		root.setPassword(password);
		root.setDefaultRole(roles.get("role_admin"));
		root.getRoles().add(roles.get("role_dba"));
		root.getRoles().add(roles.get("role_caller"));
		root.getRoles().add(roles.get("role_actuator"));
		root.getRoles().add(roles.get("role_user"));
		root.getGroups().add(groups.get("group_dba"));
		root.getGroups().add(groups.get("group_admin"));
		root.getGroups().add(groups.get("group_user"));
		root.setEmail(email);
		root.setPhonenumber(phonenumber);
		root = userRepository.save(root);
		return root;
	}

	@Override
	@Caching(evict = { @CacheEvict(cacheNames = "sys.param", allEntries = true) })
	@Transactional
	public void clean(String rawPassword) {
		User root = userRepository.findByUsername(UsmContext.RootHolder.getUsername());
		if (passwordEncoder.matches(rawPassword, root.getPassword())) {
			userRepository.deleteAll();
			roleRepository.deleteAll();
			groupRepository.deleteAll();
			permissionRepository.deleteAllInBatch();
			parameterRepository.deleteAll();
		} else {
			throw new ValidException("密码不正确");
		}
	}

	@Override
	@Transactional
	public boolean cleanAndInit(String oldPwd, String newPwd, String email, String phonenumber) {
		clean(oldPwd);
		return createRootUser(newPwd, email, phonenumber);
	}

	@Override
	@CacheEvict(cacheNames = "sys.user", key = "#username")
	public void grantAuthorityByUsername(String username, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getRoles().add(role);
		userRepository.save(user);
	}

	@Override
	@CacheEvict(cacheNames = "sys.user", key = "#username")
	public User grantDefaultAuthorityByUsername(String username, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.setDefaultRole(role);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void groupByUsername(String username, String number) {
		Group group = groupRepository.findByNumber(number);
		if (group == null) {
			throw new NoExistException("不存在的分组");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getGroups().add(group);
		userRepository.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "sys.user", key = "#username")
	public void revokeRoleByUsername(String username, String number) {
		Role role = roleRepository.findByAuthority(number);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getRoles().remove(role);
		userRepository.save(user);
	}

	@Override
	@CacheEvict(cacheNames = "sys.user", key = "#username")
	@Transactional
	public void removeGroupByUsername(String username, String number) {
		Group group = groupRepository.findByNumber(number);
		if (group == null) {
			throw new NoExistException("不存在的分组");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getGroups().remove(group);
		userRepository.save(user);
	}

	@Override
	@Transactional
	@Caching(put = { @CachePut(cacheNames = "sys.user", key = "#result.username"),
			@CachePut(cacheNames = "sys.user", key = "#result.id") })
	public User updateLastLoginTime(String id, Date date) {
		User user = userRepository.getOne(id);
		Assert.notNull(user, "not null");
		user.setLastLoginTime(date);
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(cacheNames = "sys.user", key = "#result.username"),
			@CachePut(cacheNames = "sys.user", key = "#result.id") })
	public User changePwd(String username, String oldPwd, String newPwd) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			if (user.isEnabled()) {
				if (passwordEncoder.matches(oldPwd, user.getPassword())) {
					// final String password = passwordEncoder.encode(oldPwd);
					// if (password.equals(user.getPassword())) {
					user.setPassword(passwordEncoder.encode(newPwd));
					user.setLastPasswordResetDate(new Date());
					user = userRepository.save(user);
				} else {
					throw new ValidException("用户原密码不正确");
				}
			} else {
				throw new NoExistException("用户已禁用");
			}
		} else {
			throw new NoExistException("用户不存在");
		}
		return user;
	}

	private String readConfig(final String filename) {
		return ResourcesUtil.getFileContent(UsmContext.getConfigPath() + filename);
	}

	@Override
	public Page<User> findByPageable(User user, Pageable pageable) {
		Specification<User> querySpecifi = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotBlank(user.getUsername())) {
					// 模糊查找
					predicates.add(cb.or(cb.like(root.get("email").as(String.class), "%" + user.getEmail() + "%"),
							cb.like(root.get("username").as(String.class), "%" + user.getUsername() + "%")));
				}
				// and到一起的话所有条件就是且关系，or就是或关系
				if (predicates.size() > 0) {
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return cb.conjunction();
			}
		};
		return userRepository.findAll(querySpecifi, pageable);
	}

	@Override
	public Page<User> findAll(Example<User> example, Pageable pageRequest) {
		return userRepository.findAll(example, pageRequest);
	}

	@Override
	public User findByPhonenumber(String phonenumber) {
		return userRepository.findByPhonenumber(phonenumber);
	}

	@Override
	public void createUser(String username, String password, String email, String phonenumber) {
		User user = User.withUsername(username).password(password).build();
		user.setEmail(email);
		user.setPhonenumber(phonenumber);
		User save = userRepository.save(user);
	}

}