package com.dawn.bgSys.common;


import com.dawn.bgSys.exception.OperateFailureException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: qiangxiaolong 0031372
 * Date: 13-9-6
 * Time: 下午5:13
 */
public class TimeUtils {
    /**
     * 获取指定的季度的最后一天的日期
     */
    public static Timestamp getLastDateOfQuarter(int year, int quarter) {
//        checkYearAndQuarter(year, quarter);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * quarter);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 获取指定的季度的最后一天的日期
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
     * 获取指定的季度的第一天的日期
     */
    public static Timestamp getFirstDateOfQuarter(int year, int quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 3 * (quarter -1));
        calendar.set(Calendar.DATE, 1);
//        calendar.add(Calendar.DATE, -1);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * 时间格式
     */
    public static Timestamp format(Timestamp time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(time);
        date = date.substring(0, 10)+" 00:00:00";
        time = new Timestamp(sdf.parse(date).getTime());
        return time;
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

    /**
     * 获取本地对应时间
     * @return
     */
    public static Date getLocalTime() {
        int diffTime = getDiffTimeZoneRawOffset("Asia/Shanghai");
        long nowTime = System.currentTimeMillis();
        long newNowTime = nowTime - diffTime;
        Date date = new Date(newNowTime);
        return date;
    }

    public static Date getLocalTimeByGTM() {
        int diffTime = getDiffTimeZoneRawOffset("GMT+8");
        long nowTime = System.currentTimeMillis();
        long newNowTime = nowTime - diffTime;
        Date date = new Date(newNowTime);
        return date;
    }

    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getDefault().getRawOffset()
                - TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }


    /**
     * 获取月份值
     */
    public static String getMouth(SimpleDateFormat sdf,int data) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, data);
        return sdf.format(c.getTime());
    }

    /**
     * 获取给定时间的月首与月末时间
     */
     public static List<String> getDate(String date){
         try {
             List<String> list = new ArrayList<String>();
             Calendar calendar = Calendar.getInstance();
             DateFormat sdf = new SimpleDateFormat("yyyy-MM");
             calendar.setTime(sdf.parse(date));
             calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
             sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             list.add(sdf.format(calendar.getTime()));
             calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
             String dateStr = sdf.format(calendar.getTime());
             dateStr = dateStr.substring(0,10)+" 23:59:59";
             list.add(dateStr);
             return list;
         }catch (Exception e) {
             throw new OperateFailureException("请传入有效地日期！");
         }
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


}
