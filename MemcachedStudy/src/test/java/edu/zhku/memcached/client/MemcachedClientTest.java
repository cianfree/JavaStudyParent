package edu.zhku.memcached.client;

import net.spy.memcached.MemcachedClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Arvin on 2016/5/2.
 */
public class MemcachedClientTest {

    @Test
    public void testMemcachedClient() throws IOException {

        MemcachedClient client = new MemcachedClient(new InetSocketAddress("192.168.137.90", 11211));
        //60是超时数，默认以秒为单位
        client.set("test", 60, "1111测试memcache成功了吧");


        System.out.println(client.get("test"));
        client.shutdown();//关闭连接
    }
}
