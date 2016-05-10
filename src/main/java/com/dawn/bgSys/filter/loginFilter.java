package com.dawn.bgSys.filter;

import com.alibaba.fastjson.JSONObject;
import com.dawn.bgSys.common.*;
import com.dawn.bgSys.common.consts.Consts;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.exception.GenericException;
import com.dawn.bgSys.exception.OperateFailureException;
import com.dawn.bgSys.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Date: 13-9-26
 * Time: 下午1:38
 */
public class loginFilter extends BaseFilter {

    private Logger logger= Logger.getLogger(loginFilter.class);
    private static final String[] IGNORE_URI = {"/login.do"};

    private String appTK;

    public void doSelfFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        logger.info(">>>: " + url);
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        BodyReaderHttpServletRequestWrapper requestWrapper=null;
        if (!flag) {
            System.out.println("被拦截<<<<<<");
            try {
                requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
                String jsonStr = HttpHelper.getBodyString(requestWrapper);
                //System.out.println("jsonStr="+jsonStr+"<<<<<<<");
                String token = WebJsonUtils.getStringValue(jsonStr,"token",false);
                PropertiesUtil pUtil1 = new PropertiesUtil("application.properties");
                appTK = pUtil1.getKeyValue("APP_T_K");
                if(StringUtils.length(token)==0) {
                    throw new OperateFailureException("请先登录",Consts.TOKEN_ERROR_CODE);
                }
                //System.out.println("appTK="+appTK+"<<<<<<<");
                Map<String,Object> map= JWTUtils.verifierToken(token,appTK);
                if(null==map) {
                    throw new OperateFailureException("授权验证失败",Consts.TOKEN_ERROR_CODE);
                }else {
                    long exp=Long.valueOf(map.get("exp").toString());
                    //System.out.println("exp="+exp+"<<<<<");
                    long nowTime=new Date().getTime();
                    //System.out.println("nowTime="+nowTime+"<<<<<");
                    if(exp<nowTime) {
                        throw new OperateFailureException("登录已过期，请重新登录",Consts.TOKEN_ERROR_CODE);
                    }

                    int modifyFlag=Integer.valueOf(map.get("modifyFlag").toString());
                    String userId=map.get("userId").toString();
                    IUserService userService = (IUserService)context.getBean("userServiceImpl");
                    User user = userService.queryUserById(userId);
                    if(modifyFlag!=user.getModifyFlag()) {
                        throw new OperateFailureException("登录名密码错误或登录已过期，请重新登录",Consts.TOKEN_ERROR_CODE);
                    }
                }
            } catch (Exception ex) {
                JSONObject jsonObject = new JSONObject();
                if (ex instanceof GenericException) {
                    GenericException e=(GenericException)ex;
                    jsonObject.put("status", Utils.getSubStatus(false, e.getErrorCode(), e.getMessage()));
                    jsonObject.put("data","");
                    logger.error(e.getMessage(), e);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(jsonObject.toString());
                    return;
                }else {
                    jsonObject.put("status", Utils.getSubStatus(false, Consts.COMMON_ERROR_CODE,
                            Consts.COMMON_ERROR_MSG));
                    jsonObject.put("data", "");
                    logger.error(ex.getMessage(), ex);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(jsonObject.toString());
                    return;
                }
            }
        }

        if(null==requestWrapper) {
            filterChain.doFilter(request, response);
        }else {
            filterChain.doFilter(requestWrapper, response);
        }
    }
}
