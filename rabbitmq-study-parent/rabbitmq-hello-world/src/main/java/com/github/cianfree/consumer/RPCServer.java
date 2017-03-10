package com.github.cianfree.consumer;

import com.github.cianfree.Invoker;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * RPC 服务端， 实现RPC的基本原理是：
 * 1. 生产者发送消息的时候，指定replyTo
 * 2. MQ收到消息，分发消息给消费者
 * 3. 消费者处理完消息，返回结果
 * 4. MQ中间件将结果发送到MQ的replyTo中
 * 5. 生产者读取消息，获得结果
 *
 * @author Arvin
 * @time 2017/3/10 10:52
 */
public class RPCServer extends BaseConsumer {

    public static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {

        rpcHandle();

//        System.out.println(fib(10));

    }

    public static void rpcHandle() {
        new Invoker(factory) {

            @Override
            protected boolean isCloseConnection() {
                return false;
            }

            @Override
            protected void doBusiness(final Channel channel) throws Exception {
                // 定义一个队列
                channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

                // 限制：每次最多给一个消费者发送1条消息
                int prefetchCount = 1;
                channel.basicQos(prefetchCount);

                System.out.println(" [x] Awaiting RPC requests");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                                .Builder()
                                .correlationId(properties.getCorrelationId())
                                .build();

                        String response = "";

                        try {
                            String message = new String(body, "UTF-8");
                            int n = Integer.parseInt(message);

                            System.out.println(" [.] fib(" + message + ")");
                            response += fib(n);
                        } catch (RuntimeException e) {
                            System.out.println(" [.] " + e.toString());
                        } finally {
                            // 处理成功后放到另外一个队列中去
                            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                            // ack 确认
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                };

                // 消费消息
                channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            }
        };
    }

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }
}
