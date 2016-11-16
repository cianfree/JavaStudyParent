package com.github.cianfree.autoinject.bytype;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Arvin
 * @time 2016/11/16 10:25
 */
@Controller
@RequestMapping("/auto/inject/bytype")
public class AutoInjectByTypeController {

    @RequestMapping("mytype")
    @ResponseBody
    public MyType mytype(HttpSession session, MyType myType, ApplicationContext applicationContext) {
        System.out.println("session: " + session);
        System.out.println("MyType: " + myType);
        System.out.println("ApplicationContext： " + applicationContext);
        return myType;
    }
}