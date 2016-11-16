package com.github.cianfree.autoinject.byname;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @time 2016/11/15 19:03
 */
@Controller
@RequestMapping("/auto/inject")
public class AutoInjectController {

    @RequestMapping("currentUser")
    @ResponseBody
    public String currentUser(HttpServletRequest request, String currentUser, Integer age) {
        System.out.println(request.getClass());
        System.out.println("Age: " + age);
        System.out.println("currentUser: " + currentUser);
        return currentUser;
    }
}
