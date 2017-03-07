package rpc.client;

import rpc.center.MyServerCenter;
import rpc.center.Server;
import rpc.common.HelloService;
import rpc.provider.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Arvin
 * @time 2017/2/8 9:55
 */
public class RPCClientTest {

    public static void main(String[] args) throws IOException {

        // 开启服务
        startServerCenter();

        HelloService service = MyRPCClient.getProxyService(
                HelloService.class, new InetSocketAddress("localhost", 8088));
        System.out.println(service.sayHello("test"));
    }

    /**
     * 开启服务中心
     */
    public static void startServerCenter() {
        final AtomicInteger counter = new AtomicInteger();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Server serviceServer = new MyServerCenter(new InetSocketAddress("127.0.0.1", 8088));
                    serviceServer.register(HelloService.class, new HelloServiceImpl());
                    counter.incrementAndGet();
                    serviceServer.start();
                } catch (IOException e) {
                    counter.decrementAndGet();
                    e.printStackTrace();
                }
            }
        }).start();

        while (counter.intValue() == 0) {
            // 空循环
        }
        if (counter.intValue() > 0) {
            System.out.println("Server is started...");
        } else {
            System.out.println("Server start failed...");
        }
    }
}
