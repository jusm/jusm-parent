package com.github.jusm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint.LoggerLevels;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.jusm.entity.Parameter;
import com.github.jusm.service.ParameterService;

@Controller
public class ProfilesController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoggersEndpoint delegate;
	
	@Autowired
	private ParameterService parameterService;
	
	@GetMapping("profiles.html")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		LoggerLevels levels = this.delegate.invoke("com.github");
	    logger.debug("Logger Level ：DEBUG");
        logger.info("Logger Level ：INFO");
        logger.warn("Logger Level ：WARN");
        logger.error("Logger Level ：ERROR");
        mav.setViewName("profiles");
        mav.addObject("ConfiguredLevel", levels.getConfiguredLevel());
        mav.addObject("EffectiveLevel", levels.getEffectiveLevel());
        
    	List<Parameter> parameter = parameterService.findAll();
    	mav.addObject("parameters", parameter);
		return mav;
	}
}
