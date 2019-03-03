package com.github.jusm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.component.InitializationHook;
import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.context.UsmContext;
import com.github.jusm.entity.Group;
import com.github.jusm.entity.Parameter;
import com.github.jusm.entity.Permission;
import com.github.jusm.entity.Role;
import com.github.jusm.entity.User;
import com.github.jusm.exception.NoExistException;
import com.github.jusm.exception.RepeatException;
import com.github.jusm.exception.UsmException;
import com.github.jusm.exception.ValidException;
import com.github.jusm.redis.RedisKeys;
import com.github.jusm.redis.RedisRepository;
import com.github.jusm.repository.GroupRepository;
import com.github.jusm.repository.ParameterRepository;
import com.github.jusm.repository.PermissionRepository;
import com.github.jusm.repository.RoleRepository;
import com.github.jusm.repository.UserRepository;
import com.github.jusm.security.JwtUser;
import com.github.jusm.security.JwtUserBuilder;
import com.github.jusm.service.UserService;
import com.github.jusm.util.ProtoStuffUtil;
import com.github.jusm.util.ResourcesUtil;

@CacheConfig(cacheNames = UsmCacheNames.USER)
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 更新用户信息使用
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public User modify(String id, String nickname, String phonenumber, String realname) {
		User user = userRepository.findOne(id);
		if (user == null) {
			throw new NoExistException("该用户不存在");
		}
		user.setNickname(nickname);
		user.setPhonenumber(phonenumber);
		user.setRealname(realname);
		return userRepository.save(user);
	}

	@Override
	public JwtUser loadUserByUsername(String username) {

		String key = RedisKeys.USM_MODULE_SYS_USERINFO + username;
		byte[] data = redisRepository.get(key.getBytes());
		if (data != null) {
			JwtUser user = ProtoStuffUtil.deserializate(data, JwtUser.class);
			return user;
		} else {
			User user = userRepository.findByUsername(username);
			if (user != null) {
				JwtUser userInfo = JwtUserBuilder.builder(user);
				byte[] serializate = ProtoStuffUtil.serializate(userInfo, JwtUser.class);
				redisRepository.set(key.getBytes(), serializate);
				return userInfo;
			}
			return null;
		}
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public User findOne(String userId) {
		User user = userRepository.findOne(userId);
		return user;
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
	@Transactional(rollbackFor = Exception.class)
	public void deleteByUsername(String[] usernames) {
		for (String username : usernames) {
			User user = userRepository.findByUsername(username);
			if (user == null) {
				throw new NoExistException("该用户名称不存在");
			}
			userRepository.delete(user.getId());
			delCache(username);
		}
	}

	@Caching(evict = { @CacheEvict(key = "#username"), @CacheEvict(key = "#result") })
	public void delCache(String username) {
		String key = RedisKeys.USM_MODULE_SYS_USERINFO + username;
		redisRepository.delete(key.getBytes());
	}

	@Override
	@Caching(evict = { @CacheEvict(key = "#username"), @CacheEvict(key = "#result") })
	public String deleteByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		userRepository.delete(user.getId());
		delCache(username);
		return user.getId();
	}

	@Override
	public boolean logoutByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			user.setLastLogoutTime(new Date());
			userRepository.save(user);
			delCache(username);
			return true;
		}
		return false;
	}

	@Override
	@Caching(evict = { @CacheEvict(key = "#id"), @CacheEvict(key = "#result") })
	public String cacheEvictById(String id) {
		String username = userRepository.findOne(id).getUsername();
		delCache(username);
		return username;
	}

	@Override
	@Caching(put = { @CachePut(key = "#result.username"), @CachePut(key = "#result.id") })
	public User disableByUsername(String username) {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		user.setEnabled(false);
		delCache(username);
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(key = "#result.username"), @CachePut(key = "#result.id") })
	public User disableByUserId(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NoExistException("该用户不存在");
		}
		user.setEnabled(false);
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(key = "#result.username"), @CachePut(key = "#result.id") })
	public User enabledByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("该用户名称不存在");
		}
		user.setEnabled(true);
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@Caching(evict = { @CacheEvict(allEntries = true) })
	@Transactional(rollbackFor = Exception.class)
	public boolean createRootUser(String password, String email, String phonenumber) {
		// falshDB
		boolean initialize = false;
		if (parameterRepository.findByParamKey(Parameter.SYS_CREATE_DATE_KEY) == null) {
			redisRepository.flushDB();
			String string = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
			parameterRepository.save(Parameter.SYS_CREATE_DATE.setParamValue(string));
			List<Permission> permissions = permissionRepository.findFirstLevelByType(Permission.Type.MENU);
			Map<String, Group> initGroup = initGroup();
			Map<String, Role> initRole = initRole(permissions, initGroup);
			User initUser = initUser(initGroup, initRole, password, email, phonenumber);
			Map<String, InitializationHook> beans = SpringContextHolder.getBeansOfType(InitializationHook.class);
			for (InitializationHook bean : beans.values()) {
				initialize = bean.initialize();
				if (!initialize) {
					throw new UsmException(bean.description() + "失败");
				}
			}
			return userRepository.exists(initUser.getId());
		} else {
			throw new UsmException("系统已经初始化");
		}
	}

	public static List<Permission> readPermissionConfig() {
		List<Permission> permissions = Collections.emptyList();
		String config = readConfig("permission-config.json");
		permissions = JSON.parseArray(config, Permission.class);
		return permissions;
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

	public static void grantPermissionToRoleByPermissionNumber(List<Permission> permissions, Role role,
			String permissionNumber) {
		for (Permission permission : permissions) {
			if (permission.getNumber().startsWith(permissionNumber)) {
				role.getPermissions().add(permission);
			}
			grantPermissionToRole(permission.getChildren(), role, permissionNumber);
		}
	}

	public static void grantPermissionToRole(List<Permission> permissions, Role role, String prefix) {
		for (Permission permission : permissions) {
			if (permission.getNumber().startsWith(prefix)) {
				role.getPermissions().add(permission);
			}
			grantPermissionToRole(permission.getChildren(), role, prefix);
		}
	}

	/**
	 * 建立角色
	 * <ol>
	 * <li>超级管理员</li>
	 * <li>系统管理员</li>
	 * <li>接口用户者</li>
	 * <li>数据管理员</li>
	 * <li>超级管理员</li>
	 * </ol>
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
		grantPermissionToRole(permissions, adminRole, Permission.PREFIX_OF_NUMBER);
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
	private User initUser(Map<String, Group> groups, Map<String, Role> roles, String rawPass, String email,
			String phonenumber) {
		User root = User.withUsername(UsmContext.getRootUsername()).build();
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
		root.setNickname("超级管理员");
		root.setPhonenumber(phonenumber);
		root = userRepository.save(root);
		return root;
	}

	@Override
	@Caching(evict = { @CacheEvict(cacheNames = UsmCacheNames.PARAMTER, allEntries = true) })
	@Transactional
	public void clean(String rawPassword) {
		User root = userRepository.findByUsername(UsmContext.getRootUsername());
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
	@CacheEvict(key = "#username")
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
		delCache(user.getUsername());
		userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
	public void grantAuthorityByUserId(String userId, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(userId);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getRoles().add(role);
		delCache(user.getUsername());
		userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
	public User grantDefaultAuthorityByUsername(String username, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		Role defaultRole = user.getDefaultRole();
		if (defaultRole != null) {
			user.getRoles().add(defaultRole);
			// user.getRoles().add(roleRepository.findByAuthority("role_caller"));
		}
		user.setDefaultRole(role);
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
	public User revokeDefaultAuthorityByUsername(String username, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		Role defaultRole = user.getDefaultRole();
		Set<Role> roles = user.getRoles();
		if (defaultRole != null) {
			roles.remove(defaultRole);
		}
		user.setRoles(roles);
		Role newDefault = null;
		for (Role derole : roles) {

			if (newDefault == null || derole.getPriority() > newDefault.getPriority()) {
				newDefault = derole;
			}
		}
		if (newDefault != null) {
			user.setDefaultRole(role);
		}
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
	public User revokeDefaultAuthorityByUserId(String userId, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		Role defaultRole = user.getDefaultRole();
		Set<Role> roles = user.getRoles();
		if (defaultRole != null) {
			roles.remove(defaultRole);
		}
		user.setRoles(roles);
		Role newDefault = null;
		for (Role derole : roles) {

			if (newDefault == null || derole.getPriority() > newDefault.getPriority()) {
				newDefault = derole;
			}
		}
		if (newDefault != null) {
			user.setDefaultRole(role);
		}
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
	public User grantDefaultAuthorityByUserId(String userId, String authority) {
		Role role = roleRepository.findByAuthority(authority);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		Role defaultRole = user.getDefaultRole();
		if (defaultRole != null) {
			user.getRoles().add(defaultRole);
		}
		user.setDefaultRole(role);
		delCache(user.getUsername());
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
	@CacheEvict(key = "#username")
	public void revokeRoleByUserId(String userId, String number) {
		Role role = roleRepository.findByAuthority(number);
		if (role == null) {
			throw new NoExistException("不存在的角色");
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			throw new NoExistException("不存在的用户");
		}
		user.getRoles().remove(role);
		delCache(user.getUsername());
		userRepository.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#username")
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
		delCache(user.getUsername());
		userRepository.save(user);
	}

	@Override
	@CacheEvict(key = "#username")
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
		delCache(user.getUsername());
		userRepository.save(user);
	}

	@Override
	@Transactional
	@Caching(evict = { @CacheEvict(key = "#result.username"), @CacheEvict(key = "#result.id") })
	public User updateLastLoginTime(String id, Date date) {
		User user = userRepository.findOne(id);
		Assert.notNull(user, "not null");
		user.setLastLoginTime(date);
		delCache(user.getUsername());
		return userRepository.save(user);
	}

	@Override
	@Caching(put = { @CachePut(key = "#result.username"), @CachePut(key = "#result.id") })
	public User changePwd(String username, String oldPwd, String newPwd) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			if (user.isEnabled()) {
				if (passwordEncoder.matches(oldPwd, user.getPassword())) {
					user.setPassword(passwordEncoder.encode(newPwd));
					user.setLastPasswordResetDate(new Date());
					user = userRepository.save(user);
					delCache(user.getUsername());
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

	private static String readConfig(final String filename) {
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
	public boolean createUser(String username, String password, String email, String phonenumber) {
		return createUser(username, password, email, phonenumber, null);
	}

	@Override
	public List<JwtUser> findEmployee() {
		Collection<User> list = userRepository.findAll();
		List<JwtUser> result = new ArrayList<>();
		if (list != null) {
			SimpleGrantedAuthority jwt = new SimpleGrantedAuthority("role_employee");
			for (User user : list) {
				JwtUser builder = JwtUserBuilder.builder(user);
				if (builder.getAuthorities().contains(jwt)) {
					result.add(builder);
				}
			}
		}
		return result;
	}

	@Override
	public List<JwtUser> findCustomer() {
		Collection<User> list = userRepository.findAll();
		List<JwtUser> result = new ArrayList<>();
		if (list != null) {
			SimpleGrantedAuthority jwt = new SimpleGrantedAuthority("role_customer");
			for (User user : list) {
				JwtUser builder = JwtUserBuilder.builder(user);
				if (builder.getAuthorities().contains(jwt)) {
					result.add(builder);
				}
			}
		}
		return result;
	}

	@Override
	public List<User> findByIds(String... id) {
		if (id == null) {
			return Collections.emptyList();
		}
		List<String> ids = Arrays.asList(id);
		return userRepository.findAll(ids);
	}

	
	
	@Override
	public Page<JwtUser> searchByDefaultRoleNumber(Collection<Role> authority, Pageable pageable) {
		
		Specification<User> querySpecifi = new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();

				if (authority != null && authority.size() > 0) {
					// 模糊查找
					In<Role> in = cb.in(root.get("defaultRole").as(Role.class));
					for (Role e : authority) {
						in.value(e);
					}
					predicates.add(in);
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		Page<User> page = userRepository.findAll(querySpecifi, pageable);
		List<User> content = page.getContent();
		List<JwtUser> jwtUsers = new ArrayList<>();
		for (User elemet : content) {
			JwtUser builder = JwtUserBuilder.builder(elemet);
			jwtUsers.add(builder);
		}
		return new PageImpl<JwtUser>(jwtUsers, pageable, page.getTotalElements());
	}
	
	@Override
	public Page<JwtUser> findByDefaultRoleNumber(String authority, Pageable pageable) {
		Role defaultRole = roleRepository.findByAuthority(authority);
		ExampleMatcher matcher = ExampleMatcher.matching() // 构建对象
				.withMatcher("defaultRole.authority", GenericPropertyMatchers.contains()).withIgnoreNullValues()
				.withIgnorePaths("id", "lastPasswordResetDate", "accountNonExpired", "accountNonLocked",
						"credentialsNonExpired", "enabled", "defaultRole.id", "defaultRole.name", "defaultRole.enabled",
						"defaultRole.lastmodifiedBy", "defaultRole.lastmodifiedTime", "defaultRole.priority",
						"defaultRole.createTime", "defaultRole.description", "defaultRole.createBy");
		User user = new User();
		user.setDefaultRole(defaultRole);
		Example<User> example = Example.of(user, matcher);
		Page<User> page = userRepository.findAll(example, pageable);
		List<User> content = page.getContent();
		List<JwtUser> jwtUsers = new ArrayList<>();
		for (User elemet : content) {
			JwtUser builder = JwtUserBuilder.builder(elemet);
			jwtUsers.add(builder);
		}
		return new PageImpl<JwtUser>(jwtUsers, pageable, page.getTotalElements());
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public boolean createUser(String username, String password, String email, String phonenumber, String realname) {
		if (userRepository.existsByUsername(username)) {
			throw new RepeatException("账号：" + username + "已经使用");
		}
		User user = User.withUsername(username).password(passwordEncoder.encode(password)).realname(realname).build();
		user.setEmail(email);
		user.setPhonenumber(phonenumber);
		User save = userRepository.save(user);
		grantDefaultAuthorityByUsername(username, "role_user");
		logger.debug("create finished {}", save);
		return createUser(username, password, email, phonenumber, realname, "role_user");
	}

	@Override
	public boolean createUser(String username, String password, String email, String phonenumber, String realname,
			String authority) {
		if (userRepository.existsByUsername(username)) {
			throw new RepeatException("账号：" + username + "已经使用");
		}
		User user = User.withUsername(username).password(passwordEncoder.encode(password)).realname(realname).build();
		user.setEmail(email);
		user.setPhonenumber(phonenumber);
		user.setRealname(realname);
		User save = userRepository.save(user);
		grantDefaultAuthorityByUsername(username, authority);
		logger.debug("create finished {}", save);
		return save.isEnabled();
	}

	@Override
	public boolean updateUser(String id, String password, String email, String phonenumber, String realname,
			String authority) {
		if (!userRepository.exists(id)) {
			throw new NoExistException("不存在的用户");
		}
		User user = userRepository.findOne(id);
		if (StringUtils.isNotBlank(password)) {
			user.setPassword(passwordEncoder.encode(password));
			user.setLastPasswordResetDate(new Date());
		}
		user.setEmail(email);
		user.setPhonenumber(phonenumber);
		user.setRealname(realname);
		User save = userRepository.save(user);
		String username = user.getUsername();
		grantDefaultAuthorityByUsername(username, authority);
		logger.debug("create finished {}", save);
		delCache(username);
		return save.isEnabled();
	}

}