package com.github.jusm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint.LoggerLevels;

import com.github.jusm.entity.LoggingLevel;
import com.github.jusm.repository.LoggingLevelRepository;
import com.github.jusm.service.LoggingLevelService;

public class LoggingLevelServiceImpl implements LoggingLevelService {

	@Autowired
	private LoggersEndpoint delegate;
	
	
	@Autowired
	private LoggingLevelRepository loggingLevelRepository;

	private static final String root = "root";
	private static final String hibernate = "org.hibernate";
	private static final String springframework = "org.springframework";
	private static final String jusm = "com.github.jusm";
	
	
	private static final List<String> pkgs = Arrays.asList(root,hibernate,springframework,jusm);
	// log4j.logger.org.hibernate=ERROR
	// log4j.logger.org.springframework=ERROR
	// logging.level.root=WARN
	// com.github

	@PostConstruct
	private void init() {
		List<LoggingLevel> list = new ArrayList<>();
		for (Iterator<String> iterator = pkgs.iterator(); iterator.hasNext();) {
			String pkg = (String) iterator.next();
			LoggerLevels levels = delegate.invoke(pkg);
			LoggingLevel loggingLevel = loggingLevelRepository.findByBasePackage(pkg);
			if(loggingLevel == null) {
				loggingLevel = new LoggingLevel();
			}
			loggingLevel.setBasePackage(pkg);
			loggingLevel.setConfiguredLevel(levels.getConfiguredLevel());
			loggingLevel.setEffectiveLevel(levels.getEffectiveLevel());
			list.add(loggingLevel);
		}
		loggingLevelRepository.save(list);
	}
	
	
	
}
