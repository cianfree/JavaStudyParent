package com.bxtpw.common.mq.message.converter;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * 自定义对象消息转换器
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/4 14:42
 * @since 0.1
 */
public class ObjectMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        return session.createObjectMessage((Serializable) object);
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof ObjectMessage) {
            return ((ObjectMessage) message).getObject();
        }
        return null;
    }
}
