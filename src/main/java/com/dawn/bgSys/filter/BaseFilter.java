package com.dawn.bgSys.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 13-10-21
 * Time: 下午4:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        doSelfFilter(request, response, filterChain);
    }

    public void destroy() {
    }

    /**
     * 实现自己的filter逻辑
     * @param request
     * @param response
     */
    public abstract void doSelfFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException;

}
