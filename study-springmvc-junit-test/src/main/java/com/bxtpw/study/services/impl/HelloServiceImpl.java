package com.bxtpw.study.services.impl;

import com.bxtpw.study.annotation.LogAnn;
import com.bxtpw.study.services.HelloService;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/29 10:37
 * @since 0.1
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static int instanceCount = 0;

    public HelloServiceImpl() {
        System.out.println("HelloServiceImpl 初始化(" + ++instanceCount + ").....................");
    }

    @Override
    @LogAnn(value = "DEBUG")
    public String getWelcome(String name) {
        return "Hello, " + name + "! Welcome to China!";
    }
}
