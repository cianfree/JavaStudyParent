package com.github.cianfree.producer;

import com.github.cianfree.Invoker;
import com.github.cianfree.message.ExchangeKeys;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主题消息生产者：
 * <p>
 * 发送到topic类型的exchange信箱
 * <p>
 * exchange信箱会根据指定的routingKey的匹配规则发送到指定的有监听消费者的队列中去，routingKey的规则是以 . 划分标识符的，
 * <p>
 * 如kernel.sys.error
 * 注解： 在这个routingKey中，包含了三个标识符， kernel, sys 和 error
 * <p>
 * 消费者的匹配规则：
 * <p>
 * 1. * 匹配一个标识符
 * 2. # 0个或多个标识符
 * <p>
 * 关于消费端：
 * 如果使用kernel.*.error，那么能匹配kernel.[任何一个不带 . 的标识符].error 的消息
 * 如果使用kernel.# 表示可以匹配kernel， 以及kernel后面多个标识符的消息队列
 *
 * @author Arvin
 * @time 2017/3/10 9:22
 */
public class TopicExchangeMessageProducer extends BaseProducer {

    public static void main(String[] args) {
        String topic = ExchangeKeys.TOPIC_LOG;

        sendTopicMessage(topic, "kernel.sys.info", "kernel.sys.info 1!");

        sendTopicMessage(topic, "kernel.sys.info", "kernel.sys.info 2!");

        sendTopicMessage(topic, "kernel.sys.error", "kernel.sys.error 1!");

        sendTopicMessage(topic, "kernel.sys.error", "kernel.sys.error 2!");

        sendTopicMessage(topic, "kernel.ext.info", "kernel.ext.info 1!");

        sendTopicMessage(topic, "kernel.ext.info", "kernel.ext.info 2!");

        sendTopicMessage(topic, "kernel.ext.error", "kernel.ext.error 1!");

        sendTopicMessage(topic, "kernel.ext.error", "kernel.ext.error 2!");

    }

    /**
     * 发送消息
     *
     * @param topic      主题名称
     * @param routingKey 要发送的值
     * @param message    要发送的消息
     */
    public static void sendTopicMessage(final String topic, final String routingKey, final String message) {
        new Invoker(factory) {
            @Override
            protected void doBusiness(Channel channel) throws Exception {
                // 定义一个topic类型的exchange
                channel.exchangeDeclare(topic, BuiltinExchangeType.TOPIC);
                // 发送消息
                channel.basicPublish(topic, routingKey, null, message.getBytes());

                System.out.println("成功发送了消息： " + message);
            }
        };
    }
}
