package com.github.jusm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entity.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, Integer> {

	Parameter findByParamKey(String paramKey);

}
