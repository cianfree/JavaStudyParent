package edu.zhku.curator.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.junit.After;
import org.junit.Before;

/**
 * Created by Arvin on 2016/6/16.
 */
public class BaseTest {

    /**
     * Zookeeper info
     */
    private static final String ZK_ADDRESS = "192.168.137.90:2181";
    /**
     * 测试节点
     */
    protected static final String ZK_PATH = "/curator";

    protected CuratorFramework client;

    @Before
    public void ready() {
        client = CuratorFrameworkFactory.newClient(//
                ZK_ADDRESS,//
                new RetryNTimes(10, 5000) // 重试10次，每隔5秒重试一次
        );
        client.start();
        System.out.println("zk client start successfully!");
    }

    @After
    public void after() {
        client.close();
    }

    protected static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

    protected static void print(Object result) {
        System.out.println(
                result instanceof byte[]
                        ? new String((byte[]) result)
                        : result);
    }
}
