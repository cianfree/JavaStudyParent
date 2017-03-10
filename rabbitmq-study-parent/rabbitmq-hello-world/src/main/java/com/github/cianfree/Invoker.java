package com.github.cianfree;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Arvin
 * @time 2017/3/10 9:37
 */
public abstract class Invoker {

    private ConnectionFactory connectionFactory;

    private Object result;

    public Invoker(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        invoke();
    }

    protected Connection createConnection(ConnectionFactory factory) throws IOException, TimeoutException {
        return factory.newConnection();
    }

    protected Channel createChannel(Connection connection) throws IOException {
        return connection.createChannel();
    }


    protected boolean isCloseConnection() {
        return true;
    }

    private void invoke() {
        Connection connection = null;
        Channel channel = null;

        try {
            connection = createConnection(connectionFactory);
            channel = createChannel(connection);
            doBusiness(channel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isCloseConnection()) {
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

                if (null != connection) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    protected abstract void doBusiness(Channel channel) throws Exception;


    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
