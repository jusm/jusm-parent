package core.com.chidori.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.com.chidori.model.common.CommonResponse;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class Utility {

    private static int debugCurrentTimeStamp = 0;

    public static int getCurrentTimeStamp() {
        if (debugCurrentTimeStamp == 0) {
            return (int) (System.currentTimeMillis() / 1000);
        } else {
            return debugCurrentTimeStamp;
        }
    }

    public static String encodeBase64String(String str) {
        return Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String encodeStr) {
        return Arrays.toString(Base64.decodeBase64(encodeStr.getBytes(StandardCharsets.UTF_8)));
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateGlobalOrder() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return (1 + String.format("%015d", hashCodeV));
    }

    public static <T> T convertJson(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CommonResponse getRightResponse(String msg, Object content) {
        CommonResponse res = new CommonResponse();
        res.setStatus(ErrorCode.SYS_SUCCESS);
        res.setMessage(msg);
        res.setContent(content);
        return res;
    }

    public static CommonResponse getFailResp(Object content) {
        return getFailResp(ErrorCode.SYS_FAIL, "fail", content);
    }

    public static CommonResponse getFailResp(int errorCode, String message, Object content) {
        CommonResponse resp = new CommonResponse();
        resp.setStatus(errorCode);
        resp.setMessage(message);
        resp.setContent(content);
        return resp;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return (!isBlank(str));
    }

    public static String encodeUtf8(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return url;
    }
}
