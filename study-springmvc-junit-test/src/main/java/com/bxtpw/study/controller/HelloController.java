package com.bxtpw.study.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/29 21:31
 * @since 0.1
 */
@Controller
public class HelloController {

    @RequestMapping(value = "sayHello", method = RequestMethod.POST)
    public String sayHello(String name, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("GetByRequest: " + request.getParameter("name"));
        if (name.length() < 1) {
            throw new NumberFormatException();
        }
        model.put("name", name);
        return "hello";
    }
}
