package com.github.cianfree.dynamic.datasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 自动切换数据源测试
 *
 * @author Arvin
 * @time 2016/11/16 11:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:com/github/cianfree/dynamic/datasource/jdbctemplate/applicationContext-datasource-auto-switch.xml"})
public class DynamicDataSourceAutoSwitchTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAutoSwitchDatasource() {

        System.out.println(jdbcTemplate.getClass());

        jdbcTemplate.queryForObject("select count(*) from  admin", Integer.class);

        jdbcTemplate.update("update admin set name = 'asd' where id = 10000");

    }

}