package edu.zhku.hessian.client;

import com.caucho.hessian.client.HessianProxyFactory;
import edu.zhku.hessian.domain.User;
import edu.zhku.hessian.service.UserService;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by Arvin on 2016/6/5.
 */
public class UserServiceHessianClientTest {

    @Test
    public void testGetUser() throws MalformedURLException {

        String userServiceUrl = "http://localhost/user";

        HessianProxyFactory factory = new HessianProxyFactory();
        factory.setOverloadEnabled(true); // 支持重载
        UserService userService = (UserService) factory.create(UserService.class, userServiceUrl);

        User user = null;
        try {
            user = userService.get(1);
        } catch (Exception e) {
            System.out.println(e.getClass());
            e.printStackTrace();
        }

        showUser(user);
    }

    private void showUser(User user) {
        System.out.println(user.getId() + ", " + user.getName() + ", " + user.getAge());
    }
}
