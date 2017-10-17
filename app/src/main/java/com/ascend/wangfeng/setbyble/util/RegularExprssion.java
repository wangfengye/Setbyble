package com.ascend.wangfeng.setbyble.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengye on 2016/10/26.
 * email 1040441325@qq.com
 * 正则表达式
 */
public class RegularExprssion {
    public static boolean isUrl(String url){
        String regEx="(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+"
                + "[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))"
                +"+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\\'\"."
                +",<>?«»“”‘’]))";
        Pattern pat=Pattern.compile(regEx);
        Matcher mat=pat.matcher(url);
        boolean rs=mat.find();
        return rs;
    }
    public static  boolean isIp(String ip){
        String regEx="\\d+\\.\\d+\\.\\d+\\.\\d+";
        Pattern pat=Pattern.compile(regEx);
        Matcher mat=pat.matcher(ip);
        boolean hasnotSpace=!ip.contains(" ");

        return mat.find()&&hasnotSpace;
    }

    /**
     * 匹配搜索框的正则表达式
     * @param mac
     * @param regEx
     * @return
     */
    public static boolean isChoose(String mac,String regEx){
        boolean rs=false;
        if (mac.toLowerCase().contains(regEx.toLowerCase())){
            rs=true;
        }
        return rs;
    }

    public static  boolean isMac(String mac){
        if (mac.length()!=12)return false;
        String regEx="([0-9A-Fa-f]{2}){6}";
        Pattern pat=Pattern.compile(regEx);
        Matcher mat=pat.matcher(mac);
        boolean rs=mat.find();
        return rs;
    }

    public static boolean isPhone(String value) {
        if (value.length()==11){
        return true;}return false;
    }
}
