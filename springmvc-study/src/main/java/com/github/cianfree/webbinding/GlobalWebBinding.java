package com.github.cianfree.webbinding;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Arvin
 * @time 2016/11/15 18:13
 */
public class GlobalWebBinding extends ConfigurableWebBindingInitializer {

    public GlobalWebBinding() {
        super();
        System.out.println("init GlobalWebBinding");
    }

    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        super.initBinder(binder, request);
        // 定义自己的类型转换

        System.out.println("Register customer editor!");

        binder.registerCustomEditor(Item.class, new ItemEditor());
    }
}
