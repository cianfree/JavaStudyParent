package com.github.cianfree.dynamic.datasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @time 2016/11/16 11:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:com/github/cianfree/dynamic/datasource/jdbctemplate/applicationContext-dynamic-datasource.xml"})
public class DynamicDataSourceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSwitchDatasource() {
        // 查询本地的admin数量
        int localAdminCount = jdbcTemplate.queryForObject("select count(*) from  admin", Integer.class);

        // 切换数据源
        DataSourceSwitch.switchDataSource(DataSourceType.TEST);

        // 测试服务器admin数量
        int testAdminCount = jdbcTemplate.queryForObject("select count(*) from  admin", Integer.class);

        System.out.println(localAdminCount + " : " + testAdminCount);
    }

}