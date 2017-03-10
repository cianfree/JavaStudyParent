package com.github.cianfree.producer;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.message.ExchangeKeys;
import com.rabbitmq.client.*;

/**
 * 使用Exchange 中转站来发送消息, 本次的目的是要实现一个消息消费多次
 * <p>
 * 注意，consumer要先启动，否则如果发送消息的时候，没有任何消费者，那么就会丢失
 *
 * @author Arvin
 * @time 2017/3/9 20:02
 */
public class ExchangeFanoutMessageProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);


        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            // 创建转发器, 指定为fanout 即分发到多个队列的模式
            channel.exchangeDeclare(ExchangeKeys.LOG, BuiltinExchangeType.FANOUT);

            // 发送消息
            String message = "Hi, Exchange!";
            channel.basicPublish(ExchangeKeys.LOG, "", null, message.getBytes());

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
