package com.ascend.wangfeng.setbyble.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fengye on 2017/10/16.
 * email 1040441325@qq.com
 */

public class TimeUtil {
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
