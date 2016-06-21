package com.bxtpw.common.distributed.lock.impl;

import com.bxtpw.common.distributed.lock.ProviderConfig;
import com.bxtpw.common.distributed.lock.SharedLock;
import com.bxtpw.common.distributed.lock.exceptions.LockException;
import com.bxtpw.common.distributed.lock.utils.ZKPathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.*;

/**
 * <pre>
 * 共享锁有读锁和写锁两种状态，共享锁的实现机制是当一个线程获取锁的读状态后，
 * 另一个线程也可以获取该锁的读状态，但是另一个线程无法获取该锁的写状态，并抛出异常；
 * 当一个线程获取锁的写状态后，另一个线程无法获取该锁，并抛出异常。
 *
 * 这里的线程可以在不同的JVM中；目前支持重入特性，在同一线程中的不同地方（不同的方法中）可以重复获取同一把锁
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/7 13:37
 * @since 0.1
 */
public class ZookeeperSharedLock extends AbstractLock implements SharedLock {

    /**
     * 读锁后缀
     */
    private static final String READ_LOCK_SUFFIX = "_r";
    /**
     * 写锁后缀
     */
    private static final String WRITE_LOCK_SUFFIX = "_w";

    /**
     * 读锁
     */
    private static final String READ_LOCK = "READ";
    /**
     * 写锁
     */
    private static final String WRITE_LOCK = "WRITE";

    final private String zookeeperID;

    /**
     * Construction
     *
     * @param lockConfig 获取锁的配置
     * @throws LockException
     */
    public ZookeeperSharedLock(ProviderConfig lockConfig) {
        super(lockConfig);
        // 生成随机的ZK ID
        this.zookeeperID = UUID.randomUUID().toString();
    }

    /**
     * 初始化
     *
     * @param type 锁的类型
     * @throws LockException
     */
    private void init(String type) throws LockException {
        String resId = config.getSubResId();
        if (type.equals(READ_LOCK)) {
            initReadLock(resId); // 初始化锁
        } else if (type.equals(WRITE_LOCK)) {
            initWriteLock(resId);
        }
    }

    /**
     * 初始化写锁
     *
     * @param resId 资源ID， 相对于root
     * @throws LockException
     */
    private void initWriteLock(String resId) throws LockException {
        if (session.get() == null || !session.get().contains(resId + WRITE_LOCK_SUFFIX)) {
            if (session.get() != null && session.get().contains(resId + READ_LOCK_SUFFIX)) {
                throw new LockException(new Exception("The lock has been locked."));
            }
            Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
            set.add(resId + WRITE_LOCK_SUFFIX);
            session.set(set);
            isReentry = false;
        } else {
            isReentry = true;
        }
    }

    /**
     * 初始化读锁
     *
     * @param resId 要读取的资源ID
     */
    private void initReadLock(String resId) {
        if (session.get() == null || (!session.get().contains(resId + WRITE_LOCK_SUFFIX) && !session.get().contains(resId + READ_LOCK_SUFFIX))) {
            Set<String> set = session.get() == null ? new HashSet<String>() : session.get();
            set.add(resId + READ_LOCK_SUFFIX);
            session.set(set);
            isReentry = false;
        } else {
            isReentry = true;
        }
    }

    @Override
    synchronized public void write() throws LockException {
        if (step != 1) throw new LockException(new Exception("This lock is invalid."));
        else step = 2;
        // 初始化写锁
        init(WRITE_LOCK);
        while (!isReentry) {
            currentTryCount++;
            try {
                executeWrite();
                break;
            } catch (LockException ex) {
                if (currentTryCount >= config.getRetryTimes()) {
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
     * 写锁, 写锁是互斥的，即同一时间内，只能有一个人获得写锁，且其他线程无法获取读锁
     *
     * @throws LockException
     */
    private void executeWrite() throws LockException {
        // 创建根目录
        String actualPath = "";
        try {
            ZKPathUtil.createPersistentPath(config.getRoot(), config.getZk());
            actualPath = getZK().create(config.getResId() + "###" + zookeeperID + "###write###", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> list1 = getZK().getChildren(config.getRoot(), null);
            List<String> list2 = new ArrayList<String>();
            for (String node : list1) {
                if (node.split("###").length == 4) list2.add(node);
            }

            Collections.sort(list2, new Comparator<String>() {
                @Override
                public int compare(String node1, String node2) {
                    String[] node1s = node1.split("###");
                    String[] node2s = node2.split("###");
                    if (Long.parseLong(node1s[3]) > Long.parseLong(node2s[3])) return 1;
                    if (Long.parseLong(node1s[3]) < Long.parseLong(node2s[3])) return -1;
                    return 0;
                }
            });

            for (String node : list2) {
                if (node.contains(config.getSubResId()))
                    if (!node.contains(zookeeperID))
                        throw new LockException(new Exception("The lock has been locked."));
                    else break;
            }
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        } catch (LockException e) {
            if (actualPath != null) {
                try {
                    getZK().delete(actualPath, 0);
                } catch (InterruptedException | KeeperException ignored) {
                }
            }
            throw e;
        }
    }

    @Override
    public void read() throws LockException {
        if (step != 1) throw new LockException(new Exception("This lock is invalid."));
        else step = 2;
        // 初始化读锁
        init(READ_LOCK);
        while (!isReentry) {
            currentTryCount++;
            try {
                executeRead();
                break;
            } catch (LockException ex) {
                if (currentTryCount >= config.getRetryTimes()) {
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
     * 执行读锁，多个客户端可以同时获取读锁，但是一旦存在读锁，就无法获得写锁，也就是说，读取的过程中无法进行写入
     *
     * @throws LockException
     */
    private void executeRead() throws LockException {
        // 创建根目录
        String actualPath = "";
        try {
            ZKPathUtil.createPersistentPath(config.getRoot(), config.getZk());
            actualPath = getZK().create(config.getResId() + "###" + zookeeperID + "###read###", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> list1 = getZK().getChildren(config.getRoot(), null);
            List<String> list2 = new ArrayList<>();
            for (String node : list1) {
                if (node.split("###").length == 4) list2.add(node);
            }

            Collections.sort(list2, new Comparator<String>() {
                @Override
                public int compare(String node1, String node2) {
                    String[] node1s = node1.split("###");
                    String[] node2s = node2.split("###");
                    if (Long.parseLong(node1s[3]) > Long.parseLong(node2s[3])) return 1;
                    if (Long.parseLong(node1s[3]) < Long.parseLong(node2s[3])) return -1;
                    return 0;
                }
            });

            for (String node : list2)
                if (node.contains(config.getSubResId())) {
                    if (!node.contains(zookeeperID)) {
                        if (node.contains("###write###"))
                            throw new LockException(new Exception("The lock has been write locked."));
                    } else {
                        break;
                    }
                }
        } catch (KeeperException | InterruptedException e) {
            throw new LockException(e);
        } catch (LockException e) {
            if (actualPath != null) {
                try {
                    getZK().delete(actualPath, -1);
                } catch (InterruptedException | KeeperException ignored) {
                }
            }
            throw e;
        }

    }

    /**
     * 关闭当前线程
     */
    private void closeLocalThread() {
        if (!isReentry && session.get() != null) {
            session.get().remove(config.getSubResId() + WRITE_LOCK_SUFFIX);
            session.get().remove(config.getSubResId() + READ_LOCK_SUFFIX);
        }
    }

    @Override
    synchronized public void release() throws LockException {
        if (step != 2) throw new LockException(new Exception("The lock is invalid"));
        else step = 3;
        closeLocalThread();
    }

}
