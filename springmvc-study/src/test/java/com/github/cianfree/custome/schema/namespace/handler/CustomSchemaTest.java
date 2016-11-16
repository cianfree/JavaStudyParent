package com.github.cianfree.custome.schema.namespace.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arvin
 * @time 2016/11/16 14:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:com/github/cianfree/custome/schema/xml/applicationContext-custome-schema.xml"})
public class CustomSchemaTest {

    @Resource(name = "otherDateFormat")
    private SimpleDateFormat dateFormat;

    @Test
    public void testGetDateFormat() {
        System.out.println(dateFormat.format(new Date()));
    }

}