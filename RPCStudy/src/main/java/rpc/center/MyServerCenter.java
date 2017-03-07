package rpc.center;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Arvin
 * @time 2017/2/8 10:42
 */
public class MyServerCenter implements Server {

    // 线程执行
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /** 服务类实例提供者 */
    private final Map<String, Object> serviceContainer = new HashMap<>();

    /** 绑定地址 */
    private InetSocketAddress bindAddress;

    private boolean isRunning = false;

    public MyServerCenter(InetSocketAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void start() throws IOException {
        isRunning = true;
        // 开启一个ServerSocket接受请求
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(bindAddress);
        System.out.println("MyServerCenter is started...");
        try {
            // 接收来自客户端的请求
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new SocketTask(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void register(Class<T> serviceInterface, T service) {
        serviceContainer.put(serviceInterface.getName(), service);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return bindAddress.getPort();
    }

    /**
     * 任务处理
     */
    private class SocketTask implements Runnable {

        private final Socket socket;

        public SocketTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;
            try {

                input = new ObjectInputStream(socket.getInputStream());
                // 读取服务对应的接口类名称
                String serviceInterfaceName = input.readUTF();
                // 读取方法名称
                String methodName = input.readUTF();
                // 读取方法的参数类型列表
                Class<?>[] parameterTypes = (Class[]) input.readObject();
                // 读取参数列表
                Object[] parameters = (Object[]) input.readObject();

                // 获取业务服务实例
                Object service = serviceContainer.get(serviceInterfaceName);
                if (null == service) {
                    throw new RuntimeException(serviceInterfaceName + "'s instance not found!");
                }

                // 反射获取要指定的方法
                Method method = service.getClass().getMethod(methodName, parameterTypes);

                output = new ObjectOutputStream(socket.getOutputStream());

                // 输入结果到客户端，客户端使用readObject接收
                output.writeObject(method.invoke(service, parameters));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    if (null != input) input.close();
                    if (null != output) output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
