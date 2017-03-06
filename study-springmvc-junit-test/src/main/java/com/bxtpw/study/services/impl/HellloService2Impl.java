package com.bxtpw.study.services.impl;

import com.bxtpw.study.services.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author Arvin
 * @time 2017/2/28 11:17
 */
@Service
public class HellloService2Impl implements HelloService {
    @Override
    public String getWelcome(String name) {
        return "Hello2, " + name;
    }
}
