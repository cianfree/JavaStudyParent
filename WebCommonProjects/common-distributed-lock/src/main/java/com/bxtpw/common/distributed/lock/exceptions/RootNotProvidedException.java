package com.bxtpw.common.distributed.lock.exceptions;

/**
 * 基路径未提供异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 17:20
 * @since 0.1
 */
public class RootNotProvidedException extends RuntimeException {

    public RootNotProvidedException() {
        super("Root not provided exception!");
    }

    public RootNotProvidedException(String message) {
        super(message);
    }
}
