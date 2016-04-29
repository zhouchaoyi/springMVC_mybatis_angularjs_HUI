package com.dawn.bgSys.controller;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.Utils;
import com.dawn.bgSys.common.consts.Consts;
import com.dawn.bgSys.exception.GenericException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * mvc 注解controller类的基类，用于一般注解controller的继承，非注解形式的controller不继承此类
 * 把异常控制模块提取到基类，
 * 使开发更加简洁，快速
 * Created with IntelliJ IDEA.
 * User: zhouchaoyi
 * Date: 15-09-23
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
@ControllerAdvice
public class BaseController {


    private Logger logger= Logger.getLogger(BaseController.class);

    /** 基于@ExceptionHandler异常处理 */
    @ExceptionHandler
    public void exp(HttpServletRequest request,HttpServletResponse response, Exception ex) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (ex instanceof GenericException) {
                GenericException e=(GenericException)ex;
                jsonObject.put("status", Utils.getSubStatus(false, e.getErrorCode(), e.getMessage()));
                jsonObject.put("data","");
                logger.error(e.getMessage(), e);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(jsonObject.toString());
            }else {
                jsonObject.put("status", Utils.getSubStatus(false, Consts.COMMON_ERROR_CODE,
                        Consts.COMMON_ERROR_MSG));
                jsonObject.put("data", "");
                logger.error(ex.getMessage(), ex);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(jsonObject.toString());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
