package com.bxtpw.common.mq.message.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * 对象消息监听器, 处理对象消息
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/4 14:08
 * @since 0.1
 */
public abstract class ObjectMessageListener<T> implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // 确认是Object的消息才进行转换
        if (message instanceof ObjectMessage) {
            try {
                T trueMessage = (T) ((ObjectMessage) message).getObject();
                handleMessage(trueMessage, message);
            } catch (JMSException e) {
                onHandleError(null, message, e);
            }
        } else {
            handleMessage(null, message);
        }
    }

    /**
     * 处理消息
     *
     * @param message 消息对象
     * @param source  消息源
     */
    public abstract void handleMessage(T message, Message source);

    /**
     * 处理消息异常
     *
     * @param message 消息对象
     * @param source  消息原始对象
     * @param ex      异常信息
     */
    public void onHandleError(T message, Message source, Exception ex) {
        ex.printStackTrace();
    }
}
