package edu.zhku.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.junit.Test;

/**
 * Created by Arvin on 2016/6/16.
 */
public class CuratorWatcherTest extends BaseTest {

    /**
     * 监听某个节点下的直接一级节点 孩子节点的创建，删除，更新
     *
     * @throws Exception
     */
    @Test
    public void testWatcherPath() throws Exception {
        // 2.Register watcher
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                true    // if cache data
        );

        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                } else {
                    System.out.println("Receive event: "
                            + "type=[" + event.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]");
                }
            }
        });

        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 监测节点的 创建， 更新
     */
    @Test
    public void testWatcherNode() throws Exception {
        // 节点事件监听
        final NodeCache watcher = new NodeCache(client, ZK_PATH + "/node");

        watcher.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("NodeCache changed, data is: " + new String(watcher.getCurrentData().getData()));
            }
        });

        watcher.start(false);

        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 监听某个节点以下所有节点的状态
     */
    @Test
    public void testWatcherTree() throws Exception {

        final TreeCache watcher = new TreeCache(client, ZK_PATH);

        watcher.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                } else {
                    System.out.println("Receive event: "
                            + "type=[" + event.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]");
                }
            }
        });

        watcher.start();

        Thread.sleep(Integer.MAX_VALUE);
    }

}
