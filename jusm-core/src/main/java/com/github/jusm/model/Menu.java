package com.github.jusm.model;


import java.io.Serializable;

/**
 * 菜单管理
 */
public class Menu implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	private String id;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private String parentId;
	
	/**
	 * 父菜单名称
	 */
	private String parentName;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String uri;

	/**
	 * 编码
	 */
	private String number;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private String type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer position;
	
	/**
	 * ztree属性
	 */
	private String description;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 设置：父菜单ID，一级菜单为0
	 * @param parentId 父菜单ID，一级菜单为0
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父菜单ID，一级菜单为0
	 * @return Long
	 */
	public String getParentId() {
		return parentId;
	}
	
	/**
	 * 设置：菜单名称
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：菜单名称
	 * @return String
	 */
	public String getName() {
		return name;
	}
	

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 设置：菜单图标
	 * @param icon 菜单图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取：菜单图标
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}
 
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
