package com.github.jusm.validation;
import java.util.regex.Pattern;
 
/**
 * 校验器：利用正则表达式校验邮箱、手机号等
 * 
 * 
 */
public class RegexValidator {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";
    
//    要求：
//
//    密码长度最少8位
//    大写字母，小写字母，数字，特殊符号必须四选三
//    先给出表达式：
//    ^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,}$
//
//    首先给出全部四种的匹配表达式：
//    [a-zA-Z0-9\W_]{8,}
//    其中\W 匹配任意不是字母，数字，下划线，汉字的字符
//
//    然后使用排除法，排除其中只选一种或只选两种的情况，剩下的就是选了三种的情况了。
//    有：
//    大写+小写，大写+数字，大写+特殊符号，小写+数字，小写+特殊符号，数字+特殊符号。共这6种组合。
//    然后用环视，正向否定匹配 (?!...)组合起来，就得到最终需要的表达式
//    ^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,}$
 
    public static final String REGEX_COMPLEX_PASSWORD = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,}$";
    
    
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";
 
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|17[078]|14[57]|166|199|(15[^4,\\D])|(18[0,1,5-9]))\\d{8}$";
    
    
    public static final String REGEX_ROUGH_MOBILE = "^(1)\\d{10}$";
 
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
 
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
 
    /**
     * 正则表达式：验证URL
     */
//    public static final String REGEX_URL = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
    public static final String REGEX_URL =   "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
//    public static final String REGEX_URL =   "^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/");
    
    public static void main(String[] args) {
        String username = "https://i.ky-express.com/webapp/queryWaybill?orderID=75503217813";
        username = "https://172.3.6.5/webapp/queryWaybill?orderID=75503217813";
        username = "http://mp.weixin.qq.com/s?__biz=MzA3NzA4OTEzOQ==&mid=2661915158&idx=4&sn=fad679dbe09ed4bcc9a98da5a4a40197&chksm=840a6a0ab37de31c3434d52769af8758f17fe112ad5e5fa4011635426028b41eb14c9b2f7331&mpshare=1&scene=23&srcid=0116jNsOAhHXKsOTVrIv2CIa#rd";
        System.out.println(RegexValidator.isUrl(username));
    }
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
 
    /**
     * 校验用户名
     * 
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }
 
    /**
     * 校验密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }
    
    /**
     * 校验复杂密码
     * 
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isComplexPassword(String password) {
        return Pattern.matches(REGEX_COMPLEX_PASSWORD, password);
    }
 
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_ROUGH_MOBILE, mobile);
    }
 
    /**
     * 校验邮箱
     * 
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
 
    /**
     * 校验汉字
     * 
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }
 
    /**
     * 校验身份证
     * 
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
 
    /**
     * 校验URL
     * 
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
 
    /**
     * 校验IP地址
     * 
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
}