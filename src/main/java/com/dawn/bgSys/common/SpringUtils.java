package com.dawn.bgSys.common;

import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: malone
 * Date: 14-5-15
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
public class SpringUtils {

    /**
     * 在此工具类中缓存spring的容器，并在servletContextListener启动的时候初始化容器
     */
    private static ApplicationContext context;

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext () {
        return context;
    }

}
