package edu.zhku.redis.test.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 消息处理
 */
public class MessageDelegate {

    protected final Logger logger = LogManager.getLogger(getClass());

    public void handleMessage(Msg message, String channel) {
        logger.info("channel: " + channel);
        logger.info(message);
    }
}
