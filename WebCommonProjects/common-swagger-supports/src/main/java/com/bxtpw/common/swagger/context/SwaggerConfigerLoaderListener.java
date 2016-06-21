package com.bxtpw.common.swagger.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Swagger加载配置
 */
public class SwaggerConfigerLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute("apiTitle", getInitParam(context, "apiTitle", "Api Docs"));
        context.setAttribute("apiDesc", getInitParam(context, "apiDesc", ""));
        context.setAttribute("apiUrl", getInitParam(context, "apiUrl", "/api-docs"));
    }

    /**
     * 获取初始化参数
     *
     * @param name     参数名
     * @param defValue 默认值
     * @return 返回初始化参数
     */
    private String getInitParam(ServletContext context, String name, String defValue) {
        String value = context.getInitParameter(name);
        if (null == value || "".equals(value.trim())) {
            return defValue;
        }
        return value;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
