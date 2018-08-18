package com.github.jusm.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.jusm.entities.User;

public interface UserService {

	boolean createRootUser(String password, String email, String phonenumber);

	User findByUsername(String username);

	/**
	 * @param userId
	 *            用户主键
	 * @return 当我查询一个不存在的id数据时，直接抛出异常，因为它返回的是一个引用，简单点说就是一个代理对象
	 * 建议使用{@code findOne}
	 */
	User getOne(String userId);

	/**
	 * @param userId
	 * @return 当我查询一个不存在的id数据时，返回的值是null.
	 */
	User findOne(String userId);

	/**
	 * 分页查询所有的用户
	 * @param pageable
	 * @return
	 */
	Page<User> findByPageable(Pageable pageable);

	
	/**
	 * 查询所有的用户
	 * @return
	 */
	Collection<User> findAll();

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	User newAdd(User user);

	
	/**
	 * 按条件分页查询用户
	 * @param example
	 * @param pageRequest
	 * @return
	 */
	Page<User> findAll(Example<User> example, Pageable pageRequest);

	/**
	 * 根据名称删除用户
	 * @param usernames
	 */
	void deleteByUsername(String[] usernames);

	/**
	 * 根据用户名启用用户
	 * @param username
	 * @return
	 */
	User enabledByUsername(String username);

	/**
	 * 根据用户名禁用用户
	 * @param userId
	 * @return
	 */
	User disableByUserId(String userId);


	/**
	 * 清除用户
	 * @param rawPassword
	 */
	void clean(String rawPassword);

	/**
	 * 根据用户名查询该用户的角色权限
	 * @param username 用户名
	 * @param authority 角色编码
	 */
	void grantAuthorityByUsername(String username, String authority);

	/**
	 * 给用户赋值默认角色
	 * @param username 用户名
	 * @param authority 角色编码
	 * @return
	 */
	User grantDefaultAuthorityByUsername(String username, String authority);

	/**
	 * 用户分组
	 * @param username 用户名
	 * @param number 组编码
	 */
	void groupByUsername(String username, String number);

	/**
	 * 根据用户名收回角色(权限)
	 * @param username 用户名
	 * @param number 角色编码
	 */
	void revokeRoleByUsername(String username, String number);

	/**
	 * 移除分组
	 * @param username 用户名
	 * @param number 组编码
	 */
	void removeGroupByUsername(String username, String number);

	/**
	 * 更新用户最后登录时间
	 * @param id 用户主键
	 * @param date 最后登录时间
	 * @return
	 */
	User updateLastLoginTime(String id, Date date);

	/**
	 * 根据用户名修改密码
	 * @param username 用户名
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @return
	 */
	User changePwd(String username, String oldPwd, String newPwd);

	
	/**
	 * 条件分页查询用户 
	 * @param user
	 * @param pageable
	 * @return
	 */
	Page<User> findByPageable(User user, Pageable pageable);

	/**
	 * 根据用户名删除用户
	 * @param username
	 * @return
	 */
	String deleteByUsername(String username);

	
	/**
	 * 根据用户名称禁用用户
	 * @param username 用户名
	 * @return
	 */
	User disableByUsername(String username);
	
	/**
	 * 清除用户重新初始化
	 * @param oldPwd
	 * @param newPwd
	 * @param email
	 * @param phonenumber
	 * @return
	 */
	boolean cleanAndInit(String oldPwd, String newPwd, String email, String phonenumber);

	
	/**
	 * @param phonenumber
	 * @return
	 */
	User findByPhonenumber(String phonenumber);

}
