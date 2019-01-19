package com.github.jusm.service;

import java.util.Collection;
import java.util.List;

import com.github.jusm.entity.Parameter;


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
	 * @param collection
	 * @return
	 */
	void save(Collection<Parameter> collection);
	
	
	List<Parameter> findAll();
	
	public Parameter save(Parameter param);
}
