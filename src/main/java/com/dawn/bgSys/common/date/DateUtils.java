package com.dawn.bgSys.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 14-5-15
 * Time: 下午2:11
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {

    private static ThreadLocal<SimpleDateFormat> sdfLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 获取SimpleDateFormat线程安全
     * @param formatPattern
     * @return
     */
    public static SimpleDateFormat getSdf (String formatPattern) {
        SimpleDateFormat sdf = sdfLocal.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat(formatPattern);
            sdfLocal.set(sdf);
        }
        return sdf;
    }

    /**
     * 格式化日期（线程安全）
     * @param formatPattern
     * @param date
     * @return
     */
    public static String formatDate(String formatPattern, Date date) {
        return getSdf(formatPattern).format(date);
    }

    /**
     * 把字符串转换成日期（线程安全）
     * @param formatPattern
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String formatPattern, String dateStr) throws ParseException {
        return getSdf(formatPattern).parse(dateStr);
    }

    /**
     * 根据当前时间获取当前季度（返回格式为yyyyqq)
     * @return  季度
     */
    public static int getCurrentQuarter() {
        Calendar currCal = Calendar.getInstance();
        int monthOfYear = currCal.get(Calendar.MONTH) + 1;
        int year = currCal.get(Calendar.YEAR);
        if (monthOfYear >= 1 && monthOfYear <= 3) {
            return year * 100 + 1;
        }
        if (monthOfYear >= 4 && monthOfYear <= 6) {
            return year * 100 + 2;
        }
        if (monthOfYear >= 7 && monthOfYear <= 9) {
            return year * 100 + 3;
        }
        if (monthOfYear >= 10 && monthOfYear <= 12) {
            return year * 100 + 4;
        }
        return -1;
    }

    /**
     * 获取当前时间所在季度的上一季度
     * @return  季度（格式yyyyqq)
     */
    public static int getPreviousQuarter() {
        Calendar currCal = Calendar.getInstance();
        int monthOfYear = currCal.get(Calendar.MONTH) + 1;
        if (monthOfYear >= 1 && monthOfYear <= 3) {
            int year = currCal.get(Calendar.YEAR);
            return (year - 1)* 100 + 4;
        } else {
            return getCurrentQuarter() - 1;
        }
    }

    /**
     *  获取当前时间的季度的前一季度
     * @param currCal
     * @return
     */
    public static int getPreviousQuarter(Calendar currCal) {
        int monthOfYear = currCal.get(Calendar.MONTH) + 1;
        if (monthOfYear >= 1 && monthOfYear <= 3) {
            int year = currCal.get(Calendar.YEAR);
            return (year - 1)* 100 + 4;
        } else {
            return getCurrentQuarter() - 1;
        }
    }

    /**
     * 获取指定的季度的第一天的日期
     */
    public static Date getFirstDateOfQuarter(int year, int quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * (quarter -1));
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定的季度的最后一天的日期
     */
    public static Date getLastDateOfQuarter(int year, int quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * quarter);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取当前季度的第一天的日期
     */
    public static String getFirstDateOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = new Date();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int quater = (month + 2) / 3;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(getFirstDateOfQuarter(year, quater));
    }

    /**
     * 获取当前季度的最后一天的日期
     */
    public static String getLastDateOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int quater = (month + 2) / 3;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(getLastDateOfQuarter(year, quater));
    }

    /**
     * 获得日期，在给定的日期上增加day（如果减去则day为负数）
     * @param date 给定日期
     * @param day 改变天数
     * @return 计算后的日期
     */
    public static Date getDay(Date date, int day) {
        long currDateMills = date.getTime();
        long extraMills = day * 3600 * 1000 * 24;
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(currDateMills + extraMills);
        return result.getTime();
    }

}
