package com.github.jusm.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

public class Keyword {

	@JSONField(name="keyword_id")
	private int keywordId;
	private String name;
	private String example;
	public int getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(int keywordId) {
		this.keywordId = keywordId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	
}
