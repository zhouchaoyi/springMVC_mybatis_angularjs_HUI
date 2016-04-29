package com.dawn.bgSys.common;


import com.dawn.bgSys.common.date.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with malone
 * User: P0032597
 * Date: 13-9-6
 * Time: 下午3:19
 */
public class QuarterUtil {
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
     * 获取当前季度的前两个季度
     *
     * @return
     */
    public static List<Integer> getHistoryQuarter() {
        List<Integer> result = new ArrayList<Integer>();
        result.add(DateUtils.getPreviousQuarter());
        Calendar currCal = Calendar.getInstance();
        currCal.set(Calendar.MONTH, currCal.get(Calendar.MONTH) - 3);
        result.add(DateUtils.getPreviousQuarter(currCal));
        return result;
    }

}
