package com.github.jusm.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BasicEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "create_by",updatable=false)
	@ApiModelProperty(value = "创建者的用户名", position = Integer.MAX_VALUE - 200,hidden=true)
	@CreatedBy
	@JsonIgnoreProperties(allowGetters=true)
	protected String createBy;

	@Column(name = "create_time",updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	@ApiModelProperty(value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE - 199,hidden=true)
	@JsonIgnoreProperties(allowGetters=true)
	protected Date createTime;
	
	@Column(name = "lastmodified_by",insertable=false)
	@LastModifiedBy
	@ApiModelProperty(value = "更新者的用户名", position = Integer.MAX_VALUE - 198,hidden=true)
	@JsonIgnoreProperties(allowGetters=true)
	protected String lastmodifiedBy;


    @Column(name = "lastmodified_time",insertable=false)
	@LastModifiedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@ApiModelProperty(value = "最后更新时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE - 197,hidden=true)
    @JsonIgnoreProperties(allowGetters=true)
	protected Date lastmodifiedTime;


	public String getCreateBy() {
		return createBy;
	}


	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getLastmodifiedBy() {
		return lastmodifiedBy;
	}


	public void setLastmodifiedBy(String lastmodifiedBy) {
		this.lastmodifiedBy = lastmodifiedBy;
	}


	public Date getLastmodifiedTime() {
		return lastmodifiedTime;
	}


	public void setLastmodifiedTime(Date lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	
}