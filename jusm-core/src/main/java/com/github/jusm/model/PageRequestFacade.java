package com.github.jusm.model;

import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import io.swagger.annotations.ApiModelProperty;

public class PageRequestFacade {
	
	@Transient
	@ApiModelProperty(value = "当前第几页 默认 1", example = "1",position = Integer.MAX_VALUE - 100)
	@Min(value = 1, message = "当前页码必须为正整数")
	private int pageIndex = 1;
	

	@ApiModelProperty(value = "每页大小 默认 10", example = "10",position = Integer.MAX_VALUE - 99)
	@Min(value = 1, message = "每页大小必须为正整数")
	private int pageSize = 10;

	@ApiModelProperty(value = "排序字段(按照javaBean中的字段名称 而不是数据库中的字段)",position = Integer.MAX_VALUE - 98)
	private String[] sortFields;

	@ApiModelProperty(allowableValues = "ASC, DESC", value = "排序方式  升序: ASC, 降序: DESC;",example="ASC",position = Integer.MAX_VALUE - 97)
	private Direction direction = Direction.ASC;

	/**
	 * 获取当前页面的大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 当前页面的号码
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String[] getSortFields() {
		return sortFields;
	}

	public void setSortFields(String[] sortFields) {
		this.sortFields = sortFields;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * 获取分页请求参数
	 * 
	 * @return
	 */
	public PageRequest getPageRequest() {
		if (sortFields != null && sortFields.length > 0) {
			return new PageRequest(pageIndex - 1, pageSize, direction, sortFields);
		} else {
			return new PageRequest(pageIndex - 1, pageSize);
		}
	}

}