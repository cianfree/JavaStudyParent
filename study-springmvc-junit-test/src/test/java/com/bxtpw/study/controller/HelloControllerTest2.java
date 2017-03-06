package com.bxtpw.study.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ModelMap;

import static junit.framework.Assert.assertEquals;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/29 21:35
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:applicationContext-mvc.xml"})
public class HelloControllerTest2 {

    // 模拟request,response
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    // 注入loginController
    @Autowired
    private HelloController helloController;

    // 执行测试方法之前初始化模拟request,response
    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }
    /**
     *
     * @Title：testLogin
     * @Description: 测试用户登录
     */
    @Test
    public void testSayHello() {
        try {
            request.setParameter("name", "admin");
            request.setParameter("password", "2");
            ModelMap modelMap = new ModelMap();
            assertEquals("hello", helloController.sayHello("Arvin", modelMap, request, response)) ;
            System.out.println(modelMap.get("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Double d = 3.2;
        Double old = d;
        d = 2.3;
        System.out.println(old == d);
    }
}