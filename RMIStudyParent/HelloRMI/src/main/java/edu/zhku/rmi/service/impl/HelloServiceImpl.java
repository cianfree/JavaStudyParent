package edu.zhku.rmi.service.impl;

import edu.zhku.rmi.service.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Arvin on 2016/6/24.
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    /**
     * 因为UnicastRemoteObject的构造方法抛出了RemoteException异常，因此这里默认的构造方法必须写，必须声明抛出RemoteException异常
     * @throws RemoteException
     */
    public HelloServiceImpl() throws RemoteException {
        super();
    }

    public HelloServiceImpl(int port) throws RemoteException {
        super(port);
    }

    public HelloServiceImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
