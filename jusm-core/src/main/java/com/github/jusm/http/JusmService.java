package com.github.jusm.http;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.github.jusm.model.R;

public abstract class JusmService {
	
	private final static String seriveName="kyeopenapi";
	
	@Autowired
	private JusmClient jusmClient;
	
	public R post( String methodName,String json) throws IOException {
//		json = "{\r\n" + 
//				"    \"billNoList\": [\r\n" + 
//				"        \"12548951236\",\r\n" + 
//				"        \"75600169267\"\r\n" + 
//				"    ]\r\n" + 
//				"}\r\n" + 
//				"";
		return jusmClient.execute(seriveName, methodName, getServiceAddress()+methodName, json, HttpMethod.POST);
	}
	
	public R post( String seriveName, String methodName,String json) throws IOException {
		return jusmClient.execute(seriveName, methodName, getServiceAddress(), json, HttpMethod.POST);
	}
	
	public abstract String getServiceAddress();
}
