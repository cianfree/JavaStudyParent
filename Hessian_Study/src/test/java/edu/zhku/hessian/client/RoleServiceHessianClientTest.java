package edu.zhku.hessian.client;

import com.caucho.hessian.client.HessianProxyFactory;
import edu.zhku.hessian.domain.Role;
import edu.zhku.hessian.domain.User;
import edu.zhku.hessian.service.RoleService;
import edu.zhku.hessian.service.UserService;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by Arvin on 2016/6/5.
 */
public class RoleServiceHessianClientTest {

    @Test
    public void testGetRole() throws MalformedURLException {

        String roleServiceUrl = "http://localhost/role";

        HessianProxyFactory factory = new HessianProxyFactory();
        RoleService roleService = (RoleService) factory.create(RoleService.class, roleServiceUrl);

        System.out.println(roleService.getClass());

        showRole(roleService.getRole(1));
    }

    private void showRole(Role role) {
        System.out.println(role.getId() + ", " + role.getName());
    }
}
