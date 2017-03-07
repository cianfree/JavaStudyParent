package rpc.center;

import java.io.IOException;

/**
 * 服务注册中心
 * @author Arvin
 * @time 2017/2/8 9:42
 */
public interface Server {

    void stop();

    void start() throws IOException;

    <T> void register(Class<T> serviceInterface, T service);

    boolean isRunning();

    int getPort();
}
