package com.github.cianfree.producer;

import com.github.cianfree.message.ExchangeKeys;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 使用Exchange 中转站来发送消息, 路由选择：
 * <p>
 * 消息发送者，可以将消息发送到同一个exchange的某个routingKey中，可以这么理解
 * <p>
 * 发送消息到exchange.error -> consumer可以选择只接收exchange.error的消息
 * <p>
 * 和Fanout模式相比，消费者可以选择自己关系的消息进行消费
 *
 * @author Arvin
 * @time 2017/3/9 20:02
 */
public class ExchangeDirectMessageProducer {

    public static void main(String[] args) throws Exception {

        // 发送消息
        sendMessage("error", "error log 1");
        sendMessage("error", "error log 2");
        sendMessage("error", "error log 3");
        sendMessage("error", "error log 4");

        sendMessage("info", "info1");
        sendMessage("info", "info2");
        sendMessage("info", "info3");

    }

    public static void sendMessage(String routingKey, String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);


        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            // 创建转发器, 指定为fanout 即分发到多个队列的模式
            channel.exchangeDeclare(ExchangeKeys.LOG, BuiltinExchangeType.DIRECT);

            channel.basicPublish(ExchangeKeys.LOG, routingKey, null, message.getBytes());

            System.out.println("成功发送了消息： " + message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (null != connection) {
                connection.close();
            }
        }
    }
}
