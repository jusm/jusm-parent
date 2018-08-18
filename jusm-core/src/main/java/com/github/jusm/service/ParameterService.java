package com.github.jusm.service;

import com.github.jusm.entities.Parameter;


public interface ParameterService {

	/**
	 * 查询参数
	 * @param key 参数key
	 * @return
	 */
	Parameter findByParamKey(String paramKey);
	
	/**
	 * 是否安装
	 * @return
	 */
	boolean isSetup();

	/**
	 * 保存系统参数
	 * @param param
	 * @return
	 */
	Parameter save(Parameter param);
}
