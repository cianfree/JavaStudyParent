package com.bxtpw.common.distributed.lock.utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Zookeeper资源路径工具类
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/5 16:27
 * @since 0.1
 */
public class ZKPathUtil {
    /**
     * 分隔符
     */
    public static final String SEPARATOR = "/";

    /**
     * 转换path为zk的标准路径 以/开头,最后不带/
     */
    public static String normalize(String path) {
        String temp = path;
        if (!path.startsWith(SEPARATOR)) {
            temp = SEPARATOR + path;
        }
        if (path.endsWith(SEPARATOR)) {
            temp = temp.substring(0, temp.length() - 1);
            return normalize(temp);
        } else {
            return temp;
        }
    }

    /**
     * 链接两个path,并转化为zk的标准路径
     */
    public static String contact(String path1, String path2) {
        if (path2.startsWith(SEPARATOR)) {
            path2 = path2.substring(1);
        }
        if (path1.endsWith(SEPARATOR)) {
            return normalize(path1 + path2);
        } else {
            return normalize(path1 + SEPARATOR + path2);
        }
    }

    /**
     * 字符串转化成byte类型
     */
    public static byte[] toBytes(String data) {
        if (data == null || data.trim().equals("")) return null;
        return data.getBytes();
    }

    /**
     * 创建不安全的持久化的path
     *
     * @param path 要创建的path
     * @param zk   zk连接
     */
    public static void createPersistentPath(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
        // 获取一级路径
        int index = path.indexOf(SEPARATOR);
        int lastIndex = path.lastIndexOf(SEPARATOR);
        index = path.indexOf(SEPARATOR, index + 1);
        if (index < 0) {
            createPersistentPathIfNotExists(path, zk);
        } else {
            createPersistentPathIfNotExists(path.substring(0, index), zk);
            while (index != lastIndex) {
                index = path.indexOf("/", index + 1);
                createPersistentPathIfNotExists(path.substring(0, index), zk);
            }
            createPersistentPathIfNotExists(path, zk);
        }
    }

    /**
     * 创建路径
     *
     * @param path 要创建的路径
     */
    protected static void createPersistentPathIfNotExists(String path, ZooKeeper zk) throws KeeperException, InterruptedException {
        // 如果根路径不存在就创建根路径
        if (zk.exists(path, false) == null) {
            zk.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }
}
