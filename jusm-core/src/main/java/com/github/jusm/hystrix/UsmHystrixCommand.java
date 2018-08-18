package com.github.jusm.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.netflix.hystrix.HystrixCommand;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

public class UsmHystrixCommand extends HystrixCommand<Response> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String url;

	private String requestBody;

	private Map<String, String> headers = new HashMap<>();

	private HttpMethod httpMethod;

	private AsyncHttpClient asyncHttpClient;

	protected UsmHystrixCommand(Setter setter, String url, Map<String, String> headers, String requestBody,
			HttpMethod httpMethod, AsyncHttpClient asyncHttpClient) {
		super(setter);
		this.url = url;
		if (headers == null) {
			headers = new HashMap<>();
			headers.put("Content-Type", "application/json;charset=UTF-8");
		}
		this.headers = headers;
		this.requestBody = requestBody;
		this.httpMethod = httpMethod;
		this.asyncHttpClient = asyncHttpClient;
	}

	protected UsmHystrixCommand(Setter setter, String url, String requestBody, HttpMethod httpMethod,
			AsyncHttpClient asyncHttpClient) {
		this(setter, url, null, requestBody, httpMethod, asyncHttpClient);
	}

	protected UsmHystrixCommand(Setter setter, String url, HttpMethod httpMethod, AsyncHttpClient asyncHttpClient) {
		this(setter, url, null, httpMethod, asyncHttpClient);
	}

	@Override
	protected Response run() throws Exception {
		Response response = null;
		Request request = null;
		BoundRequestBuilder builder = null;
		ListenableFuture<Response> responseFuture = null;
		try {
			switch (httpMethod) {
			case POST:
				builder = asyncHttpClient.preparePost(url);
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}
				if (StringUtils.isNotBlank(requestBody)) {
					builder.setBody(requestBody);
				}
				request = builder.build();
				responseFuture = asyncHttpClient.executeRequest(request);
				response = responseFuture.get();
				break;
			case GET:
				builder = asyncHttpClient.prepareGet(url);
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}
				if (StringUtils.isNotBlank(requestBody)) {
					builder.setBody(requestBody);
				}
				request = builder.build();
				responseFuture = asyncHttpClient.executeRequest(request);
				response = responseFuture.get();
				break;
			default:
				request = new RequestBuilder().setUrl(url).build();
				responseFuture = asyncHttpClient.executeRequest(request);
				response = responseFuture.get();
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (responseFuture != null) {
				responseFuture.abort(e);
			}
		}
		return response;
	}

	@Override
	protected Response getFallback() {
		logger.error("Active hystrix getFallback: request " + url + " method: " + httpMethod.name());
		throw new RuntimeException("active hystrix getFallback");// 实现快速失败
	}
}
