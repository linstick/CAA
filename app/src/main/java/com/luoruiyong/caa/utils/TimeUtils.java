package com.luoruiyong.caa.utils;

import com.luoruiyong.caa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: luoruiyong
 * Date: 2019/4/18/018
 * Description:
 **/
public class TimeUtils {

    private static final SimpleDateFormat COMPLETE_FORMAT = new SimpleDateFormat("yyyy-M-dd HH:mm");
    private static final SimpleDateFormat YEAR_EXCEPT_FORMAT = new SimpleDateFormat("M-dd HH:mm");
    private static final SimpleDateFormat DATE_EXCEPT_FORMAT = new SimpleDateFormat("HH:mm");

    public static Date stringToDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long stringToLong(String str) {
        Date date = stringToDate(str);
        return date != null ? date.getTime() : 0;
    }

    /**
     * 时间格式化
     * @param str 字符串时间
     * @return
     */
    public static String format(String str) {
        Date date = stringToDate(str);
        long time = date.getTime();
        SimpleDateFormat sdf;
        if (isSameYear(date)) {
            //同一年 显示MM-dd HH:mm
            sdf = DATE_EXCEPT_FORMAT;
            if (isSameDay(date)) {
                int minute = minutesAgo(time);
                if (minute < 60) {
                    //1小时之内 显示n分钟前
                    if (minute <= 1) {
                        //一分钟之内，显示刚刚
                        return ResourcesUtils.getString(R.string.time_just_now);
                    } else {
                        return String.format(ResourcesUtils.getString(R.string.time_minutes_ago), minute);
                    }
                }
                return sdf.format(date);
            }
            if (isYesterday(date)){
                return String.format(ResourcesUtils.getString(R.string.time_yesterday), sdf.format(date));
            }
            if (isTheDayBeforeYesterday(date)) {
                return String.format(ResourcesUtils.getString(R.string.time_the_day_before_yesterday), sdf.format(date));
            }
            //同一年 显示MM-dd HH:mm
            sdf = YEAR_EXCEPT_FORMAT;
            return sdf.format(date);
        }
        //不是同一年 显示完整日期yyyy-MM-dd HH:mm
        sdf = COMPLETE_FORMAT;
        return sdf.format(date);
    }

    /**
     * 几分钟前
     *
     * @param time
     * @return
     */
    public static int minutesAgo(long time) {
        return (int) ((System.currentTimeMillis() - time) / (60000));
    }

    /**
     * 是否是当前时间的昨天
     * 获取指定时间的后一天的日期，判断与当前日期是否是同一天
     *
     * @param date
     * @return
     */
    public static boolean isYesterday(Date date) {
        Date yesterday = getNextDay(date, 1);
        return isSameDay(yesterday);
    }

    public static boolean isTheDayBeforeYesterday(Date date) {
        Date yesterday = getNextDay(date, 2);
        return isSameDay(yesterday);
    }

    /**
     * 判断与当前日期是否是同一天
     *
     * @param date
     * @return
     */
    public static boolean isSameDay(Date date) {
        return isEquals(date, "yyyy-MM-dd");
    }

    /**
     * 判断与当前日期是否是同一月
     *
     * @param date
     * @return
     */
    public static boolean isSameMonth(Date date) {
        return isEquals(date, "yyyy-MM");
    }

    /**
     * 判断与当前日期是否是同一年
     *
     * @param date
     * @return
     */
    public static boolean isSameYear(Date date) {
        return isEquals(date, "yyyy");
    }


    /**
     * 格式化Date，判断是否相等
     *
     * @param date
     * @return 是返回true，不是返回false
     */
    private static boolean isEquals(Date date, String format) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
    }

    /**
     * 获取某个date第n天的日期
     * n<0 表示前n天
     * n=0 表示当天
     * n>1 表示后n天
     *
     * @param date
     * @param n
     * @return
     */
    public static Date getNextDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        date = calendar.getTime();
        return date;
    }
}

