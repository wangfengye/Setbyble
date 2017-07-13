package com.ascend.wangfeng.setbyble.util;

/**
 * Created by fengye on 2017/6/23.
 * email 1040441325@qq.com
 */

public class StringUtil {
    public static int countStr(String str1, String str2) {
        String tag = str1;
        int count = 0;
        while (tag.indexOf(str2) != -1) {
            count++;
            tag =tag.substring(0,tag.indexOf(str2))+tag.substring(tag.indexOf(str2)+str2.length(),tag.length());
        }
        return count;
    }
}
