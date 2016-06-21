package com.bxtpw.common.distributed.lock.exceptions;

/**
 * 资源不存在异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 15:49
 * @since 0.1
 */
public class ResNotFoundException extends RuntimeException {

    public ResNotFoundException() {
        super("Res is not found!");
    }

    public ResNotFoundException(String message) {
        super(message);
    }
}
