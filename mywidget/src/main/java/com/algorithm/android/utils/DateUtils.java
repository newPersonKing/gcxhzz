package com.algorithm.android.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by algorithm on 2017/11/14.
 */

public class DateUtils {

    /**
     * DateFormat缓存
     */
    private static Map<String, DateFormat> dateFormatMap = new HashMap<>();

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";// 2017-10-12 09:00
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";// 2017-10-12 09:00:00
    public static final String yyyy_MM = "yyyy-MM";

    /**
     * 根据时间戳 获取指定格式的日期
     *
     * @return
     */
    public static String getDate(long stamp, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(stamp));
    }

    /**
     * 获取DateFormat
     *
     * @param formatStr
     * @return
     */
    public static DateFormat getDateFormat(String formatStr) {
        DateFormat df = dateFormatMap.get(formatStr);
        if (df == null) {
            df = new SimpleDateFormat(formatStr);
            dateFormatMap.put(formatStr, df);
        }
        return df;
    }

    // 返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    // 获取某一天的星期
    public static String getWeekSomeDay(Date date) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        String week;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {

            case Calendar.SUNDAY:
                week = "周日";
                break;

            case Calendar.MONDAY:
                week = "周一";
                break;
            case Calendar.TUESDAY:
                week = "周二";
                break;

            case Calendar.WEDNESDAY:
                week = "周三";
                break;

            case Calendar.THURSDAY:
                week = "周四";
                break;

            case Calendar.FRIDAY:
                week = "周五";
                break;

            case Calendar.SATURDAY:
                week = "周六";
                break;

            default:
                week = "";
                break;
        }

        return week;
    }

    //获取本年的结束时间
    public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(Calendar.YEAR));
    }

    public static Integer getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(Calendar.MONTH));
    }

    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 000);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 000);
        return new Timestamp(calendar.getTimeInMillis());

    }

    // 获取某年某个月份的结束时间
    public static Date getEndDayOfSomeMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDayEndTime(cal.getTime());
    }

    // 获取某个月份的结束时间
    public static Date getEndDayOfNowMonth(int add) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, getNowMonth() + add);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDayEndTime(cal.getTime());
    }

    // 获取某个月份的结束时间
    public static Date getEndDayOfSomeMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDayEndTime(cal.getTime());
    }

    /**
     * 获取当前月份的第一天
     * @return
     */
    public static Date getStartDayOfNowMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, getNowMonth());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStartTime(cal.getTime());
    }


}
