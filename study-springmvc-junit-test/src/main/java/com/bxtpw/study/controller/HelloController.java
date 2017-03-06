package com.bxtpw.study.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;

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

    @RequestMapping(value = "sayHello", method = {RequestMethod.POST, RequestMethod.GET})
    public String sayHello(String name, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("GetByRequest: " + request.getParameter("name"));
        if (name.length() < 1) {
            throw new NumberFormatException();
        }
        model.put("name", name);

        System.out.println("System: " + System.getProperty("user.dir"));

        File file = new File("../cmd/HelloWorld.java");
        System.out.println("File:HelloWorld.java: " + file.exists());


        return "hello";
    }
}
