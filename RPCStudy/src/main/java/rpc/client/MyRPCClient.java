package rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Arvin
 * @time 2017/2/8 10:24
 */
public class MyRPCClient {

    /**
     * 获取代理的服务
     *
     * @param serviceInterface
     * @param address
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxyService(final Class<T> serviceInterface, final InetSocketAddress address) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Socket socketClient = null;
                ObjectOutputStream output = null;
                ObjectInputStream input = null;
                try {
                    socketClient = new Socket();
                    // 连接到服务提供者
                    socketClient.connect(address);

                    // 将远程服务需要的参数，提供者相关信息发送给服务提供者中心
                    // 包含服务接口类，方法名称，方法参数类型列表，方法参数
                    // 获取输出流
                    output = new ObjectOutputStream(socketClient.getOutputStream());
                    // 输出接口类, 对应服务端用input.readUTF()接收第一个参数
                    output.writeUTF(serviceInterface.getName());
                    // 输出方法名, 对应服务端用input.readUTF()接收第二个参数
                    output.writeUTF(method.getName());
                    // 输出参数类型列表, 对应服务端使用input.readObject() 接收
                    output.writeObject(method.getParameterTypes());
                    // 输出实际参数列表, 对应服务端使用input.readObject() 接收
                    output.writeObject(args);

                    // 参数都传完了，远程执行完成后，读取远程执行的结果
                    input = new ObjectInputStream(socketClient.getInputStream());
                    // 读取结果
                    return input.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != socketClient) socketClient.close();
                    if (null != output) output.close();
                    if (null != input) input.close();
                }
                return null;
            }
        });
    }
}
