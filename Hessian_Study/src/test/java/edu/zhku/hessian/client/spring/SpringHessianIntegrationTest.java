package edu.zhku.hessian.client.spring;

import com.caucho.hessian.client.HessianConnectionException;
import com.caucho.hessian.client.HessianConnectionFactory;
import edu.zhku.hessian.auth.MyHessianConnectionFactory;
import edu.zhku.hessian.domain.User;
import edu.zhku.hessian.service.RoleService;
import edu.zhku.hessian.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Arvin on 2016/6/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-hessian-client.xml"})
public class SpringHessianIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    public void testUserService() {

        //System.setProperty(HessianConnectionFactory.class.getName(), MyHessianConnectionFactory.class.getName());

        try {
            System.out.println(userService);

            User user = userService.get(1, "张三");


            System.out.println(user);
        } catch(RemoteConnectFailureException e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
