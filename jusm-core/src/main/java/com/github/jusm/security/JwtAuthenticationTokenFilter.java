package com.github.jusm.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;

class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final UrlPathHelper pathHelper = new UrlPathHelper();

	private UserDetailsService userDetailsService;

	private UsmRequestMatcher usmRequestMatcher;

	private JwtTokenHandler jwtTokenHandler;

	private CorsProperties corsProperties;

	public JwtAuthenticationTokenFilter(UserDetailsService userDetailsService, UsmRequestMatcher usmRequestMatcher,
			JwtTokenHandler jwtTokenHandler, CorsProperties corsProperties) {
		this.userDetailsService = userDetailsService;
		this.usmRequestMatcher = usmRequestMatcher;
		this.jwtTokenHandler = jwtTokenHandler;
		this.corsProperties = corsProperties;
	}

	/**
	 * 以api开始的url都需要鉴权
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		String lookupPath = this.pathHelper.getLookupPathForRequest(request);
		/// 可以不用鉴权的请求
		if (usmRequestMatcher.getPermitAllRequestMatcher().matches(request)) {
			logger.debug("访问此路径: " + lookupPath + " 不需要需要JWT鉴权");
			return true;
		} else if (usmRequestMatcher.getAuthorizeRequestMatcher().matches(request)) {
			logger.info("访问此路径: " + lookupPath + " 需要JWT鉴权");
			return false;
		} else {
			logger.debug("访问此路径: " + lookupPath + " 不需要需要JWT鉴权");
			return true;
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(jwtTokenHandler.getTokenHeaderKey());
		if (!HttpMethod.OPTIONS.matches(request.getMethod())) {
			if (authHeader != null && authHeader.startsWith(jwtTokenHandler.getTokenPrefix())) {
				final String authToken = authHeader.substring(jwtTokenHandler.getTokenPrefix().length()); // The part
																											// after
																											// "Bearer "
				String username = jwtTokenHandler.getUsernameFromToken(authToken);
				logger.debug("checking authentication " + username);
				// boolean expiredToken =
				// redisUtil.isMember(RedisKeys.getExpiredTokenKey(username),authToken);服务器时间不一致导致校验失败
				if (username != null /* && !expiredToken */) {
					logger.debug("authenticated user " + username + "............");
					// 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
					// 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
					// 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
					// 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询,但是接口中很多用到的用户的信息不止是token中的用户信息所以增加一次数据查询用户信息
					UserDetails userDetails = null;
					try {
						userDetails = this.userDetailsService.loadUserByUsername(username);
					} catch (UsernameNotFoundException e) {
						logger.warn("Username: " + username + " 不存在或未激活的用户");
						responseOutWithJson(response, R.failure("不存在或未激活的用户"));
					}
					if (jwtTokenHandler.validateToken(authToken, userDetails)) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						logger.debug("Authenticated user " + username + ", setting security context");
						SecurityContextHolder.getContext().setAuthentication(authentication);
						chain.doFilter(request, response);
					} else {
						logger.warn("Token 校验失败");
						logger.warn("Username:" + username);
						logger.warn("AuthToken:" + authToken);
						responseOutWithJson(response, R.result(ReturnCode.TOKEN_ERROR, "请重新登录"));
					}
				} else {
					logger.warn("username 为空或token失效 ");
					logger.warn("Username:" + username);
					logger.warn("authToken:" + authToken);
					responseOutWithJson(response, R.result(ReturnCode.TOKEN_VALID_FAILED, "请登录"));
				}
			} else {
				responseOutWithJson(response, R.result(ReturnCode.AUTH_ERROR,
						"请求头需携带以Authorization为key, 以'" + jwtTokenHandler.getTokenPrefix() + "'为前缀的token参数"));
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	protected void responseOutWithJson(HttpServletResponse response, Object responseObject) {
		// 将实体对象转换为JSON Object转换
		String responseJSONObject = JSONObject.toJSONString(responseObject);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		// 设置允许跨域
		response.setHeader("Access-Control-Allow-Origin", StringUtils.join(corsProperties.getAllowedOrigins(), ","));
		// 允许的访问方法
		response.setHeader("Access-Control-Allow-Methods", StringUtils.join(corsProperties.getAllowedMethods(), ","));
		// Access-Control-Max-Age 用于 CORS 相关配置的缓存
		response.setHeader("Access-Control-Max-Age", String.valueOf(corsProperties.getMaxAge()));
		// response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With,// Content-Type, Accept");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(responseJSONObject);
			logger.debug("返回是\n");
			logger.debug(responseJSONObject);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
