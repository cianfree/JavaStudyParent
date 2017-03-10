package com.github.cianfree.consumer;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.message.ExchangeKeys;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消息消费客户端
 *
 * @author Arvin
 * @time 2017/3/9 20:17
 */
public class ExchangeDirectMessageConsumer {


    public static void main(String[] args) throws Exception {

        // 以下两个都是队列名称不同，但是都会收到相同的消息，原因在于，producer把消息发给exchange之后，由于客户端绑定了两个队列，因此会把同一个消息发送到这两个队列，因此都能消费消息
        startConsumer("打印日志1", "log1", "error");
        startConsumer("打印日志2", "log3", "error");

        System.out.println("............................................");

        startConsumer("存储日志", "log2", "info");
    }

    private static void startConsumer(final String consumerName, final String queueName, final String routingKey) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    factory.setPort(5672);

                    Connection connection = factory.newConnection();

                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(ExchangeKeys.LOG, BuiltinExchangeType.DIRECT);

                    // 创建一个非持久、唯一且自动删除的队列
//                    String queueName = channel.queueDeclare().getQueue();
                    channel.queueDeclare(queueName, false, true, false, null);
                    // 绑定队列并绑定
                    channel.queueBind(queueName, ExchangeKeys.LOG, routingKey);

                    Consumer callback = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                            String message = new String(body, "UTF-8");
                            System.out.println(queueName + "_" + routingKey + " receive Message: " + message);


//                            System.out.println("------------------------------------------------------------------------------------------------------------");
//                            System.out.println("消费客户端： " + queueName + ", " + consumerName);
//                            System.out.println("consumerTag: " + consumerTag);
//                            System.out.println("Envelope: " + JSON.toJSONString(envelope));
//                            System.out.println("Properties: " + JSON.toJSONString(properties));
//
//                            String message = new String(body, "UTF-8");
//                            System.out.println("Message: " + message);
//                            System.out.println("------------------------------------------------------------------------------------------------------------");
                        }
                    };

                    System.out.println("消费客户端： " + queueName + ", " + consumerName + ", 已经启动！");
                    // 接收消息
                    channel.basicConsume(queueName, false, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
