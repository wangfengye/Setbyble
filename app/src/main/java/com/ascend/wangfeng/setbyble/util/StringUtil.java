package com.ascend.wangfeng.setbyble.util;

import android.content.Context;

import com.ascend.wangfeng.setbyble.R;

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
    /**
     * @param label 配置参数
     * @return 对应中文
     */
    public static String toChinese(String label, Context c) {
        switch (label) {
            case "MAC":
                return c.getString(R.string.mac);
            case "DEV":
                return c.getString(R.string.dev);
            case "UPLD":
                return c.getString(R.string.upld);
            case "TVAL":
                return c.getString(R.string.tval);
            case "TCP":
                return c.getString(R.string.tcp);
            case "AMAC":
                return c.getString(R.string.amac);
            case "APHONE":
                return c.getString(R.string.aphone);
            default:
                return label;
        }
    }
}
