package com.github.jusm.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpMethod;

import com.github.jusm.hystrix.UsmHystrixCommand2;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 
 * @author haoran.wen
 */
public class DefaultJusmClient {

	private int timeout = 15000;

	private int coreSize = 20;

//	private AsyncHttpClient asyncHttpClient;
	private OkHttpClient okHttpclient;

	public DefaultJusmClient() {
	
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.callTimeout(5, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS);
		okHttpclient = builder.build();
//		AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
//		builder.setConnectTimeout(5000);
//		builder.setReadTimeout(15000);
//		builder.setRequestTimeout(15000);
//		asyncHttpClient = new AsyncHttpClient(builder.build());
	}

	public Response execute(String seriveName, String methodName, String url, Map<String, String> headers,
			String requestBody, HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand2(setter, url, headers, requestBody, httpMethod, okHttpclient).execute();
	}

	public Response execute(String seriveName, String methodName, String url, String requestBody,
			HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand2(setter, url, requestBody, httpMethod, okHttpclient).execute();
	}

	public Response execute(String seriveName, String methodName, String url, HttpMethod httpMethod) {
		HystrixCommand.Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(seriveName));
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(seriveName + ":" + methodName));
		setter.andCommandPropertiesDefaults(
				HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout));
		setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(coreSize));
		return new UsmHystrixCommand2(setter, url, httpMethod, okHttpclient).execute();
	}

}
