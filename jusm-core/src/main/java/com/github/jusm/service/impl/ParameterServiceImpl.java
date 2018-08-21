package com.github.jusm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.entities.Parameter;
import com.github.jusm.repository.ParameterRepository;
import com.github.jusm.service.ParameterService;

@CacheConfig(cacheNames = UsmCacheNames.PARAMTER)
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	private ParameterRepository parameterRepository;

	@Override
	@Cacheable
	public Parameter findByParamKey(String paramKey) {
		return parameterRepository.findByParamKey(paramKey);
	}

	@Override
	@Caching(put = { @CachePut(key = "#result.id"), @CachePut(key = "#result.paramKey") })
	public Parameter save(Parameter param) {
		return parameterRepository.save(param);
	}

	@Override
	@Cacheable(key = "#root.methodName")
	public boolean isSetup() {
		return (null != parameterRepository.findByParamKey(Parameter.SYS_CREATE_DATE_KEY));
	}
}
