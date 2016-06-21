package edu.zhku.hessian.auth;

/**
 * Created by Arvin on 2016/6/7.
 */
public class UnAuthException extends RuntimeException {

    public UnAuthException() {
        super("Un Auth Request!");
    }
}
