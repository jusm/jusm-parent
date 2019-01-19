package com.github.jusm.signature.util;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * String字符串工具类
 * @author lyj 2015年12月15日
 *
 */
public class StringUtil {
	/**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.trim().equals(""))
			return true;
		return false;
	}

	/**
	 * 判断日期是否为NULL
	 * @param date
	 * @return
	 */
	public static boolean isDateEmpty(Date date){
		if(date == null)
			return true;
		return false;
	}
	/**
	 * 将字符串按逗号分開成數組
	 */
	public static String[] strToArray(String str) {
		  StringTokenizer st = new StringTokenizer(str, ",");
		  String[] strArray = new String[st.countTokens()];
		  int strLeng = st.countTokens();
		  for (int i=0; i<strLeng; i++) {
			  strArray[i] = st.nextToken();
		  }
		  return strArray;
	}
	
	/**
	 * 判断字符串是否手机号，如果是，做转换如下：15825632045  --  158****2045
	 *
	 * @param str
	 * @return
	 */
	public static String encryptPhoneNumber(String str) {
		String retNum = "";
		String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
		if (str != null && str != "")
		{
			if(Pattern.matches(REGEX_MOBILE, str))
			{
				retNum = str.substring(0, 3) + "****" + str.substring(7, 11);
			}
			else
			{
				retNum = str;
			}
		}
		return retNum;
	}
	
	/**
	 * 对一MD5加密串只截取32位之后的字符串
	 *
	 * @param str
	 * @return
	 */
	public static String subMD5Str(String str) {
		String subStr = "0";
		if(str.length() > 32)
		{
			subStr = str.substring(32, str.length());
		}
		return subStr;
	}
	
 
}
