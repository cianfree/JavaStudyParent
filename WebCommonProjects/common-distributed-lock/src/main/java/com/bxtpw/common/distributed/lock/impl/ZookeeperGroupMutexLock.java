package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.MutexLock;
import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.exceptions.LockException;
import com.bxtpw.common.distributed.lock.exceptions.ResNotFoundException;
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
public class ZookeeperGroupMutexLock implements MutexLock {

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
     * 锁列表
     */
    private final List<MutexLock> locks = new ArrayList<>();

    /**
     * 当前会话唯一ID
     */
    private final String sessionId;

    /**
     * 主配置，包含zk，root，retryTimes，retryTimeout
     */
    private final ProviderConfig config;

    /**
     * 唯一构造函数
     *
     * @param config 组合锁配置， root必须填写，resId不需要填写
     * @param resIds 资源ID列表
     */
    public ZookeeperGroupMutexLock(ProviderConfig config, String... resIds) throws LockException {
        this(config, Arrays.asList(resIds));
    }

    /**
     * 分布式的组锁
     *
     * @param config 组合锁配置， root必须填写，resId不需要填写
     * @param resIds 资源ID列表
     */
    public ZookeeperGroupMutexLock(ProviderConfig config, List<String> resIds) throws LockException {
        if (null == resIds || resIds.isEmpty()) throw new LockException(new ResNotFoundException());
        this.config = config;
        sessionId = createSessionIdFromResIds(resIds);
        // 初始化每一把锁
        initLockItem(resIds);
    }

    /**
     * 创建sessionId
     *
     * @param resIds 资源ID列表
     */
    private String createSessionIdFromResIds(List<String> resIds) {
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
     * @param resIds 资源ID列表
     */
    private void initLockItem(List<String> resIds) {
        for (String resId : resIds) {
            locks.add(new ZookeeperMutexLock(ProviderConfig.builder(config.getZk(), config.getRoot())//
                    .resId(resId)//
                    .retryTimeout(config.getRetryTimeout())//
                    .retryTimes(config.getRetryTimes())//
                    .root(config.getRoot())//
                    .build()));
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
            ZKPathUtil.createPersistentPath(config.getRoot(), config.getZk());
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
