package com.github.cianfree.producer;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Arvin
 * @time 2017/3/10 9:47
 */
public abstract class BaseProducer {

    protected final static ConnectionFactory factory = new ConnectionFactory();

    static {
        factory.setHost("localhost");
        factory.setPort(5672);
    }
}
