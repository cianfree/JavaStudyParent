package com.github.cianfree.webbinding;

import com.github.cianfree.BaseMvcController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Arvin
 * @time 2016/11/15 18:28
 */
public class GlobalWebBindingControllerTest extends BaseMvcController {

    @Test
    public void testAddItem() throws Exception {
        ResultActions actions = mockMvc.perform(get("/global/webbinding/addItem?item=name:arvin"));

        System.out.println("Result: " + actions.andReturn().getResponse().getContentAsString());
    }
}