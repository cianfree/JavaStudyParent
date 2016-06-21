package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.ProviderConfig;
import org.apache.zookeeper.ZooKeeper;

import java.util.Set;

/**
 * 抽象锁
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:47
 * @since 0.1
 */
public abstract class AbstractLock {
    /**
     * 当前重试次数
     */
    protected int currentTryCount = 0;

    protected int step = 1;

    /**
     * 是否线程重入
     */
    protected boolean isReentry;

    /**
     * 当前线程会话
     */
    final protected static ThreadLocal<Set<String>> session = new ThreadLocal<>();

    /**
     * 锁的配置
     */
    protected ProviderConfig config;

    public AbstractLock(ProviderConfig config) {
        this.config = config;
    }

    protected ZooKeeper getZK() {
        return config.getZk();
    }
}
