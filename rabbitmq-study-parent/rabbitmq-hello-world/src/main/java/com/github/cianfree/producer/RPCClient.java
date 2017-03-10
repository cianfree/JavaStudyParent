package com.github.cianfree.producer;

import com.github.cianfree.Invoker;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Arvin
 * @time 2017/3/10 11:02
 */
public class RPCClient extends BaseProducer {

    public static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        System.out.println(fib(12));
    }

    public static int fib(final int n) {
        Object obj = new Invoker(factory) {
            @Override
            protected void doBusiness(Channel channel) throws Exception {
                String replyQueueName = channel.queueDeclare().getQueue();
                final String corrId = UUID.randomUUID().toString();

                AMQP.BasicProperties props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(corrId)
                        .replyTo(replyQueueName)
                        .build();

                channel.basicPublish("", RPC_QUEUE_NAME, props, String.valueOf(n).getBytes("UTF-8"));

                final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

                // 等待结果
                channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        if (properties.getCorrelationId().equals(corrId)) {
                            response.offer(new String(body, "UTF-8"));
                        }
                    }
                });

                this.setResult(response.take());
            }
        }.getResult();
        if (null == obj) {
            return -1;
        }
        return Integer.parseInt(obj.toString());

    }
}
