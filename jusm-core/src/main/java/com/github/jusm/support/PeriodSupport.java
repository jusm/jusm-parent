package com.github.jusm.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModelProperty;

public class PeriodSupport extends PaginationSupport{
	
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss - yyyy-MM-dd HH:mm:ss";

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	@ApiModelProperty(value = "时间区间过滤 约定格式yyyy-MM-dd HH:mm:ss - yyyy-MM-dd HH:mm:ss")
	private String period;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Date getStartDateTime() {
		try {
			String period = getPeriod();
			if (StringUtils.isBlank(period)) {
				return null;
			}
			String startDateTime = period.substring(0, 19);
			if (startDateTime != null) {
				SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
				return df.parse(startDateTime);
			}
		} catch (ParseException e) {
			return null;
		}
		return null;
	}

	public Date getEndDateTime() {
		try {
			String period = getPeriod();
			if (StringUtils.isBlank(period)) {
				return null;
			}
			String endDateTime = getPeriod().substring(getPeriod().length() - 19, getPeriod().length());
			if (endDateTime != null) {
				SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
				return df.parse(endDateTime);
			}
		} catch (ParseException e) {
			return null;
		}
		return null;
	}
}
