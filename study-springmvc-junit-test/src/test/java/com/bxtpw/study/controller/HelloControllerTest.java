package com.bxtpw.study.controller;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
public class HelloControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    @ExceptionHandler(NumberFormatException.class)
    public void testSayHello() throws Exception {

        ResultActions actions = mockMvc.perform(post("/sayHello?name=arvin"));
        System.out.println(actions.toString());
        MvcResult result = actions.andReturn();
        System.out.println(JSON.toJSONString(result.getModelAndView()));
        System.out.println(result.getModelAndView().getModelMap().get("name"));
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(JSON.toJSONString(actions));
    }
}