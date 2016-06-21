package com.bxtpw.common.distributed.lock;

import com.bxtpw.common.distributed.lock.exceptions.LockException;

/**
 * <pre>
 * 共享锁接口定义，共享锁有读锁和写锁两种状态，共享锁的实现机制是当一个线程获取锁的读状态后，
 * 另一个线程也可以获取该锁的读状态，但是另一个线程无法获取该锁的写状态，并抛出异常；
 * 当一个线程获取锁的写状态后，另一个线程无法获取该锁，并抛出异常。
 * </pre>
 * @author 夏集球
 * @time 2016/3/7 13:41
 * @version 0.1
 * @since 0.1
 */
public interface SharedLock {

    /**
     * 获取写锁
     *
     * @throws LockException
     */
    void write() throws LockException;

    /**
     * 获取读锁
     *
     * @throws LockException
     */
    void read() throws LockException;

    /**
     * 释放锁
     *
     * @throws LockException
     */
    void release() throws LockException;

}
