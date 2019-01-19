package com.github.jusm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="usm_logging_level")
public class LoggingLevel extends UuidEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(unique=true)
	private String basePackage; 
	
	private String configuredLevel;

	private String effectiveLevel;

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getConfiguredLevel() {
		return configuredLevel;
	}

	public void setConfiguredLevel(String configuredLevel) {
		this.configuredLevel = configuredLevel;
	}

	public String getEffectiveLevel() {
		return effectiveLevel;
	}

	public void setEffectiveLevel(String effectiveLevel) {
		this.effectiveLevel = effectiveLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
