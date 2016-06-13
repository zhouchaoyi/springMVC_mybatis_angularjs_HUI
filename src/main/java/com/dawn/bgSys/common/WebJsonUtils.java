package com.dawn.bgSys.common;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.password.Base64Utils;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.exception.ParamCheckException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: zhouchaoyi
 * Date: 15-09-23
 * Time: 下午4:54
 * To change this template use File | Settings | File Templates.
 */
public class WebJsonUtils {


    /**
     * 获取int值
     * @param json
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static int getIntValue(JSONObject json, String paramName, int defaultValue, boolean notNull)
            throws ParamCheckException {
        return getValue(json, paramName, defaultValue, notNull);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(JSONObject json, String paramName, T defaultValue, boolean notNull)
            throws ParamCheckException {
        String paramValue = null;
        try {
            if(!notNull&&!json.containsKey(paramName)) { //非必须字段且json中不包含paramName
                paramValue=null;
            }else {
                paramValue = json.getString(paramName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ParamCheckException("请传递参数：" + paramName);
        }
        if (paramValue != null) {
            //过滤Scripting_Attack, Stored_XSS攻击
            //paramValue = new HTMLFilter().filter(paramValue);
            //过滤sql注入
            //paramValue = BaseDaoImpl.TransactSQLInjection(paramValue);
        }
        checkValue(paramName, paramValue, notNull);
        if (paramValue != null) {
            if (defaultValue instanceof Integer) {
                isInteger(paramValue);
                return (T)(Integer.valueOf(paramValue));
            }
            if (defaultValue instanceof Double) {
                isDouble(paramValue);
                return (T)(Double.valueOf(paramValue));
            }
            if (defaultValue instanceof Long) {
                isLong(paramValue);
                return (T)(Long.valueOf(paramValue));
            }
            if (defaultValue instanceof Float) {
                isFloat(paramValue);
                return (T)(Float.valueOf(paramValue));
            }
            if (defaultValue instanceof String) {
                return (T)(paramValue);
            }
        }
        return defaultValue;
    }

    /**
     * 辅助方法
     * @param paramValue
     * @param notNull
     */
    private static void checkValue(String paramName, String paramValue, boolean notNull) throws ParamCheckException {
        if (notNull && paramValue == null) {
            throw new ParamCheckException("请传递参数：" + paramName);
        }
    }

    /**
     * 获取int值
     * @param json
     * @param paramName
     * @return
     */
    public static int getIntValue(JSONObject json, String paramName) throws ParamCheckException {
        return getIntValue(json, paramName, -1, false);
    }

    /**
     * 获取int值
     * @param json
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static int getIntValue(JSONObject json, String paramName, int defaultValue) throws ParamCheckException {
        return getIntValue(json, paramName, defaultValue, false);
    }

    /**
     * 获取int值
     * @param json
     * @param paramName
     * @param notNull
     * @return
     */
    public static int getIntValue(JSONObject json, String paramName, boolean notNull) throws ParamCheckException {
        return getIntValue(json, paramName, -1, notNull);
    }

    public static int getIntValue(String jsonStr, String paramName, boolean notNull) throws ParamCheckException {
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(jsonStr);
        } catch (JSONException e) {
            throw new OperateFailureException("请传入JSON格式参数");
        }
        return getIntValue(jsonObject, paramName, -1, notNull);
    }

    /**
     * 返回字符串，默认返回""
     * @param json
     * @param paramName
     * @return
     */
    public static String getStringValue(JSONObject json, String paramName) throws ParamCheckException {
        return getStringValue(json, paramName, false);
    }

    /**
     * 获取String值
     * @param json
     * @param paramName
     * @param notNull
     * @return
     */
    public static String getStringValue(JSONObject json, String paramName, boolean notNull)
            throws ParamCheckException {
        return getValue(json, paramName, "", notNull);
    }

    public static String getStringValue(String jsonStr, String paramName,boolean notNull)
            throws ParamCheckException {
        if(null==jsonStr|| StringUtils.equals("",jsonStr)) {
            throw new ParamCheckException("请传入JSON格式参数");
        }
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(jsonStr);
        } catch (JSONException e) {
            throw new ParamCheckException("请传入JSON格式参数");
        }
        return getValue(jsonObject, paramName, "", notNull);
    }

    /**
     * 获取double值
     * @param json
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static double getDoubleValue(JSONObject json, String paramName, double defaultValue)
            throws ParamCheckException {
        return getDoubleValue(json, paramName, defaultValue, false);
    }

    /**
     * 获取double值
     * @param json
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static double getDoubleValue(JSONObject json, String paramName, double defaultValue, boolean notNull)
            throws ParamCheckException {
        return getValue(json, paramName, defaultValue, notNull);
    }

    public static double getDoubleValue(JSONObject json, String paramName,  boolean notNull)
            throws ParamCheckException {
        return getDoubleValue(json, paramName, -1, notNull);
    }

    /**
     * 获取double值
     * @param json
     * @param paramName
     * @return
     */
    public static double getDoubleValue(JSONObject json, String paramName)
            throws ParamCheckException {
        return getDoubleValue(json, paramName, 0);
    }

    /**
     * 获取boolean值
     * @param jsonStr
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static String getBooleanValue(String jsonStr, String paramName, String defaultValue)
            throws ParamCheckException {
        return getBooleanValue(jsonStr, paramName, defaultValue, false);
    }

    /**
     * 获取boolean值
     * @param jsonStr
     * @param paramName
     * @param defaultValue
     * @param notNull
     * @return
     */
    public static String getBooleanValue(String jsonStr, String paramName, String defaultValue, boolean notNull)
            throws ParamCheckException {
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(jsonStr);
        } catch (JSONException e) {
            throw new ParamCheckException("请传入JSON格式参数");
        }
        String paramValue = null;
        try {
            paramValue = jsonObject.getString(paramName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkValue(paramName, paramValue, notNull);
        if (paramValue == null) {
            return defaultValue;
        }
        if (paramValue.equals("true")) {
            return "1";
        }
        if (paramValue.equals("false")) {
            return "0";
        }
        if (paramValue.equals("1")) {
            return "1";
        }
        if (paramValue.equals("0")) {
            return "0";
        }
        return "0";
    }

    public static Long getLongValue(JSONObject json, String paramName, long defaultValue, boolean notNull)
            throws ParamCheckException {
        return getValue(json, paramName, defaultValue, notNull);
    }

    /**
     * 获取boolean值
     * @param jsonStr
     * @param paramName
     * @return
     */
    public static String getBooleanValue(String jsonStr, String paramName) throws ParamCheckException {
        return getBooleanValue(jsonStr, paramName, "0");
    }

    /**
     * 对base64加密的字符串进行解码
     * @param str
     * @return
     */
    public static String decodeFromBase64(String str) throws UnsupportedEncodingException {
        if (str == null) {
            throw new UnsupportedEncodingException();
        }
        return new String(Base64Utils.decode(str), "UTF-8");
    }

    /**
     *  验证字符串长度
     * @param str
     * @param length
     * @throws OperateFailureException
     */
    public static void  checkStrLength(String str, int length) throws OperateFailureException{
        if (str == null) {
            throw new OperateFailureException("字符串不能为空！");
        }
        if (str.length() > length) {
            throw new OperateFailureException("字符串长度不能超过" + length);
        }
    }

    /**
     *  验证字符串是否是整形数字
     * @param str
     */
    public static void isInteger(String str) {
        try {
            Integer.valueOf(str);
        } catch (NumberFormatException e) {
            throw new OperateFailureException("该字符串应该为整形数字！");
        }
    }

    /**
     *  验证字符串是否是浮点型数字
     * @param str
     */
    public static void isDouble(String str) {
        try {
            Double.valueOf(str);
        } catch (NumberFormatException e) {
            throw new OperateFailureException("该字符串应该为浮点型数字！");
        }
    }

    /**
     *  判断字符串是否为长整形数字
     * @param str
     */
    public static void isLong(String str) {
        try {
            Long.valueOf(str);
        } catch (NumberFormatException e) {
            throw new OperateFailureException("该字符串应该为长整形数字！");
        }
    }

    /**
     * 浮点型数字
     * @param str
     */
    public static void isFloat(String str) {
        try {
            Float.valueOf(str);
        } catch (NumberFormatException e) {
            throw new OperateFailureException("该字符串应该为浮点型数字！");
        }
    }

    /**
     * 将接收的json字符串转换成jsonObject
     */
//    public static JSONObject getJSONObject(HttpServletRequest request) {
//        StringBuffer jb = new StringBuffer();
//        String line = null;
//        try {
//            BufferedReader reader = request.getReader();
//            while ((line = reader.readLine()) != null)
//                jb.append(line);
//            JSONObject jsonObject = new JSONObject(jb.toString());
//        } catch (Exception e) { //report an error }
//            throw new OperateFailureException("该字符串应该为浮点型数字！");
//        }
//
//    }

}
