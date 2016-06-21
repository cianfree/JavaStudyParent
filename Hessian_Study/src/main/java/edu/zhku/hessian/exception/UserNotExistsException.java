package edu.zhku.hessian.exception;

/**
 * Created by Arvin on 2016/6/5.
 */
public class UserNotExistsException extends RuntimeException {

    public UserNotExistsException() {
        super("用户不存在！");
    }
}
