package com.github.jusm.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 权限表 1,包含菜单
 * 
 *
 */
@Entity
@Table(name = "usm_permission")
public class Permission extends UuidEntity {

	/**
	 * 编码前缀
	 */
	public static String PREFIX_OF_NUMBER = "p_";
	/**
	 *
	 */
	private static final long serialVersionUID = 8581140638871641542L;

	/** 资源名称 */
	@Column(unique = true)
	private String name;

	/** 菜单编码 **/
	@Column(unique = true, nullable = false)
	private String number;

	/** uri统一资源标识 */
	@Column(unique = false)
	private String uri;
	
	@Column
	private String icon;

	/** url地址 */
	@Column(name = "http_method", columnDefinition = "integer COMMENT '0:GET, 1:HEAD, 2:POST, 3:PUT, 4:PATCH, 5:DELETE, 6:OPTIONS, 7:TRACE' ")
	private HttpMethod httpMethod;

	@Column(name = "f_type", columnDefinition = "integer COMMENT '0:目录; 1: 菜单; 2: 功能; 3: 按钮; 4: 资源;' ")
	@Enumerated(EnumType.ORDINAL)
	private Type type;

	/** 描述 */
	@Column(name = "description")
	private String description;

	/**
	 * 位置
	 */
	@Column(name = "position", nullable = false)
	private Integer position = 999;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	@JsonBackReference("Permission")
	@JsonIgnore
	private Permission parent;

	@SuppressWarnings("deprecation")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", orphanRemoval = true)
	@JsonManagedReference("Permission")
	@org.hibernate.annotations.ForeignKey(name = "none")
	private List<Permission> children = new ArrayList<Permission>(0);

	@JsonBackReference("Permission")
	@ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>();

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	@JsonBackReference("Permission")
	public Permission getParent() {
		return parent;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@JsonBackReference("Permission")
	public void setParent(Permission parent) {
		this.parent = parent;
	}
	
	@JsonManagedReference("Permission")
	public List<Permission> getChildren() {
		Collections.sort(children, new Comparator<Permission>() {
			@Override
			public int compare(Permission o1, Permission o2) {
				return o1.getPosition().equals(o2.getPosition()) ? o1.getNumber().compareTo(o2.getNumber())
						: o1.getPosition().compareTo(o2.getPosition());
			}
		});
		return children;
	}

	@JsonManagedReference("Permission")
	public void setChildren(List<Permission> children) {
		this.children = children;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Permission [name=" + name + ", number=" + number + ", uri=" + uri + ", httpMethod=" + httpMethod
				+ ", description=" + description + ", position=" + position + ", parent=" + parent + ", children="
				+ children + ", roles=" + roles + "]";
	}

	/**
	 * 权限许可证类型
	 */
	public enum Type {
		/**
		 * 0：目录
		 */
		CATALOG,

		/**
		 * 1：菜单
		 */
		MENU,

		/**
		 * 2：功能
		 */
		FUNCTION,

		/**
		 * 3：按钮
		 */
		BUTTON,

		/**
		 * 4：资源
		 */
		RESOURCE;
	}
}
