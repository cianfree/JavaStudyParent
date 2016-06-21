package com.bxtpw.common.distributed.lock;

import com.bxtpw.common.distributed.lock.exceptions.LockException;

/**
 * 互斥锁
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 14:06
 * @since 0.1
 */
public interface MutexLock {

    /**
     * 获取分布式锁并加锁
     *
     * @throws LockException
     */
    void lock() throws LockException;

    /**
     * 释放锁
     *
     * @throws LockException
     */
    void release() throws LockException;

}
