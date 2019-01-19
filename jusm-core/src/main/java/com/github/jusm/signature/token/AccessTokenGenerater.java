package com.github.jusm.signature.token;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.jusm.model.R;
import com.github.jusm.model.ReturnCode;
import com.github.jusm.signature.util.MD5Tool;

/**
 * AccessToken生成器
 */
public class AccessTokenGenerater {
	
	
	public static class SignResult{
		
		private String sortJson;
		
		private String sign;
		
		public SignResult(String sortJson, String sign) {
			super();
			this.sortJson = sortJson;
			this.sign = sign;
		}

		public String getSortJson() {
			return sortJson;
		}

		public void setSortJson(String sortJson) {
			this.sortJson = sortJson;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}
		
	}
	/**
	 * 生成签名
	 * @param secret key 秘钥
	 * @param jsonParam json类型的入参
	 * @return
	 */
	public static R getMD5Result(String secretKey, String jsonParam) {
		Map<String, String> valList = new HashMap<>();
		Map<String, String> dataMap = new HashMap<>();
		/**
		 * 将JSON字符串中的换行、回车、tab建去除
		 */
//		jsonParam = jsonParam.replace("\r\n", "").replace("\r", "").replace("\t", "").replace("\n", "").replace(" ",
//				"");
		jsonParam = jsonParam.replace("\r\n", "").replace("\r", "").replace("\t", "").replace("\n", "");
		/**
		 * JSON中key值集合
		 */
		List<String> keyList = new ArrayList<String>();
		dataMap.put("error", "");
		try {
			keyList = get(keyList, jsonParam, valList);
		} catch (Exception e) {
			e.printStackTrace();
			return  R.result(ReturnCode.GENERATE_SIGN_ERROR,"请检查JSON字符串的格式是否正确，比如包含中文符号等");
		}
		
		/**
		 * 对JSON中的key进行排序
		 */
		Collections.sort(keyList);
		/**
		 * 根据排序后的key重新排序成JSON字符串
		 */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sb.append(keyList.get(i));
			sb.append(valList.get(keyList.get(i)));
		}
		jsonParam = sb.toString();
		/**
		 * 秘钥 + JSON 进行MD5加密
		 */
		String str = secretKey.replace(" ", "") + jsonParam;
		dataMap.put("sortJson", str);
		String md5Param = "";
		try {
			md5Param = MD5Tool.encryptionMD5(str).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return  R.result(ReturnCode.GENERATE_SIGN_ERROR,e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return  R.result(ReturnCode.GENERATE_SIGN_ERROR,e.getMessage());
		}
		return R.success(new SignResult(str,md5Param));
	}
	public static Map<String, String> getMD5(String secretKey, String jsonParam) throws Exception {
		Map<String, String> valList = new HashMap<>();
		Map<String, String> dataMap = new HashMap<>();
		/**
		 * 将JSON字符串中的换行、回车、tab建去除
		 */
		jsonParam = jsonParam.replace("\r\n", "").replace("\r", "").replace("\t", "").replace("\n", "").replace(" ",
				"");
		/**
		 * JSON中key值集合
		 */
		List<String> keyList = new ArrayList<String>();
		dataMap.put("error", "");
		try {
			keyList = get(keyList, jsonParam, valList);
		} catch (Exception e) {
			throw e;
		}
		/**
		 * 对JSON中的key进行排序
		 */
		Collections.sort(keyList);
		/**
		 * 根据排序后的key重新排序成JSON字符串
		 */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keyList.size(); i++) {
			sb.append(keyList.get(i));
			sb.append(valList.get(keyList.get(i)));
		}
		jsonParam = sb.toString();

		/**
		 * 秘钥 + JSON 进行MD5加密
		 */
		String str = secretKey.replace(" ", "") + jsonParam;
		dataMap.put("sortJson", str);
		String MD5Param = "";
		try {
			MD5Param = MD5Tool.encryptionMD5(str).toUpperCase();
			dataMap.put("md5Json", MD5Param);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataMap;
	}
	/**
	 * 循环获取json中属性值
	 * 
	 * @param list
	 * @param str
	 * @param valList
	 * @return
	 * @throws Exception
	 */
	private static List<String> get(List<String> list, String str, Map<String, String> valList) throws Exception {
		JSONObject json = JSONObject.parseObject(str);
		Iterator<String> it = json.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String val = json.getString(key);
			try {
				get(list, val, valList);
			} catch (Exception e) {
				if (StringUtils.isEmpty(val) || "null".equalsIgnoreCase(val)) {
				} else {
					list.add(key);
					valList.put(key, val);
				}
			}
		}
		return list;
	}
}
