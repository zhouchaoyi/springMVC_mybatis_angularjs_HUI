package com.dawn.bgSys.interceptor;

import com.dawn.bgSys.common.BodyReaderHttpServletRequestWrapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

/**
 * Created by zhouchaoyi on 2016/5/4.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String[] IGNORE_URI = {"/login"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        System.out.println(">>>: " + url);
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println("被拦截<<<<<<");
            //T_supplier_user user = LoginController.getLoginUser(request);
            //if (user != null) flag = true;
            flag = true;
        }
        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        request=requestWrapper;
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
