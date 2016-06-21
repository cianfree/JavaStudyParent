package edu.zhku.dubbo.service;

import edu.zhku.dubbo.domain.User;

/**
 * Created by Arvin on 2016/6/13.
 */
public interface HelloService {

    /**
     * Say Hi
     *
     * @param name
     * @return
     */
    String sayHi(String name);

    /**
     * 根据用户ID获取User
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     * 根据用户ID和用户名称
     * @param id
     * @param name
     * @return
     */
    User get(Integer id, String name);
}
