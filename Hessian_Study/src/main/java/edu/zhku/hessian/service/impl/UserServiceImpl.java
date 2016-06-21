package edu.zhku.hessian.service.impl;

import edu.zhku.hessian.domain.User;
import edu.zhku.hessian.exception.UserNotExistsException;
import edu.zhku.hessian.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arvin on 2016/6/5.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();

    public UserServiceImpl() {
        userMap.put(1, new User(1, "张三", 20));
        userMap.put(2, new User(2, "李四", 22));
        userMap.put(3, new User(3, "王五", 23));
        userMap.put(4, new User(4, "六弦", 24));
        userMap.put(5, new User(5, "无憾", 26));
    }

    @Override
    public User get(Integer id) {
        User user = userMap.get(id);
        if (null == user) throw new UserNotExistsException();
        return user;
    }

    @Override
    public User get(Integer id, String name) {
        /*try {
            System.out.println("服务端休息两秒");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        User user = userMap.get(id);
        if (null == user) throw new UserNotExistsException();
        if (user.getName().equals(name)) {
            return user;
        }
        throw new UserNotExistsException();
    }
}
