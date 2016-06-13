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

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Date: 13-9-26
 * Time: 下午1:38
 */
//暂时没有用到<<<<<<<<<<<<<<<<<<<<<
public class PermFilter extends BaseFilter {

    private Logger logger= Logger.getLogger(PermFilter.class);
    private static final String[] IGNORE_URI = {"index.html","login.html","template/console/"};

    private String appTK;

    public void doSelfFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean flag = false;
        String url = request.getServletPath();

        logger.info(">>>: " + url);
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        Cookie[] aCookie = request.getCookies();
        String token="";
        for(int i=0;i<aCookie.length;i++) {
            Cookie cookie = aCookie[i];
            if(StringUtils.equals("token",cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (!flag && StringUtils.length(token)>0) {
            System.out.println("html被拦截<<<<<<");
            try {
                PropertiesUtil pUtil1 = new PropertiesUtil("application.properties");
                appTK = pUtil1.getKeyValue("APP_T_K");
                Map<String,Object> map= JWTUtils.verifierToken(token,appTK);
                String userId=map.get("userId").toString();
                System.out.println("userId="+userId+"<<<<<");
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

        filterChain.doFilter(request, response);

        //使html页面不在浏览器缓存
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
    }

}
