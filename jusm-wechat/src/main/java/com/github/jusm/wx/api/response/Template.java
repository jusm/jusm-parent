package com.github.jusm.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 *
 */
public class Template {
	
	@JSONField(name = "template_id", ordinal = 1)
	private String templateId;

	@JSONField(name = "title", ordinal = 2)
	private String title;

	@JSONField(name = "content", ordinal = 3)
	private String content;
	
	@JSONField(name = "example", ordinal = 4)
	private String example;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}
}
