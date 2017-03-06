package service.test;

import edu.zhku.rmi.service.HelloService;
import edu.zhku.rmi.service.impl.HelloServiceImpl;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Created by Arvin on 2016/6/24.
 */
public class RMITest {

    @Test
    public void testStartServer() throws RemoteException, NamingException, MalformedURLException {
        // 实例化实现了IService接口的远程服务ServiceImpl对象
        HelloService helloService = new HelloServiceImpl();

        // 将名称绑定到对象,即向命名空间注册已经实例化的远程服务对象
        Naming.rebind("rmi://127.0.0.1:1099/helloService", helloService);
    }

    @Test
    public void testClient() {
        String url = "rmi://localhost/";
        try {
            Context namingContext = new InitialContext();
            // 检索指定的对象。 即找到服务器端相对应的服务对象存根
            HelloService service02 = (HelloService) namingContext.lookup(url + "helloService");
            Class stubClass = service02.getClass();
            System.out.println(service02 + " 是 " + stubClass.getName() + " 的实例！");
            // 获得本底存根已实现的接口类型
            Class[] interfaces = stubClass.getInterfaces();
            for (Class c : interfaces) {
                System.out.println("存根类实现了 " + c.getName() + " 接口！");
            }
            System.out.println(service02.sayHello("Arvin"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
