package com.blife.blife_app.tools;

import com.blife.blife_app.utils.util.DateFormatUtils;

/**
 * Created by Somebody on 2016/9/5.
 */
public class DateUtils {

    public static synchronized String getTime(String time) {
        try {
            String timeHStr = DateFormatUtils.getTimeHStr(Long.parseLong(time.trim()) * 1000, "yyyy-MM-dd HH:mm");
            return timeHStr.toString().trim();
        } catch (Exception e) {
            return "时间信息获取错误";
        }

    }
}
