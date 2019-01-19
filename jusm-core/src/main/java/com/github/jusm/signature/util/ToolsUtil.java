package com.github.jusm.signature.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.fastjson.JSON;

/**
 * 工具类
 * @author lyj 2015年12月11日
 *
 */
public class ToolsUtil {

	/**
	 * MD5加密
	 * @param content 需要加密的内容
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptionMD5(String content)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = null;
		messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(content.getBytes("UTF-8"));
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}
	
	/**
	 * 获取request中body值
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static String getBodyString(BufferedReader br) throws IOException {
		String inputLine;
		String str = "";
		while ((inputLine = br.readLine()) != null) {
			str += inputLine;
		}
		br.close();
		return str;
	}
	private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	/**
	 * 获取配置文件中的json字符串(拦截器使用)
	 */
	public static String accessControlJson() {
		try {
			return getFileContent("classpath:META-INF/config/accessControl.txt");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> aclRouter() {
		try {
			return JSON.parseArray(getFileContent("classpath:config/acl-router.json"), String.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFileContent(String path) throws IOException {
			// 获取所有匹配的文件
		Resource resource = resolver.getResource(path);
		// 获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
		InputStream stream = resource.getInputStream();
		return inputStream2String(stream);
	}
	
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	/**
	 * 处理服务端错误响应
	 * @return
	 */
//	public static Result handerGunServerErrorResponse() {
//		Result result = new Result();
//		result.setRetStatus(ConstantUtil.WS_RET_FAIL);
//		result.setErrCode(ConstantUtil.SERVER_ERROR_CODE);
//		result.setErrMsg(ConstantUtil.SERVER_ERROR_MSG);
//
//		TipInfo tipInfo = new TipInfo();
//		tipInfo.setRetMsg(ConstantUtil.SERVER_ERROR_MSG);
//
//		result.setResult(tipInfo);
//
//		return result;
//	}

	/**
	 * 处理客户端错误响应
	 * @return
	 */
//	public static Result handerClientErrorResponse() {
//		Result result = new Result();
//		result.setRetStatus(ConstantUtil.WS_RET_FAIL);
//		result.setErrCode(ConstantUtil.SERVER_ERROR_CODE);
//		result.setErrMsg(ConstantUtil.INPUTPARAMINVALID);
//
//		TipInfo tipInfo = new TipInfo();
//		tipInfo.setRetMsg(ConstantUtil.INPUTPARAMINVALID);
//
//		result.setResult(tipInfo);
//
//		return result;
//	}

	/**
	 * 生成接口请求md5的key
	 * @param strMD5
	 * @return
	 * @throws Exception
	 */
	public static String generateKey(String strMD5) throws Exception {
		String token = "12345678901234567890123456789090";
		Date nowTime=new Date();
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");
		String date = time.format(nowTime);
		return encode(strMD5 + date + token.substring(10, 10 + 16));
	}

	/**
	 * MD5加密
	 * @param content
	 * @return
	 */
	public static String encode(String content) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");// 获取MD5算法对象
			byte[] digest = instance.digest(content.getBytes("UTF-8"));// 对字符串加密,返回字节数组

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff;// 获取字节的低八位有效值
				String hexString = Integer.toHexString(i);// 将整数转为16进制

				if (hexString.length() < 2) {
					hexString = "0" + hexString;// 如果是1位的话,补0
				}

				sb.append(hexString);
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// 没有该算法时,抛出异常, 不会走到这里
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

//	public static void main(String[] args) {
//		String ss = "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";
//		byte[] b = null;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			b = decoder.decodeBuffer(ss);
//			FileOutputStream fos = new FileOutputStream("D:/sss.jpg");
//			fos.write(b);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		try {//C8653B32CB293FED9048A2F7FD822A28afokd深圳m105p298
////			String ss = ToolsUtil.encryptionMD5("C8653B32CB293FED9048A2F7FD822A28a105d98m深圳p2fok");
////			System.out.println(ss.toUpperCase());
////		} catch (NoSuchAlgorithmException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
////		String ss = "{\"organization\":\"101\",\"sequenceId\":\"11111111\",\"timeStamp\":\"20151212152700\",\"sign\":\"df5ba33065f8a8942ed76856e802d9f2\"}";
////		try {
////			String s = encryptionBase64(ss);
////			System.out.println(s);
////			String s1 = DecryptBase64(s);
////			System.out.println(s1);
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
////		String ss = "{\"retStatus\":\"1\",\"errCode\":null,\"errMsg\":\"\",\"result\": {\"uuid\":\"7A73408323BE456A9D38DD3BD4469B6F\",\"aa\": {\"dd\":\"7A73408323BE456A9D38DD3BD4469B6F\"}}}";
//		
//	}
}
