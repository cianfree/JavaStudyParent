package com.github.cianfree.consumer;

import com.github.cianfree.Invoker;
import com.github.cianfree.message.ExchangeKeys;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者， 查看类： TopicExchangeMessageProducer
 * @author Arvin
 * @time 2017/3/10 9:51
 */
public class TopicExchangeMessageConsumer extends BaseConsumer {

    public static void main(String[] args) {

        String topic  = ExchangeKeys.TOPIC_LOG;

        // 接收所有kernel开头的消息
        startConsumeMessageThread("C_1", topic, "kernel.#");

        // 接收所有kernel.*.error
        startConsumeMessageThread("C_2", topic, "kernel.*.error");

        // 接收所有kernel.*.info
        startConsumeMessageThread("C_3", topic, "kernel.*.info");


        // 接收所有 error 日志
        startConsumeMessageThread("C_4", topic, "#.error");

        // 接收所有 ext.*.info
        startConsumeMessageThread("C_5", topic, "ext.*.info");

    }

    /**
     * 开始一个处理
     *
     * @param consumerName      消费者名称
     * @param topic             主题名称
     * @param routingKeyPattern 要处理哪些感兴趣的消息
     */
    public static void startConsumeMessageThread(final String consumerName, final String topic, final String routingKeyPattern) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Invoker(factory) {

                    @Override
                    protected boolean isCloseConnection() {
                        return false;
                    }

                    @Override
                    protected void doBusiness(Channel channel) throws Exception {
                        // 先定义消费者
                        Consumer consumer = new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                                String message = new String(body, "UTF-8");
                                System.out.println(consumerName + "_" + routingKeyPattern + ": " + message);
                            }
                        };

                        // 定义一个要接收消息的exchange
                        channel.exchangeDeclare(topic, BuiltinExchangeType.TOPIC);
                        // 定义一个队列
                        String queueName = channel.queueDeclare().getQueue();
                        // 将队列和routingKey进行绑定
                        channel.queueBind(queueName, topic, routingKeyPattern);

                        // 接收消息并处理
                        channel.basicConsume(queueName, false, consumer);

                        System.out.println(consumerName + "_" + topic + "_" + routingKeyPattern + "\t: 正在等待消息！");
                    }
                };
            }
        }).start();
    }
}
