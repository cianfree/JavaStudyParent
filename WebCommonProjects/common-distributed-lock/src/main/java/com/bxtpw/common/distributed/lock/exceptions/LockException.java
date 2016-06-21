package com.bxtpw.common.distributed.lock.exceptions;

/**
 * 分布式锁异常
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 15:43
 * @since 0.1
 */
public class LockException extends Exception {

    private Exception ex;

    public LockException(Exception ex) {
        this.ex = ex;
    }

    public void printStackTrace() {
        ex.printStackTrace();
    }

}
