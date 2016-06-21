package edu.zhku.redis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

/**
 * 基于Spring data 的 Redis 发布/订阅 测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-redis.xml"})
public class SpringRedisPublishTest {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 发送消息
     *
     * @param channel 发送频道
     * @param message 要发送的消息
     */
    protected void sendMessage(String channel, Serializable message) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * 发布消息
     */
    @Test
    public void testPublishMessage() {
        sendMessage("java", new Msg("message 1"));
        sendMessage("java", new Msg("message 2"));
        sendMessage("java", new Msg("message 3"));
        sendMessage("java", new Msg("message 4"));
        sendMessage("java", "asdsadsad");
    }
}
