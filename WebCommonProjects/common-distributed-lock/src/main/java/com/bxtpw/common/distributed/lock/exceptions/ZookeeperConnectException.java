package com.bxtpw.common.distributed.lock.exceptions;

/**
 * Zookeeper连接异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:57
 * @since 0.1
 */
public class ZookeeperConnectException extends RuntimeException {

    public ZookeeperConnectException() {
        super("Zookeeper connect error!");
    }

    public ZookeeperConnectException(String message) {
        super(message);
    }
}
