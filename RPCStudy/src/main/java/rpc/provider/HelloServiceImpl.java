package rpc.provider;

import rpc.common.HelloService;

/**
 * @author Arvin
 * @time 2017/2/8 9:39
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
