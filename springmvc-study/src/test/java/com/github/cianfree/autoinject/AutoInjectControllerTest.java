package com.github.cianfree.autoinject;

import com.github.cianfree.BaseMvcController;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Arvin
 * @time 2016/11/16 10:12
 */
public class AutoInjectControllerTest extends BaseMvcController {

    @Test
    public void testCurrentUser() throws Exception {
        ResultActions actions = mockMvc.perform(get("/auto/inject/currentUser"));

        System.out.println("Result: " + actions.andReturn().getResponse().getContentAsString());
    }
}