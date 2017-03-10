package com.github.cianfree.producer;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.message.RoutingKeys;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Map;

/**
 * 消息生产
 *
 * @author Arvin
 * @time 2017/3/9 16:22
 */
public class MessageProducer {

    public static void main(String[] args) throws Exception {

        String host = "localhost";
        int port = 5672;

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);

        connectionFactory.setUsername("arvin");
        connectionFactory.setPassword("123456");

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

        // 要发送的消息
        String message = "Hello, RabbitMQ 1";

        // 使用channel发送一条消息
        String exchange = "";
        channel.basicPublish(exchange, queueName, null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        // 关闭管道和连接
        channel.close();
        connection.close();
    }
}
