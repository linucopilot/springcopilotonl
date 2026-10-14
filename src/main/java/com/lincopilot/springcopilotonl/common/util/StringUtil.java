package com.lincopilot.springcopilotonl.common.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isEquals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean urlMatchesWithRegEx(String url, String pattern) {
        if (pattern.indexOf("/**") > -1){
            pattern = pattern.replace("/**", "/(.|\\s)*\\S(.|\\s)*");
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(url);
        return matcher.find();
    }
}
