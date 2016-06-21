package edu.zhku.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Curator 测试
 */
public class CuratorClientTest extends BaseTest {

    @Test
    public void testCreatePersistentNode() throws Exception {
        String data1 = "hello";
        Stat stat = client.checkExists().forPath(ZK_PATH);
        // stat为空表示不存在
        if (null == stat) {
            // 返回存储的节点路径
            String result = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(ZK_PATH, data1.getBytes());
            System.out.println("Result: " + result);
        }
    }

    /**
     * 节点操作
     */
    @Test
    public void testNodeOperation() throws Exception {
        // 2.Client API test
        // 2.1 Create node
        String data1 = "hello";
        print("create", ZK_PATH, data1);

        Stat stat = client.checkExists().forPath(ZK_PATH);
        // stat为空表示不存在
        if (null == stat) {
            // 返回存储的节点路径
            String result = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(ZK_PATH, data1.getBytes());
            System.out.println("Result: " + result);
        }

        // 2.2 Get node and data
        print("ls", "/");
        List<String> items = client.getChildren().forPath("/");

        print(client.getChildren().forPath("/"));
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // 2.3 Modify data
        String data2 = "world";
        print("set", ZK_PATH, data2);
        client.setData().forPath(ZK_PATH, data2.getBytes());
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

        // 2.4 Remove node
        print("delete", ZK_PATH);
        client.delete().forPath(ZK_PATH);
        print("ls", "/");
        print(client.getChildren().forPath("/"));
    }

    @Test
    public void testGetRootNodeDate() throws Exception {
        String root = "/";

        List<String> list = client.getChildren().forPath(root);

        System.out.println("/");
        System.out.println(list);

        System.out.println("--------------------------------------------");

        byte[] data = client.getData().forPath(root);

        print(data);
        System.out.println("Show data: " + data);
    }

    @Test
    public void testGetCuratorData() throws Exception {
        String path = "/curator0000000015";
        byte[] data = client.getData().forPath(path);
        print(data);
    }

}