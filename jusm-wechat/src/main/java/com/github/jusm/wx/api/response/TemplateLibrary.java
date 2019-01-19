package com.github.jusm.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 */
public class TemplateLibrary {
	
	@JSONField(name = "id", ordinal = 1)
	private String id;

	@JSONField(name = "title", ordinal = 2)
	private String title;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
