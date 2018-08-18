package com.github.jusm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CREATE TABLE `persistent_logins` ( `username` varchar(64) NOT NULL, `series`
 * varchar(64) NOT NULL, `token` varchar(64) NOT NULL, `last_used` timestamp NOT
 * NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY
 * (`series`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * 
 *
 */
public class LogoutEntity {
	
	@Column(length = 64, nullable = false)
	private String username;
	
	@Column(length = 64, nullable = false)
	private String series;
	
	@Id
	@Column(length = 64, nullable = false)
	private String token;
	
	@Column(name = "last_used",  nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed = new Date();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

}
