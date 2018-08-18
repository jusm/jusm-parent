package com.github.jusm.hystrix;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class UsmHystrixCommandWapper {

	@Value("${hystrix.execute.timeout:15000}")
	private int timeout;

	@Value("${hystrix.threadPool.coreSize:20}")
	private int coreSize;

	@Autowired
	private AsyncHttpClient asyncHttpClient;

	public Response execute(String seriveName, String methodName, String url, Map<String, String> headers, String requestBody, HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand(setter, url, headers, requestBody, httpMethod, asyncHttpClient).execute();
	}
	
	public Response execute(String seriveName, String methodName, String url, String requestBody, HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand(setter, url, requestBody, httpMethod, asyncHttpClient).execute();
	}
	
	public Response execute(String seriveName, String methodName, String url,   HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand(setter, url, httpMethod, asyncHttpClient).execute();
	}
}
