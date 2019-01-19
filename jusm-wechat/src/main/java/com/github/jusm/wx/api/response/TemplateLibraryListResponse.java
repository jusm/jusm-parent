package com.github.jusm.wx.api.response;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class TemplateLibraryListResponse {
	
	private int errcode;
	
	private String errmsg;
	
	@JSONField(name="total_count")
	private int totalCount;

	private List<TemplateLibrary> list = new ArrayList<>();

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public List<TemplateLibrary> getList() {
		return list;
	}

	public void setList(List<TemplateLibrary> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	
}
