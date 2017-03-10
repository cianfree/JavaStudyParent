package com.github.cianfree.consumer;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.message.RoutingKeys;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;

/**
 * 消息消费
 *
 * @author Arvin
 * @time 2017/3/9 17:34
 */
public class MessageConsumer {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 5672;

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);

        //connectionFactory.setUsername("arvin");
        //connectionFactory.setPassword("123456");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        // 定义一个队列
        String queueName = RoutingKeys.HELLO; // 队列名称
        boolean durable = false; // 是否对消息进行持久化，如果是的话，重启MQ后该消息不会丢失
        boolean exclusive = false; // true的话当一个队列不再使用的时候就会移除
        boolean autoDelete = false; // 是否自动移除
        Map<String, Object> arguments = null; // Map 对象, 设置Queue的构造函数参数

        // 定义一个队列，如果没定义的话，消息发送过去也会被丢弃
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);

        System.out.println("DeclareOk: " + JSON.toJSONString(declareOk));

        // 消费消息
        boolean autoAck = true;
        String consumerTag = "hi";
        boolean noLocal = false;
        // 消费者参数
        Map<String, Object> consumeArguments = null;

        Consumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("------------------------------------------------------------------------------------------------------------");
                System.out.println("consumerTag: " + consumerTag);
                System.out.println("Envelope: " + JSON.toJSONString(envelope));
                System.out.println("Properties: " + JSON.toJSONString(properties));

                String message = new String(body, "UTF-8");
                System.out.println("Message: " + message);
            }
        };


        channel.basicConsume(queueName, autoAck, consumerTag, exclusive, noLocal, consumeArguments, callback);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    }
}
