package edu.zhku.redis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 与Spring集成情况下的使用
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-redis.xml"})
public class SpringRedisTemplateTest {

    @Autowired
    private RedisTemplate<String, String> stringTemplate; // inject the template

    @Autowired
    private RedisTemplate<String, List<Integer>> integerListTemplate;

    @Test
    public void testSpringRedisTemplate() {
        stringTemplate.opsForValue().set("name", "arvin");
        System.out.println(stringTemplate.opsForValue().get("name"));

        integerListTemplate.opsForValue().set("number", Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(integerListTemplate.opsForValue().get("number"));
    }

    @Test
    public void testHashKey() {
        stringTemplate.opsForHash().put("family", "1", "1");
        stringTemplate.opsForHash().put("family", "2", "2");
        stringTemplate.opsForHash().put("family", "3", "3");
        stringTemplate.opsForHash().put("family", "4", "4");

        Set<Object> keys = stringTemplate.opsForHash().keys("family");
        List<Object> values = stringTemplate.opsForHash().values("family");
        System.out.println(keys);
        System.out.println(values);
    }
}
