package com.github.jusm.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import com.github.jusm.entity.Permission;
import com.github.jusm.entity.Role;
import com.github.jusm.repository.PermissionRepository;

/**
 * @formatter:off
 * 
 *                <pre>
 *                最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 *                此类在初始化时，应该取到所有资源及其对应角色的定义。 这里涉及到分布式缓存 参照
 *                {@link org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource}
 * 
 *                ************************************************************************************
 *                <h1>spring security实现动态配置url权限的两种方法
 *                <h1/>
 *                <ul>
 * 
 *                标准的RABC, 权限需要支持动态配置，spring
 *                security默认是在代码里约定好权限，真实的业务场景通常需要可以支持动态配置角色访问权限，即在运行时去配置url对应的访问角色。
 * 
 *                基于spring security，如何实现这个需求呢？
 * 
 *                <li>spring security 授权回顾</li>
 * 
 *                <pre>
 *                --------------------------------------------------------------------------------------------------------------------
 *                | Alias | Filter Class | Namespace Element or Attribute |
 *                |CHANNEL_FILTER | ChannelProcessingFilter |
 *                http/intercept-url@requires-channel | |SECURITY_CONTEXT_FILTER
 *                | SecurityContextPersistenceFilter | http |
 *                |SERVLET_API_SUPPORT_FILTER |
 *                SecurityContextHolderAwareRequestFilter |
 *                http/@servlet-api-provision | |CONCURRENT_SESSION_FILTER |
 *                ConcurrentSessionFilter |
 *                session-management/concurrency-control | |HEADERS_FILTER |
 *                HeaderWriterFilter | http/headers | |CSRF_FILTER | CsrfFilter
 *                | http/csrf | |LOGOUT_FILTER | LogoutFilter |
 *                http/@servlet-api-provision | |X509_FILTER |
 *                X509AuthenticationFilter | http/x509 | |PRE_AUTH_FILTER |
 *                AbstractPreAuthenticatedProcessingFilter | N/A | |CAS_FILTER |
 *                CasAuthenticationFilter | N/A | |FORM_LOGIN_FILTER |
 *                UsernamePasswordAuthenticationFilter | http/form-login |
 *                |BASIC_AUTH_FILTER | BasicAuthenticationFilter |
 *                http/http-basic | |SERVLET_API_SUPPORT_FILTER |
 *                SecurityContextHolderAwareRequestFilter |
 *                http/@servlet-api-provision | |JAAS_API_SUPPORT_FILTER |
 *                JaasApiIntegrationFilter | http/@jaas-api-provision |
 *                |REMEMBER_ME_FILTER | RememberMeAuthenticationFilter |
 *                http/remember-me | |ANONYMOUS_FILTER |
 *                AnonymousAuthenticationFilter | http/anonymous |
 *                |SESSION_MANAGEMENT_FILTER | SessionManagementFilter |
 *                session-management | |EXCEPTION_TRANSLATION_FILTER|
 *                ExceptionTranslationFilter | http |
 *                |FILTER_SECURITY_INTERCEPTOR | FilterSecurityInterceptor |
 *                http | |SWITCH_USER_FILTER | SwitchUserFilter | N/A |
 *                ---------------------------------------------------------------------------------------------------------------------
 * @formatter:on <{@link 最重要的是FilterSecurityInterceptor，该过滤器实现了主要的鉴权逻辑，最核心的代码在这里>
 * 
 *               从上面可以看出，要实现动态鉴权，可以从两方面着手：
 *
 *               自定义SecurityMetadataSource，实现从数据库加载ConfigAttribute
 *               另外就是可以自定义accessDecisionManager，官方的UnanimousBased其实足够使用，并且他是基于AccessDecisionVoter来实现权限认证的，
 *               因此我们只需要自定义一个AccessDecisionVoter就可以了
 *               </ul>
 * 
 *               <pre>
 * 
 */
public class UsmFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	public static final String PERMISSION_KEY = "PERMISSION_KEY";

	private static final Logger logger = LoggerFactory.getLogger(UsmFilterInvocationSecurityMetadataSource.class);

	private FilterInvocationSecurityMetadataSource defaultFilterInvocationSecurityMetadataSource;

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	private PermissionRepository permissionRepository;

	private UsmRequestMatcher usmRequestMatcher;

	public UsmFilterInvocationSecurityMetadataSource(PermissionRepository permissionRepository,
			UsmRequestMatcher usmRequestMatcher) {
		this.permissionRepository = permissionRepository;
		this.usmRequestMatcher = usmRequestMatcher;
		synchronizeSecurityMetadataSourcetToCache();
	}
	// 参考代码
	// 这里的需要从DB加载
	// private final Map<String,String> urlRoleMap = new HashMap<String,String>(){{
	// put("/open/**","ROLE_ANONYMOUS");
	// put("/health","ROLE_ANONYMOUS");
	// put("/restart","ROLE_ADMIN");
	// put("/demo","ROLE_USER");
	// }};
	//
	// @Override
	// public Collection<ConfigAttribute> getAttributes(Object object) throws
	// IllegalArgumentException {
	// FilterInvocation fi = (FilterInvocation) object;
	// String url = fi.getRequestUrl();
	//
	// for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
	// if(antPathMatcher.match(entry.getKey(),url)){
	// return SecurityConfig.createList(entry.getValue());
	// }
	// }
	//
	// // 返回代码定义的默认配置
	// return superMetadataSource.getAttributes(object);
	// }
	// http://www.cnblogs.com/xiaoqi/p/spring-security-usage.html#3998033
	////////////////////////////////////////////////////////////////////

	/**
	 * 本实例化调用 查询所有的权限项 然后同步到缓存
	 */
	public void synchronizeSecurityMetadataSourcetToCache() {
		MetadataSourceCacheManager.metadata_source_map.clear();
		List<Permission> list = permissionRepository.findAll();
		MetadataSourceCacheManager.put(list);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : MetadataSourceCacheManager.metadata_source_map
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}
		return allAttributes;
	}

	/**
	 * 根据URL，找到相关的权限配置 如果是静态文件就返回null
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// object 是一个URL，被用户请求的url
		Collection<ConfigAttribute> result = new HashSet<>();
		FilterInvocation filterInvocation = (FilterInvocation) object;
		HttpServletRequest httpRequest = filterInvocation.getHttpRequest();
		if (usmRequestMatcher.getPermitAllRequestMatcher().matches(httpRequest)) {
			logger.debug(httpRequest.getPathInfo());
			return null;
		}
		if (usmRequestMatcher.getWebIgnoreRequestMatcher().matches(httpRequest)) {
			logger.debug(httpRequest.getPathInfo());
			return null;
		}
		Iterator<RequestMatcher> ite = MetadataSourceCacheManager.metadata_source_map.keySet().iterator();
		while (ite.hasNext()) {
			RequestMatcher requestMatcher = ite.next();
			if (requestMatcher.matches(httpRequest)) {
				logger.debug("Request("+httpRequest.getRequestURI()+", "+ httpRequest.getMethod() +") matched!");
				Collection<ConfigAttribute> c = MetadataSourceCacheManager.metadata_source_map.get(requestMatcher);
				for (ConfigAttribute configAttribute : c) {
					result.add(configAttribute);
					logger.debug(configAttribute.getAttribute());
				}
			}
		}
		return result;
	}

	/**
	 * 缓存管理器 所有都是私有的只有通过 {@link UsmFilterInvocationSecurityMetadataSource} 的 remove
	 * 或者 refresh 方法 更新缓存
	 *
	 */
	public static class MetadataSourceCacheManager {

		private static Logger logger = LoggerFactory.getLogger(MetadataSourceCacheManager.class);

		public static final Map<RequestMatcher, Collection<ConfigAttribute>> metadata_source_map = new ConcurrentHashMap<RequestMatcher, Collection<ConfigAttribute>>();

		/**
		 * 更新缓存
		 * 
		 * @param permission
		 * @return
		 */
		public static Map<RequestMatcher, Collection<ConfigAttribute>> put(Collection<Permission> permissions) {
			for (Permission permission : permissions) {
				recursionPermissions(permission);
			}
			return metadata_source_map;
		}

		private static void recursionPermissions(Permission... permissions) {
			recursionPermissions(Arrays.asList(permissions));
		}

		private static void recursionPermissions(List<Permission> permissions) {
			for (Permission permission : permissions) {
				UsmPermissionRequestMatcher permissionMather = UsmPermissionRequestMatcher.Builder
						.buildWithPermission(permission);
				Collection<ConfigAttribute> roleCollection = new HashSet<ConfigAttribute>();
				StringBuilder sb = new StringBuilder(" Authority: ");
				Set<Role> roles = permission.getRoles();
				for (Role role : roles) {
					ConfigAttribute roleNumber = new SecurityConfig(role.getAuthority());
					roleCollection.add(roleNumber);
					sb.append("[").append(role.getAuthority()).append("] ");
				}
				logger.info("Put cache UsmRequestMatcher { permissionName=" + permissionMather.getPermissionName()
						+ sb.toString() + permissionMather.getPatterns() + " } ");
				metadata_source_map.put(permissionMather, roleCollection);
				recursionPermissions(permission.getChildren());
			}
		}
	}

	private static final class UsmPermissionRequestMatcher implements RequestMatcher {

		@Override
		public String toString() {
			return "UsmPermissionRequestMatcher [permissionId=" + permissionId + ", permissionName=" + permissionName
					+ ", requestMatchers=" + requestMatchers + "]";
		}

		private final Logger logger = LoggerFactory.getLogger(getClass());
		private final String permissionId;
		private final String permissionName;
		private final List<AntPathRequestMatcher> requestMatchers;

		public String getPermissionName() {
			return permissionName;
		}

		public String getPermissionId() {
			return permissionId;
		}

		public List<AntPathRequestMatcher> getRequestMatchers() {
			return requestMatchers;
		}

		public String getPatterns() {
			StringBuilder sb = new StringBuilder("patterns: ");
			for (AntPathRequestMatcher requestMatcher : requestMatchers) {
				sb.append("[").append(requestMatcher.getPattern()).append("] ");
			}
			return sb.toString();
		}

		private UsmPermissionRequestMatcher(String permissionId, String permissionName) {
			this.permissionId = permissionId;
			this.permissionName = permissionName;
			this.requestMatchers = java.util.Collections.emptyList();
		}

		/**
		 * Creates a new instance
		 *
		 * @param requestMatchers
		 *            the {@link RequestMatcher} instances to try
		 */
		private UsmPermissionRequestMatcher(String permissionId, String permissionName,
				List<AntPathRequestMatcher> requestMatchers) {
			Assert.notEmpty(requestMatchers, "requestMatchers must contain a value");
			if (requestMatchers.contains(null)) {
				throw new IllegalArgumentException("requestMatchers cannot contain null values");
			}
			this.requestMatchers = requestMatchers;
			this.permissionId = permissionId;
			this.permissionName = permissionName;
		}

		/**
		 * Creates a new instance
		 *
		 * @param requestMatchers
		 *            the {@link RequestMatcher} instances to try
		 */
		private UsmPermissionRequestMatcher(String permissionId, String permissionName,
				AntPathRequestMatcher... requestMatchers) {
			this(permissionId, permissionName, Arrays.asList(requestMatchers));
		}

		public boolean matches(HttpServletRequest request) {
			String requestURI = request.getRequestURI();
			String method = request.getMethod();
			for (RequestMatcher matcher : requestMatchers) {
				logger.debug("Trying to match using " + matcher);
				if (matcher.matches(request)) {
					logger.debug("({},{})matched", requestURI, method);
					return true;
				}
			}
			logger.debug("({},{}) No matches found", requestURI, method);
			return false;
		}

		public static class Builder {
			public static UsmPermissionRequestMatcher buildWithPermission(Permission permission) {
				Assert.notNull(permission, "permission should not null!");
				if (StringUtils.isNotBlank(permission.getUri())) {
					String[] urls = permission.getUri().split(",");
					String httpMethod = permission.getHttpMethod() == null ? null : permission.getHttpMethod().name();
					List<AntPathRequestMatcher> list = new ArrayList<>();
					for (int i = 0; i < urls.length; i++) {
						AntPathRequestMatcher pathRequestMatcher = new AntPathRequestMatcher(urls[i], httpMethod);
						list.add(pathRequestMatcher);
					}
					return new UsmPermissionRequestMatcher(permission.getId(), permission.getName(), list);
				} else {
					return new UsmPermissionRequestMatcher(permission.getId(), permission.getName());
				}
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((permissionId == null) ? 0 : permissionId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UsmPermissionRequestMatcher other = (UsmPermissionRequestMatcher) obj;
			if (permissionId == null) {
				if (other.permissionId != null)
					return false;
			} else if (!permissionId.equals(other.permissionId))
				return false;
			return true;
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
		// return true;
	}

	public FilterInvocationSecurityMetadataSource getDefaultFilterInvocationSecurityMetadataSource() {
		return defaultFilterInvocationSecurityMetadataSource;
	}

	public void setDefaultFilterInvocationSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource defaultFilterInvocationSecurityMetadataSource) {
		this.defaultFilterInvocationSecurityMetadataSource = defaultFilterInvocationSecurityMetadataSource;
	}
}