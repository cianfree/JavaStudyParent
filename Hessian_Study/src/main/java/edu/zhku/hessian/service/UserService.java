package edu.zhku.hessian.service;

import edu.zhku.hessian.domain.User;

/**
 * Created by Arvin on 2016/6/4.
 */
public interface UserService {

    User get(Integer id);

    User get(Integer id, String name);
}
