package com.bxtpw.study.aspect;

import com.bxtpw.study.services.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by DW on 2017/3/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-aspect.xml"})
public class LogAnnAspectTest {

    @Autowired
    private HelloService helloService;

    @Test
    public void testLogAnn() throws Exception {

        System.out.println(helloService.getWelcome("Arvin"));

    }
}