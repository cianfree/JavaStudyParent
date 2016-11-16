package com.github.cianfree.autoinject.byname;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Arvin
 * @time 2016/11/15 19:00
 */
public class AutoInjectHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public AutoInjectHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (null != values && values.length > 0) {
            return values[0];
        }
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        System.out.println("Use customer http request wrapper!");

        if ("currentUser".equals(name)) {
            return new String[]{"Arvin"};
        }

        if ("age".equals(name)) {
            return new String[]{"25"};
        }

        if ("item".equals(name)) {
            return new String[]{"arvin:hello"};
        }

        return super.getParameterValues(name);
    }

}
