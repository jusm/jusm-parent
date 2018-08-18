package com.github.jusm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entities.Parameter;


public interface ParameterRepository extends JpaRepository<Parameter, String> {

	Parameter findByParamKey(String paramKey);
	
}
