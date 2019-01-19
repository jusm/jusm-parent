package com.github.jusm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jusm.entity.LoggingLevel;

public interface LoggingLevelRepository extends JpaRepository<LoggingLevel, String> {

	LoggingLevel findByBasePackage(String pkg);


}
