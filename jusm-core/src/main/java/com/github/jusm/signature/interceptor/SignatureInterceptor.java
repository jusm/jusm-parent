package com.github.jusm.signature.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.github.jusm.signature.util.StringUtil;

public class SignatureInterceptor extends HandlerInterceptorAdapter {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 将特殊的几个组织码提出来
	public final static Map<String, String> kyeMap = new HashMap<String, String>();
	
	/**
	 * 成功
	 */
	public static final int WS_RET_SUCCESS = 1;

	/**
	 * 失败
	 */
	public static final int WS_RET_FAIL = 0;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
		/*try {
			// 获取access-token
			String accessToken = request.getHeader("access-token");
			// 组织机构kye
			String kye = request.getHeader("kye");
			// 请求路径
			String reqPath = request.getServletPath();

			// 如果进入了拦截器，但header中kye access-token无值，则直接返回鉴权失败
			if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(kye)) {
				// 如果有鉴权失败的接口，则打印日志
				log.error("[authErrorPath] = " + reqPath + " [reasonOne] = accessToken or kye is empty");
				response.setContentType("text/json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				PrintWriter out = response.getWriter();
				out.write(R.result(ReturnCode.UNAUTHORIZED_ERROR,"鉴权失败,消息头部请携带kye和access-token请求参数!").toString());
				out.close();
				return false;
			}
			List<AccessControlLimit> accessContrlList = getAccessControlList(accessControlJson);
			// 具体请求的接口名
			String reqInterface_name = StringUtil.isEmpty(reqPath) ? ""
					: reqPath.substring(reqPath.lastIndexOf("/") + 1).trim();

			// 组织机构kye为提供给外部的组织机构代码，如10006，...，表示是提供给外部的接口，需要先过滤，只允许请求指定接口
			if (accessContrlList != null && accessContrlList.size() > 0) {
				for (AccessControlLimit accessObj : accessContrlList) {
					String accessKye = accessObj.getKye();
					String accessInterface = accessObj.getAccessInterface();
					if (!"all".equals(accessInterface) && accessKye.equals(kye)) {
						if (!StringUtil.isEmpty(reqPath) && !StringUtil.isEmpty(accessInterface)) {
							// reqPath = reqPath.substring(reqPath.indexOf("/") + 1).trim();
							if (!accessInterface.contains(reqInterface_name)) {
								// 如果有鉴权失败的接口，则打印日志
								log.error("[authErrorPath] = " + reqPath + " [reasonTwo] = request unauth interface");
								response.setContentType("application/json;charset=UTF-8");
								response.setHeader("Cache-Control", "no-cache");
								PrintWriter out = response.getWriter();
								out.write(R.result(ReturnCode.AUTH_ERROR, "无效的签名").toString());
								out.close();
								return false;
							}
						}
					}
				}
			}
			String bodyParam = null;
			// body中前台传入的参数
			if( request != null && request instanceof BodyReaderHttpServletRequestWrapper ) {
				bodyParam =	((BodyReaderHttpServletRequestWrapper)request).getOriginalBody();
			}else {
				bodyParam = HttpHelper.getBodyString(request);
			}
			log.debug("路径: " + reqPath + " 鉴权：" + accessToken + "||" + bodyParam);
			// 那要判断下 access-token 、 kye 这两个入参 如果为空的话 直接返回 鉴权失败直接返回 鉴权失败，您无权限访问！
			if (StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(kye)) {

				// 如果有鉴权失败的接口，则打印日志
				log.error("[authErrorPath] = " + reqPath + " [reasonThree] = accessToken or kye is empty");
				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				PrintWriter out = response.getWriter();
				out.write(R.result(ReturnCode.AUTH_ERROR, "无效的签名").toString());
				out.close();
				return false;
			}

			// 数组排序后字符串
			String sortStr = "";
			// 秘钥
			String accesskey = "";
			List<String> keyList = new ArrayList<String>();
			// body数据保存到map
			Map<String, String> valList = new ConcurrentHashMap<String, String>();
			// 去body非空入参
			keyList = get0(keyList, bodyParam, kye, valList);
			// 对数组进行排序
			Collections.sort(keyList);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < keyList.size(); i++) {

				String tempKey = keyList.get(i);
				String tempValue = valList.get(tempKey);

				// 判断SQL注入
				// if(SqlParamFilterUtil.sqlInjectValidate(tempValue)) {
				// result.setRetStatus(0);
				// result.setErrMsg(ERRORMESSAGE);
				// response.setContentType("text/json;charset=UTF-8");
				// response.setHeader("Cache-Control", "no-cache");
				// PrintWriter out = response.getWriter();
				// out.write(JSONObject.fromObject(result).toString());
				// out.close();
				// return false;
				// }

				sb.append(tempKey);
				sb.append(tempValue);
			}
			sortStr = sb.toString();

			// 因为机构和Key在配置文件中是一一对应的，所以可以通过解析的机构号获取相应Key
			for (AccessControlLimit accessObj : accessContrlList) {
				String tempKye = accessObj.getKye();
				String tempVal = accessObj.getAccessKey();
				if (tempKye.equals(kye)) {
					accesskey = tempVal;
				}
			}
			// 进行MD5加密
			// 消息签名规则： MD5(秘钥+参数串)
			String encryptionStr = ToolsUtil.encryptionMD5(accesskey + sortStr);
			encryptionStr = encryptionStr.toUpperCase();
			log.debug("========================encryptionStr=" + encryptionStr);
			if (!encryptionStr.equals(accessToken)) {

				// 如果有鉴权失败的接口，则打印日志
				log.error("[authErrorPath] = " + reqPath + " [clientAccessTokenAndParam] = " + accessToken + "||"
						+ bodyParam + " [clientKye] = " + kye + " [serverAccessToken] = " + encryptionStr
						+ " [reasonFour] = accessToken is not match");

				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("Cache-Control", "no-cache");
				PrintWriter out = response.getWriter();
				out.write(R.result(ReturnCode.AUTH_ERROR, "无效的签名").toString());
				out.close();
				return false;
			}

			return true;
		} catch (JSONException e) {
			log.info("鉴权解析json异常：" + e);
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.write(R.result(ReturnCode.AUTH_ERROR, "鉴权解析json异常").toString());
			out.close();
			return false;
		} catch (Exception e) {
			log.error("",e);
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.write(R.result(ReturnCode.AUTH_ERROR, "无效的签名").toString());
			out.close();
			return false;
		}*/
	}
	
	
	/**
	 * 循环获取json中属性值
	 * 
	 * @param list
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private List<String> get0(List<String> list, String str, String access_kye, Map<String, String> valList)
			throws JSONException, Exception {
		com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(str);
		Iterator<String> it = json.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			Object val = json.get(key);
			if (val != null) {
				if (val instanceof com.alibaba.fastjson.JSONObject) {
					if (!((com.alibaba.fastjson.JSONObject) val).isEmpty()) {
						list.add(key);
						valList.put(key, JSON.toJSONString(val));
					}
				} else if (val instanceof CharSequence) {
					CharSequence valTemp = (CharSequence) val;
					if (StringUtils.isNotBlank(valTemp) && !StringUtil.isEmpty(valTemp.toString())
							&& !"null".equalsIgnoreCase(valTemp.toString())) {
						list.add(key);
						valList.put(key, valTemp.toString());
					}
				} else {
					list.add(key);
					// 防止取JSON数组值的时候某些特殊字符被转码导致鉴权失败(gson.toJson是会转码的),之前的就不要去动它了
					if (kyeMap.containsKey(access_kye)) {
						valList.put(key,  JSON.toJSONString(val));
					} else {
						valList.put(key, val.toString());
					}
				}
			}

		}
		return list;
	}

	/**
	 * 循环获取json中属性值
	 * 
	 * @param list
	 * @param str
	 * @return
	 * @throws Exception
	 */
//	private List<String> get1(List<String> list, String str, String access_kye, Map<String, String> valList)
//			throws JSONException, Exception {
//		
//		JSONObject json = JSONObject.fromObject(str);
//		Iterator it = json.keys();
//		while (it.hasNext()) {
//			String key = it.next().toString();
//			Object val = json.get(key);
//			if (val != null) {
//				if (val instanceof JSONObject) {
//					if (!((JSONObject) val).isNullObject()) {
//						list.add(key);
//						valList.put(key, gson.toJson(val));
//					}
//				} else if (val instanceof CharSequence) {
//					CharSequence valTemp = (CharSequence) val;
//					if (StringUtils.isNotBlank(valTemp) && !StringUtil.isEmpty(valTemp.toString())
//							&& !"null".equalsIgnoreCase(valTemp.toString())) {
//						list.add(key);
//						valList.put(key, valTemp.toString());
//					}
//				} else {
//					list.add(key);
//					// 防止取JSON数组值的时候某些特殊字符被转码导致鉴权失败(gson.toJson是会转码的),之前的就不要去动它了
//					if (kyeMap.containsKey(access_kye)) {
//						valList.put(key, gson.toJson(val));
//					} else {
//						valList.put(key, val.toString());
//					}
//				}
//			}
//
//		}
//		return list;
//	}

 
}
