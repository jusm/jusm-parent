package com.github.jusm.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.ning.http.client.AsyncHttpClient;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UsmHystrixCommand2 extends HystrixCommand<Response> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String url;

	private String json;

	private Map<String, String> headers = new HashMap<>();

	private HttpMethod httpMethod;

	private OkHttpClient okHttpClient;
	
	
	public UsmHystrixCommand2(Setter setter, String url, Map<String, String> headers, String json,
			HttpMethod httpMethod, OkHttpClient okHttpClient) {
		super(setter);
		this.url = url;
		if (headers == null) {
			headers = new HashMap<>();
			headers.put("Content-Type", "application/json;charset=UTF-8");
		}
		this.headers = headers;
		this.json = json;
		this.httpMethod = httpMethod;
		this.okHttpClient = okHttpClient;
	}

	public UsmHystrixCommand2(Setter setter, String url, String requestBody, HttpMethod httpMethod,
			OkHttpClient asyncHttpClient) {
		this(setter, url, null, requestBody, httpMethod, asyncHttpClient);
	}

	public UsmHystrixCommand2(Setter setter, String url, HttpMethod httpMethod, OkHttpClient asyncHttpClient) {
		this(setter, url, null, httpMethod, asyncHttpClient);
	}

	@Override
	protected Response run() throws Exception {
		Response response = null;
		Request request;
		try {
			Request.Builder builder = new Request.Builder();
			Call call;
			switch (httpMethod) {
			case POST:
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}
				if (StringUtils.isNotBlank(json)) {
					// MediaType 设置Content-Type 标头中包含的媒体类型值
					RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
					request = builder.url(url)// 请求的url
							.post(requestBody).build();
				} else {
					request = builder.url(url)// 请求的url
							.build();
				}
				call = okHttpClient.newCall(request);
				response = call.execute();
				break;
			case GET:
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					builder.addHeader(entry.getKey(), entry.getValue());
				}
				request = builder.url(url)// 请求的url
						.build();
				call = okHttpClient.newCall(request);
				response = call.execute();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected Response getFallback() {
		logger.error("Active hystrix getFallback: request " + url + " method: " + httpMethod.name());
		throw new RuntimeException("active hystrix getFallback");// 实现快速失败
	}
}
