package com.github.jusm.controller;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.jusm.domain.RegisterDO;
import com.github.jusm.entities.EmailAccount;
import com.github.jusm.entities.MessageInfo;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.security.JwtAuthenticationRequest;
import com.github.jusm.security.JwtAuthenticationResponse;
import com.github.jusm.service.AuthService;
import com.github.jusm.service.UserService;
import com.github.jusm.util.EmailUtil;
import com.github.jusm.validation.AddGroup;
import com.github.jusm.validation.ValidatorUtil;
import com.github.jusm.web.WebContextHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "Auth" }, description = "鉴权管理", position = 0)
@RestController
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class AuthController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${jwt.header:Authorization}")
	private String tokenHeader;

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@ApiOperation(value = "系统登录", notes = "系统登录，将登录成功后的data数据为以后请求的Authorization")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public R createAuthenticationToken(@ApiParam("用户账号密码") @RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {
		return authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
	}

	@ApiOperation(value = "刷新Token")
	@ApiImplicitParam(name = "Authorization", value = "登录成功后返回的以Bearer 开始的Token", required = true, paramType = "header")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET, headers = "Authorization")
	public R refreshAndGetAuthenticationToken() {
		try {
			HttpServletRequest request = WebContextHolder.getRequest();
			String token = request.getHeader(tokenHeader);
			String refreshedToken = authService.refresh(token);
			return R.success(new JwtAuthenticationResponse(refreshedToken));
		} catch (AuthenticationException e) {
			logger.error("刷新token失败", e);
			return R.failure(e);
		}
	}

	@ApiOperation(value = "注册账号")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public R register(@ApiParam("用户注册信息") @RequestBody RegisterDO addedUser) throws AuthenticationException {
		try {
			ValidatorUtil.validateEntity(addedUser, AddGroup.class);
			if (!StringUtils.equals(addedUser.getPassword(), addedUser.getConfirmPassword())) {
				return R.result(ReturnCode.VALID_ERROR, "两次输入的密码不一致");
			}
			if (!EmailUtil.checkEmail(addedUser.getEmail())) {
				return R.result(ReturnCode.VALID_ERROR, "邮箱不正确");
			}
			MessageInfo msg= new MessageInfo();
			msg.setFrom("18565826287@163.com");
			msg.setSubject("邮件主题");
			msg.setTo(Arrays.asList("804918076@qq.com","3429293919@qq.com"));
			msg.setMsg("测试测试邮件");
			msg.setSendDate(new Date());
			EmailAccount emailAccount = new EmailAccount();
			emailAccount.setPassword("wen804918076");
			emailAccount.setUsername("18565826287@163.com");
			emailAccount.setPlace("smtp.163.com");
			boolean sslSend = EmailUtil.sslSend(msg, emailAccount);
			System.out.println(sslSend);
			userService.createUser(addedUser.getUsername(),addedUser.getPassword(),addedUser.getEmail(),addedUser.getPhonenumber());
			return R.success();
		} catch (Exception e) {
			e.printStackTrace();
			return R.failure(e);
		}
	}

}
