package com.github.jusm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class UuidEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * @formatter:off
	 * 【hibernate】主键生成策略使用UUID报出如下警告：org.hibernate.id.UUIDHexGenerator - HHH000409:
	 * Using org.hibernate.id.UUIDHexGenerator which does not generate IETF RFC 4122
	 * compliant UUID values; 主键生成策略使用UUID报出如警告如下：
	 * 控制台- 2017-11-24 18:40:14 [restartedMain] WARN 
	 * org.hibernate.id.UUIDHexGenerator - HHH000409: Using org.hibernate.id.UUIDHexGenerator which does not generate IETF RFC 4122 compliant UUID values; consider using org.hibernate.id.UUIDGenerator instead
	 * 说是它不生成符合IETF RFC 4122标准的UUID值; 请考虑使用org.hibernate.id.UUIDGenerator。
	 * @formatter:on
	 */
	@Id
	// @GeneratedValue(generator = "jpa-uuid")
	// @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@ApiModelProperty(value = "UUID主键 新增可以不用输入", position = Integer.MIN_VALUE)
	protected String id;

	@Column(name = "create_by",updatable=false)
	@ApiModelProperty(value = "创建者的用户名", position = Integer.MAX_VALUE - 200, hidden = true)
	@CreatedBy
	protected String createBy;

	@Column(name = "create_time",updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	@ApiModelProperty(value = "创建时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE - 199, hidden = true)
	protected Date createTime;

	@Column(name = "lastmodified_by",insertable=false)
	@LastModifiedBy
	@ApiModelProperty(value = "更新者的用户名", position = Integer.MAX_VALUE - 198, hidden = true)
	protected String lastmodifiedBy;

	@JsonIgnoreProperties(allowSetters=false)
	@Column(name = "lastmodified_time",insertable=false)
	@LastModifiedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@ApiModelProperty(value = "最后更新时间 格式:yyyy-MM-dd HH:mm:ss", position = Integer.MAX_VALUE - 197, hidden = true)
	protected Date lastmodifiedTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastmodifiedBy() {
		return lastmodifiedBy;
	}

	public void setLastmodifiedBy(String lastmodifiedBy) {
		this.lastmodifiedBy = lastmodifiedBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastmodifiedTime() {
		return lastmodifiedTime;
	}

	public void setLastmodifiedTime(Date lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UuidEntity other = (UuidEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}