package com.bxtpw.study.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @time 2017/2/28 11:14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class HelloServiceTest {

    @Autowired
    private Set<HelloService> helloServiceList;

    @Test
    public void testHelloService() {
        System.out.println(helloServiceList.size());
        System.out.println("--------");
    }

}