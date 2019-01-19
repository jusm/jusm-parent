package com.github.jusm.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.jusm.criteria.ChpwdCriteria;
import com.github.jusm.criteria.ForgetPwdCriteria;
import com.github.jusm.criteria.GrantRoleCriteria;
import com.github.jusm.criteria.RevokeRoleCriteria;
import com.github.jusm.domain.RegisterDO;
import com.github.jusm.entity.Role;
import com.github.jusm.entity.User;
import com.github.jusm.exception.BizException;
import com.github.jusm.exception.NoExistException;
import com.github.jusm.exception.ValidException;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.model.RoleModel;
import com.github.jusm.model.UserModel;
import com.github.jusm.model.UserUpdateModel;
import com.github.jusm.security.JwtAuthenticationRequest;
import com.github.jusm.security.JwtAuthenticationResponse;
import com.github.jusm.security.JwtUser;
import com.github.jusm.service.AuthorizeService;
import com.github.jusm.service.MailService;
import com.github.jusm.service.RoleService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.Conts;
import com.github.jusm.util.EmailUtil;
import com.github.jusm.validation.AddGroup;
import com.github.jusm.validation.ValidatorUtil;
import com.github.jusm.web.WebContextHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Authorization", description = "注册与登录", position = 0)
@RestController
@RequestMapping(value = Conts.DEFAULT_REST_API_PATH, produces = "application/json;charset=UTF-8")
public class AuthorizeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String tokenHeader = Conts.TOKENHEADERKEY;

	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "系统登录", tags = Conts.Tags.RZ, notes = "系统登录，将登录成功后的data数据为以后请求的Authorization")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public R login(@ApiParam("用户账号密码") @RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		return R.success(
				authorizeService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
	}

	@ApiOperation(value = "授予默认角色", tags = Conts.Tags.RZ, notes = "给用户默认角色", position = 8)
	@RequestMapping(value = "grant", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R grantDefaultRole(@RequestBody GrantRoleCriteria criteria) throws AuthenticationException {
		if (criteria != null && StringUtils.isNotBlank(criteria.getRoleCode())) {
			String id = criteria.getId();
			if (StringUtils.isNotBlank(id)) {
				userService.grantDefaultAuthorityByUserId(id, criteria.getRoleCode());
			} else {
				userService.grantDefaultAuthorityByUsername(criteria.getUsername(), criteria.getRoleCode());
			}
		}
		return R.success();
	}

	@ApiOperation(value = "创建角色", tags = Conts.Tags.RZ, notes = "创建角色", position = 8)
	@RequestMapping(value = "role", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R createRole(@Valid @RequestBody RoleModel model, BindingResult bindingResult)
			throws AuthenticationException {
		logger.debug("create roles");
		if (bindingResult.hasErrors()) {
			throw new ValidException(bindingResult);
		}
		String authority = model.getAuthority();
		if (!authority.startsWith("role_")) {
			authority = "role_" + authority;
		}
		roleService.addRole(model.getName(), authority, model.getDescription());
		return R.success();
	}

	@ApiOperation(value = "修改角色", tags = Conts.Tags.RZ, notes = "创建角色", position = 8)
	@RequestMapping(value = "role", method = RequestMethod.PUT, headers = Conts.TOKENHEADERKEY)
	public R updateRole(@Valid @RequestBody RoleModel model, BindingResult bindingResult)
			throws AuthenticationException {
		logger.debug("create roles");
		if (bindingResult.hasErrors()) {
			throw new ValidException(bindingResult);
		}
		String authority = model.getAuthority();
		if (!authority.startsWith("role_")) {
			authority = "role_" + authority;
		}
		roleService.addRole(model.getName(), authority, model.getDescription());
		return R.success();
	}

	@ApiOperation(value = "查询角色", tags = Conts.Tags.RZ, notes = "所有角色", position = 8)
	@RequestMapping(value = "role", method = RequestMethod.GET, headers = Conts.TOKENHEADERKEY)
	public R listRole() {
		logger.debug("list roles");
		List<Role> findAll = roleService.findAll();
		return R.success(findAll);
	}

	@ApiOperation(value = "查询用户", tags = Conts.Tags.RZ, notes = "所有用户", position = 8)
	@RequestMapping(value = "user/page-list", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R listUser(@Valid @RequestBody com.github.jusm.criteria.UserCriteria userCriteria, BindingResult result) {
		logger.debug("list users");
		if (result.hasErrors()) {
			return R.failure(result);
		}
		Role role = roleService.findByAuthority(userCriteria.getRole());
		Page<JwtUser> findAll = userService.searchByDefaultRoleNumber(Arrays.asList(role),
				userCriteria.getPageRequest());
		return R.success(findAll);
	}

	@ApiOperation(value = "收回角色", tags = Conts.Tags.RZ, notes = "收回用户角色", position = 9)
	@RequestMapping(value = "revoke", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R revokeRole(@RequestBody RevokeRoleCriteria criteria) throws AuthenticationException {
		if (criteria != null && StringUtils.isNotBlank(criteria.getRoleCode())) {
			String username = criteria.getUsername();
			if (StringUtils.isNotBlank(username)) {
				userService.revokeDefaultAuthorityByUsername(username, criteria.getRoleCode());
			}
		}
		return R.success();
	}

	@ApiOperation(value = "系统退出", tags = Conts.Tags.RZ, notes = "系统退出后，Authorization无效 必须再次登录", position = 10)
	@RequestMapping(value = "logout", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R logout() throws AuthenticationException {
		return authorizeService.logout();
	}

	@ApiOperation(value = "解析"
			+ Conts.TOKENHEADERKEY, tags = Conts.Tags.RZ, notes = "系统退出后，Authorization无效 必须再次登录", position = 10)
	@RequestMapping(value = "parse-token", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R parseToken(@ApiParam(Conts.TOKENHEADERKEY) String token) throws AuthenticationException {
		return R.success(authorizeService.getUsernameFromToken(token));
	}

	@ApiOperation(value = "修改密码", tags = Conts.Tags.RZ, notes = "修改密码", position = 10)
	@RequestMapping(value = "chpwd", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R chpwd(@Valid @RequestBody ChpwdCriteria criteria, BindingResult result) {
		if (result.hasErrors()) {
			return R.failure(result);
		}
		String username = criteria.getUsername();
		String oldPwd = criteria.getOldPwd();
		String newPwd = criteria.getNewPwd();
		userService.changePwd(username, oldPwd, newPwd);
		return R.success();
	}

	@ApiOperation(value = "忘记密码", tags = Conts.Tags.RZ, notes = "确保邮箱可以使用", position = 10)
	@RequestMapping(value = "forget/permit-all", method = RequestMethod.POST)
	public R forget(@Valid @RequestBody ForgetPwdCriteria criteria, BindingResult result)
			throws AuthenticationException {
		if (result.hasErrors()) {
			return R.failure(result);
		}
		User user = userService.findByUsername(criteria.getUsername());
		if (user == null) {
			throw new NoExistException("不存在的记录");
		}
		if (StringUtils.isBlank(user.getEmail())) {
			throw new BizException(ReturnCode.FAILTURE, "没有电子邮箱不能自主找回");
		}
		return R.failure();
	}

	@ApiOperation(tags = Conts.Tags.RZ, value = "刷新Token", position = 2)
	@ApiImplicitParam(name = "Authorization", value = "登录成功后返回的以Bearer 开始的Token", required = true, paramType = "header")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, headers = Conts.TOKENHEADERKEY)
	public R refreshAndGetAuthenticationToken() {
		try {
			HttpServletRequest request = WebContextHolder.getRequest();
			String token = request.getHeader(tokenHeader);
			String refreshedToken = authorizeService.refresh(token);
			return R.success(new JwtAuthenticationResponse(refreshedToken));
		} catch (AuthenticationException e) {
			logger.error("刷新token失败", e);
			return R.failure(e);
		}
	}

	@ApiOperation(tags = Conts.Tags.RZ, value = "注册账号", position = 3)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public R register(@ApiParam("用户注册信息") @RequestBody RegisterDO addedUser) throws AuthenticationException {
		try {
			ValidatorUtil.validateEntity(addedUser, AddGroup.class);
			if (!StringUtils.equals(addedUser.getPassword(), addedUser.getConfirmPassword())) {
				return R.result(ReturnCode.VALID_ERROR, "两次输入的密码不一致");
			}
			String email = addedUser.getEmail();
			if (!EmailUtil.checkEmail(email)) {
				return R.result(ReturnCode.VALID_ERROR, "邮箱不正确");
			}
			String username = addedUser.getUsername();
			boolean enabled = userService.createUser(username, addedUser.getPassword(), email,
					addedUser.getPhonenumber());
			User findByUsername = userService.findByUsername(username);
			String subject = enabled ? "注册成功" : "邮箱激活";
			// mailService.sendSimpleMailMessage(email, subject, String.format("Hello %s !
			// 欢迎使用USM", username));
			Map<String, Object> params = new HashMap<>();
			params.put("username", username);
			params.put("id", findByUsername.getId());
			mailService.sendTemplateMailMessage(email, subject, params);
			return R.success(subject);
		} catch (Exception e) {
			e.printStackTrace();
			return R.failure(e);
		}
	}

	@ApiOperation(tags = Conts.Tags.RZ, value = "创建账号", position = 4)
	@RequestMapping(value = "user", method = RequestMethod.POST, headers = Conts.TOKENHEADERKEY)
	public R createUser(@Valid @ApiParam("用户注册信息") @RequestBody UserModel model, BindingResult bindingResult)
			throws AuthenticationException {
		try {
			if (bindingResult.hasErrors()) {
				throw new ValidException(bindingResult);
			}
			ValidatorUtil.validateEntity(model, AddGroup.class);
			String password = model.getPassword();
			if (!StringUtils.equals(password, model.getConfirmPassword())) {
				return R.result(ReturnCode.VALID_ERROR, "两次输入的密码不一致");
			}
			String email = model.getEmail();
			if (!EmailUtil.checkEmail(email)) {
				return R.result(ReturnCode.VALID_ERROR, "邮箱不正确");
			}
			String username = model.getUsername();
			String realname = model.getRealname();
			boolean enabled = userService.createUser(username, password, email, model.getPhonenumber(), realname,
					model.getRole());
			String subject = enabled ? "注册成功" : "邮箱激活";
			mailService.sendSimpleMailMessage(email, subject,
					String.format("Hello %s !+ 欢迎使用USM \n 请使用账号：%s 密码：%s 登录", realname, username, password));
			return R.success(subject);
		} catch (Exception e) {
			e.printStackTrace();
			return R.failure(e);
		}
	}

	@ApiOperation(tags = Conts.Tags.RZ, value = "更新账号", position = 4)
	@RequestMapping(value = "user", method = RequestMethod.PUT, headers = Conts.TOKENHEADERKEY)
	public R updateUser(@Valid @ApiParam("用户注册信息") @RequestBody UserUpdateModel model, BindingResult bindingResult)
			throws AuthenticationException {
		try {

			if (bindingResult.hasErrors()) {
				throw new ValidException(bindingResult);
			}
			User user = userService.findOne(model.getId());
			if (user == null) {
				throw new NoExistException("不存在的用户");
			}
			String password = model.getPassword();
			if (!StringUtils.equals(password, model.getConfirmPassword())) {
				return R.result(ReturnCode.VALID_ERROR, "两次输入的密码不一致");
			}
			String email = model.getEmail();
			if (!EmailUtil.checkEmail(email)) {
				return R.result(ReturnCode.VALID_ERROR, "邮箱不正确");
			}
			String username = model.getUsername();
			user.setUsername(username);
			String realname = model.getRealname();
			user.setRealname(realname);
			userService.updateUser(model.getId(), password, email, model.getPhonenumber(), realname, model.getRole());
			return R.success();
		} catch (Exception e) {
			e.printStackTrace();
			return R.failure(e);
		}
	}

}
