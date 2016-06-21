package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.exceptions.LockException;
import com.bxtpw.common.distributed.lock.utils.ZKPathUtil;
import org.apache.zookeeper.KeeperException;

import java.util.*;

/**
 * <pre>
 * 组合锁的实现，组合锁的提供配置信息，即获取锁的时候应该尝试多少次，超时多久，等等
 * 组合锁： 只有里面的所有锁都获得了才能进行操作，否则不能进行操作
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/7 17:31
 * @since 0.1
 */
public class ZookeeperCombinationMutexLock implements MutexLock {

    private int step = 1;

    /**
     * 是否线程重入
     */
    private boolean isReentry;

    /**
     * 当前线程会话
     */
    final private static ThreadLocal<Set<String>> session = new ThreadLocal<>();

    /**
     * 组合锁配置
     */
    private final List<ProviderConfig> configs;

    /**
     * 锁列表
     */
    private final List<MutexLock> locks = new ArrayList<>();

    /**
     * 当前会话唯一ID
     */
    private final String sessionId;

    /**
     * 唯一构造函数
     *
     * @param configs 组合锁配置
     */
    public ZookeeperCombinationMutexLock(List<ProviderConfig> configs) throws LockException {
        if (null == configs || configs.isEmpty()) throw new LockException(new Exception("锁配置为空！"));
        // 初始化sessionId
        sessionId = calculateSessionId(configs);
        // 初始化每一把锁
        initLockItem(configs);
        this.configs = configs;
    }

    /**
     * 初始化sessionId
     *
     * @param configs 锁配置
     */
    private String calculateSessionId(List<ProviderConfig> configs) {
        List<String> resIds = new ArrayList<>();
        for (ProviderConfig config : configs) {
            resIds.add(config.getSubResId());
        }
        Collections.sort(resIds);
        // 构造成string
        StringBuilder sb = new StringBuilder();
        for (String resId : resIds) {
            sb.append(resId);
        }
        return sb.toString();
    }

    /**
     * 初始化每一把锁
     *
     * @param configs 组合锁配置
     */
    private void initLockItem(List<ProviderConfig> configs) {
        for (ProviderConfig config : configs) {
            locks.add(new ZookeeperMutexLock(config));
        }
    }

    @Override
    public void lock() throws LockException {
        // 防止重入
        if (step != 1) throw new LockException(new Exception("This lock is invalid."));
        else step = 2;
        // 当前线程会话初始化
        initCurrentSession();
        if (!isReentry) {
            Set<MutexLock> gettedLocks = new HashSet<>();
            try {
                for (MutexLock lock : locks) {
                    lock.lock();
                    gettedLocks.add(lock);
                }
            } catch (LockException e) {
                //没有获得锁，释放其他锁并返回
                try {
                    for (MutexLock lock : gettedLocks) {
                        lock.release();
                    }
                } catch (LockException ignored) {
                }
                e.printStackTrace();
                throw e;
            } finally {
                gettedLocks.clear();
            }
        }
    }

    /**
     * 初始化线程信息
     *
     * @throws LockException
     */
    private void initCurrentSession() throws LockException {
        if (session.get() == null || !session.get().contains(sessionId)) {
            Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
            set.add(sessionId);
            session.set(set);
            isReentry = false;
        } else {
            isReentry = true;
        }
        // 如果根路径不存在就创建根路径
        try {
            for (ProviderConfig config : configs) {
                ZKPathUtil.createPersistentPath(config.getRoot(), config.getZk());
            }
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        }
    }

    @Override
    public void release() throws LockException {
        // 释放所有的锁
        for (MutexLock lock : locks) {
            try {
                // TODO 如果要严谨一点，还需要把这个未成功解锁的进行解锁操作
                lock.release();
            } catch (LockException ignored) {
            }
        }

    }
}
