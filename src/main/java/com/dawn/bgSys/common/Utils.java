package com.dawn.bgSys.common;


import com.dawn.bgSys.common.consts.Consts;
import com.dawn.bgSys.exception.OperateFailureException;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 13-10-21
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    /**
     * 获取返回状态map
     * @return
     */
    public static Map<String, String> getSubStatus(Boolean success, String errorCode, String errorMsg) {
        Map<String, String> subStatus = new HashMap<String, String>();
        subStatus.put("success", success.toString());
        subStatus.put("errorCode", errorCode);
        subStatus.put("errorMsg", errorMsg);
        return subStatus;
    }

    /**
     * 获取返回状态map
     * @return
     */
    public static Map<String, String> getSubStatus(String errorMsg) {
        return getSubStatus(true, Consts.COMMON_SUCCESS_CODE, errorMsg);
    }

    /**
     * 获取返回状态map
     * @return
     */
    public static Map<String, String> getSubStatus(String errorCode, String errorMsg) {
        return getSubStatus(true, errorCode, errorMsg);
    }

    //集合转换成字符串，以逗号隔开
    public static <T> String collectionToStr (Collection<T> collection) {
        StringBuilder result = new StringBuilder("");
        if (collection == null) {
            return result.toString();
        }
        T[] array = (T[])collection.toArray();
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i != array.length -1) {
                result.append(",");
            }
        }
        return result.toString();
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
     * 生成六位随机数字
     * @return
     */
    public static String randomAccessSixNum () {
        return String.valueOf(nextInt(100000, 999999));
    }

    /**
     * 生成指定范围内的数字
     * @param min
     * @param max
     * @return
     */
    public static int nextInt(final int min, final int max) {
        Random rand = null;
        try {
            rand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new OperateFailureException("生成随机数字出错！");
        }
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
    }

    public static String transOrderByStr(String orderBy) {
        String result="";
        if(StringUtils.contains(orderBy,",")) {
            String[] aStr=orderBy.split(",");
            if(aStr.length>1) {
                if(StringUtils.equals("1",aStr[1].trim())) {
                    result=CamelCaseUtils.toUnderlineName(aStr[0]);
                }else if(StringUtils.equals("-1",aStr[1].trim())) {
                    result=CamelCaseUtils.toUnderlineName(aStr[0])+" desc";
                }else {
                    return "";
                }
            }else {
                return "";
            }
        }else {
            return "";
        }
        return result;
    }

}
