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
public class ExchangeFanoutMessageConsumer {


    public static void main(String[] args) throws Exception {

        startConsumer("打印日志", "log1");

        System.out.println("............................................");

        startConsumer("存储日志", "log2");
    }

    private static void startConsumer(final String consumerName, final String queueName) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("消费客户端： " + queueName + ", " + consumerName + ", 已经启动！");
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    factory.setPort(5672);

                    Connection connection = factory.newConnection();

                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(ExchangeKeys.LOG, BuiltinExchangeType.FANOUT);

                    // 创建一个非持久、唯一且自动删除的队列
//                    String queueName = channel.queueDeclare().getQueue();
                    channel.queueDeclare(queueName, false, true, false, null);
                    // 绑定队列并绑定
                    channel.queueBind(queueName, ExchangeKeys.LOG, "");

                    Consumer callback = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                            System.out.println("------------------------------------------------------------------------------------------------------------");
                            System.out.println("消费客户端： " + queueName + ", " + consumerName);
                            System.out.println("consumerTag: " + consumerTag);
                            System.out.println("Envelope: " + JSON.toJSONString(envelope));
                            System.out.println("Properties: " + JSON.toJSONString(properties));

                            String message = new String(body, "UTF-8");
                            System.out.println("Message: " + message);
                            System.out.println("------------------------------------------------------------------------------------------------------------");
                        }
                    };

                    // 接收消息
                    channel.basicConsume(queueName, false, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
