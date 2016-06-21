package edu.zhku.dubbo.consumer;

import edu.zhku.dubbo.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Arvin on 2016/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-consumer.xml"})
public class HelloConsumerTest {

    @Autowired
    private HelloService helloService;

    @Test
    public void testHelloService() {
        System.out.println(helloService);

        System.out.println(helloService.sayHi("Arvin"));
    }
}
