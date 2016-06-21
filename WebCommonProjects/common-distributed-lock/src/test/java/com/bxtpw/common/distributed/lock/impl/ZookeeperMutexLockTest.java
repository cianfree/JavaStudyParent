package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.exceptions.LockException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * 互斥锁测试
 */
public class ZookeeperMutexLockTest {

    /**
     * Zookeeper url
     */
    private final String url = "192.168.137.90:2181";

    private final static String auth_type = "digest";
    private final static String auth_passwd = "password";

    private ZooKeeper zk;

    @Before
    public void ready() throws Exception {
        zk = new ZooKeeper(url, 3000, null);
        zk.addAuthInfo(auth_type, auth_passwd.getBytes());
        System.out.println("连接ZK成功！");
    }

    @After
    public void after() {
        if (null != zk) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                System.out.println("关闭ZK错误");
            }
        }
    }

    /**
     * 单互斥锁测试
     */
    @Test
    public void testSingleMutexLock() throws Exception {

        getSingleMutexLock(1500);

        Thread.sleep(200);

        getSingleMutexLock(1500);

        Thread.sleep(5000);

    }

    public void getSingleMutexLock(final int businessTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MutexLock lock = null;
                try {
                    // 构造锁的提供配置
                    ProviderConfig config = ProviderConfig.builder(zk, "mutex")//
                            .resId("user1").build();

                    lock = new ZookeeperMutexLock(config);
                    // 执行锁定操作
                    lock.lock();
                    // 处理业务
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")获得锁成功，处理相关业务");
                    try {
                        Thread.sleep(businessTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")业务处理完成");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")没有获得锁！");
                } finally {
                    if (null != lock) {
                        try {
                            // 释放锁
                            lock.release();
                        } catch (LockException ignored) {
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 同质组合锁测试，同一个root下的
     */
    @Test
    public void testGroupMutexLock() throws Exception {
        // 获取互斥锁
        getGroupLock(1500, "user1", "user2", "user3");
        // 等待500毫秒
        Thread.sleep(500);
        // 重新获取锁， 此时是无法获取锁的
        getGroupLock(1500, "user6", "user4", "user5");

        Thread.sleep(4000);
    }

    public void getGroupLock(final int businessExecuteTime, final String... resIds) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MutexLock lock = null;
                try {
                    ProviderConfig config = ProviderConfig.builder(zk, "/group/mutex").buildGroupConfig();
                    lock = new ZookeeperGroupMutexLock(config, resIds);
                    // 准备获取锁
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")尝试获得互斥锁");
                    // 获取互斥锁
                    lock.lock();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经获得互斥锁，准备执行业务");
                    try {
                        Thread.sleep(businessExecuteTime);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经执行了业务");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")没有获得互斥锁");
                } finally {
                    if (null != lock) {
                        try {
                            lock.release();
                        } catch (LockException ignored) {
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 同质组合锁测试，可能不同一个root下的
     */
    @Test
    public void testCombinationMutexLock() throws Exception {
        // 获取互斥锁
        getCombinationLock(1500, //
                ProviderConfig.builder(zk, "/comb/type1/").resId("user1").build(),//
                ProviderConfig.builder(zk, "/comb/type2/").resId("user1").build(),//
                ProviderConfig.builder(zk, "/comb/type3/").resId("user1").build()//
        );
        // 等待500毫秒
        Thread.sleep(500);
        // 重新获取锁， 此时是无法获取锁的
        getCombinationLock(1500, //
                ProviderConfig.builder(zk, "/comb/type1/").resId("user2").build(),//
                ProviderConfig.builder(zk, "/comb/type2/").resId("user2").build(),//
                ProviderConfig.builder(zk, "/comb/type3/").resId("user2").build()//
        );

        Thread.sleep(4000);
    }

    public void getCombinationLock(final int businessExecuteTime, final ProviderConfig... configs) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MutexLock lock = null;
                try {
                    lock = new ZookeeperCombinationMutexLock(Arrays.asList(configs));
                    // 准备获取锁
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")尝试获得互斥锁");
                    // 获取互斥锁
                    lock.lock();
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经获得互斥锁，准备执行业务");
                    try {
                        Thread.sleep(businessExecuteTime);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")已经执行了业务");
                } catch (LockException e) {
                    System.out.println("Thread(" + Thread.currentThread().getId() + ")没有获得互斥锁");
                } finally {
                    if (null != lock) {
                        try {
                            lock.release();
                        } catch (LockException ignored) {
                        }
                    }
                }
            }
        }).start();
    }
}