package com.bxtpw.common.mq.message.creator;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * 对象消息创建
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/4 14:22
 * @since 0.1
 */
public class ObjectMessageCreator<T> implements MessageCreator {

    /**
     * 消息主体
     */
    public T objectMessage;

    public ObjectMessageCreator(T objectMessage) {
        this.objectMessage = objectMessage;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        return session.createObjectMessage((Serializable) objectMessage);
    }

    /**
     * 获取Creator
     *
     * @param message 要处理的对象消息
     */
    public static MessageCreator getCreator(final Object message) {
        return new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage((Serializable) message);
            }
        };
    }
}
