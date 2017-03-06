package edu.zhku.rmi.service;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Arvin on 2016/6/24.
 */
public interface HelloService extends Remote {

    String sayHello(String name) throws RemoteException;
}
