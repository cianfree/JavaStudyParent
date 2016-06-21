package edu.zhku.redis.test.spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by Arvin on 2016/6/18.
 */
public class SpringRedisSubscribeClientTest {

    @Test
    public void testSubscribe() {
        new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml", "classpath:applicationContext-redis-pubsub.xml");

        while (true) { //这里是一个死循环,目的就是让进程不退出,用于接收发布的消息
            try {
                System.out.println("current time: " + new Date());

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
