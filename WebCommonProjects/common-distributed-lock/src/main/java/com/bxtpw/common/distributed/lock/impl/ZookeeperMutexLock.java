package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.exceptions.LockException;
import com.bxtpw.common.distributed.lock.utils.ZKPathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.HashSet;
import java.util.Set;

/**
 * Zookeeper 互斥锁
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/7 8:53
 * @since 0.1
 */
public class ZookeeperMutexLock extends AbstractLock implements MutexLock {

    /**
     * Construction
     *
     * @param lockConfig 获取锁的配置
     * @throws LockException
     */
    public ZookeeperMutexLock(ProviderConfig lockConfig) {
        super(lockConfig);
    }

    @Override
    synchronized public void lock() throws LockException {
        // 防止重入
        if (step != 1) throw new LockException(new Exception("This lock is invalid."));
        else step = 2;
        init(); // 初始化会话
        while (!isReentry) {
            currentTryCount++;
            try {
                execute();
                break;
            } catch (LockException ex) {
                if (currentTryCount >= config.getRetryTimes()) { // 超过了重试的次数
                    closeLocalThread();
                    throw new LockException(ex);
                } else
                    try {
                        Thread.sleep(config.getRetryTimeout());
                    } catch (Exception ignored) {
                    }
            }
        }
    }

    /**
     * 初始化线程信息
     *
     * @throws LockException
     */
    private void init() throws LockException {
        if (session.get() == null || !session.get().contains(config.getResId())) {
            Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
            set.add(config.getResId());
            session.set(set);
            isReentry = false;
        } else {
            isReentry = true;
        }
        // 创建基路径
        try {
            ZKPathUtil.createPersistentPath(config.getRoot(), config.getZk());
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        }
    }

    /**
     * 执行锁定
     *
     * @throws LockException
     */
    private void execute() throws LockException {
        try {
            getZK().create(config.getResId(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        }
    }

    private void closeLocalThread() {
        String resId = config.getResId();
        if (!isReentry && session.get() != null) {
            if (session.get().contains(resId)) session.get().remove(resId);
        }
    }

    @Override
    synchronized public void release() throws LockException {
        if (step != 2) throw new LockException(new Exception("This lock is invalid."));
        else step = 3;
        closeLocalThread();
    }

}
