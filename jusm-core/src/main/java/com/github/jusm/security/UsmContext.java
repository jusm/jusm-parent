package com.github.jusm.security;

import java.util.Map;

import com.github.jusm.autoconfigure.UsmProperties;
import com.github.jusm.component.SpringContextHolder;
import com.github.jusm.entities.Parameter;
import com.github.jusm.service.ParameterService;

/**
 * 
 * 系统所有的参数 配置 都从这个入口拿 其他的类都没有权限了
 */
public class UsmContext {
	
	
	public static class RootHolder{
		/**
		 * 超级用户名称
		 * @return
		 */
		public static String getUsername() {
			UsmContextVariables usmContextFacade = SpringContextHolder.getBean(UsmContextVariables.class);
			UsmProperties usmProperties = usmContextFacade.getUsmProperties();
			return usmProperties.getRoot();
		}
		
		/**
		 * 超级用户名称
		 * @return
		 */
		public static String getEmail() {
			UsmContextVariables usmContextFacade = SpringContextHolder.getBean(UsmContextVariables.class);
			UsmProperties usmProperties = usmContextFacade.getUsmProperties();
			return usmProperties.getEmail();
		}
	}



	/**
	 * 配置文件路径
	 * 
	 * @return
	 */
	public static String getConfigPath() {
		UsmContextVariables usmContextFacade = SpringContextHolder.getBean(UsmContextVariables.class);
		UsmProperties usmProperties = usmContextFacade.getUsmProperties();
		return usmProperties.getConfigPath();
	}

	/**
	 * 根据key 获取系统参数
	 * 
	 * @param paramKey
	 * @return
	 */
	public static Parameter getUsmParameter(String paramKey) {
		UsmContextVariables usmContextFacade = SpringContextHolder.getBean(UsmContextVariables.class);
		ParameterService parameterService = usmContextFacade.getParameterService();
		return parameterService.findByParamKey(paramKey);
	}

	/**
	 * 查询系统的发布订阅的主题
	 * 
	 * @return
	 */
	public static Map<String, String> getTopicMap() {
		UsmContextVariables usmContextFacade = SpringContextHolder.getBean(UsmContextVariables.class);
		UsmProperties parameterService = usmContextFacade.getUsmProperties();
		return parameterService.getTopicMap();
	}
}
