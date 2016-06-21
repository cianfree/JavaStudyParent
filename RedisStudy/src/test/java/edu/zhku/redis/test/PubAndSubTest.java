package edu.zhku.redis.test;

import org.junit.Test;
import redis.clients.jedis.JedisPubSub;

/**
 * <pre>
 * 原生接口 发布与订阅测试
 * 测试的时候，先运行{@link PubAndSubTest#testSubscribe()}, 该方法是阻塞执行的
 *
 * 之后在允许发布消息的客户端
 * </pre>
 */
public class PubAndSubTest extends BaseClient {

    private final String topic = "user*";

    @Test
    public void testSubscribe() throws InterruptedException {
        // 订阅
        JedisPubSub pubSub = new JedisPubSub() {
            // 取得订阅的消息后的处理
            public void onMessage(String channel, String message) {
                logger.info("取得订阅的消息后的处理 : " + channel + "=" + message);
            }

            // 初始化订阅时候的处理
            public void onSubscribe(String channel, int subscribedChannels) {
                logger.info("初始化订阅时候的处理 : " + channel + "=" + subscribedChannels);
            }

            // 取消订阅时候的处理
            public void onUnsubscribe(String channel, int subscribedChannels) {
                logger.info("取消订阅时候的处理 : " + channel + "=" + subscribedChannels);
            }

            // 初始化按表达式的方式订阅时候的处理
            public void onPSubscribe(String pattern, int subscribedChannels) {
                logger.info("初始化按表达式的方式订阅时候的处理 : " + pattern + "=" + subscribedChannels);
            }

            // 取消按表达式的方式订阅时候的处理
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                logger.info(" 取消按表达式的方式订阅时候的处理 : " + pattern + "=" + subscribedChannels);
            }

            // 取得按表达式的方式订阅的消息后的处理
            public void onPMessage(String pattern, String channel, String message) {
                logger.info("取得按表达式的方式订阅的消息后的处理 :" + pattern + "=" + channel + "=" + message);
            }
        };
        logger.info("准备订阅！");
        jedis.psubscribe(pubSub, topic);
        logger.info("Subscribe!");
    }

    @Test
    public void testPublish() {
        // 发布
        jedis.publish("user1", "admin");
        jedis.publish("user2", "admin");
        jedis.publish("user3", "admin");
        logger.info("Publish channel: content");
    }
}