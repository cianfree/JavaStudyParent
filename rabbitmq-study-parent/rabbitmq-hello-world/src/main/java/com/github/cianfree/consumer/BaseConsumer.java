package com.github.cianfree.consumer;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Arvin
 * @time 2017/3/10 9:47
 */
public abstract class BaseConsumer {

    protected final static ConnectionFactory factory = new ConnectionFactory();

    static {
        factory.setHost("localhost");
        factory.setPort(5672);
    }
}
