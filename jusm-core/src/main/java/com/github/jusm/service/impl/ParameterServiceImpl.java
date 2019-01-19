package com.github.jusm.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.github.jusm.cache.UsmCacheNames;
import com.github.jusm.entity.Parameter;
import com.github.jusm.repository.ParameterRepository;
import com.github.jusm.service.ParameterService;

@CacheConfig(cacheNames=UsmCacheNames.PARAMTER)
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	private ParameterRepository parameterRepository;

	@Override
	@Cacheable(unless = "#result == null",condition="#paramKey != null or #paramKey != ''")
	public Parameter findByParamKey(String paramKey) {
		return parameterRepository.findByParamKey(paramKey);
	}

	@Override
	@Caching(put = { @CachePut(value="usm:parameter",key = "#result.id"), @CachePut(value="usm:parameter",key = "#result.paramKey") })
	public Parameter save(Parameter param) {
		return parameterRepository.save(param);
	}

	@Override
//	@Cacheable(key="#root.methodName",unless="!#result")
	public boolean isSetup() {
		return (null != findByParamKey(Parameter.SYS_CREATE_DATE_KEY));
	}

	@Override
	public List<Parameter> findAll() {
		return parameterRepository.findAll();
	}

	@Override
	public void save(Collection<Parameter> collection) {
		parameterRepository.save(collection);
	}
}
