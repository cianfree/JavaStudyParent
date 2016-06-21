package com.bxtpw.common.exception;

/**
 * Error Code 接口
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/3 9:18
 * @since 0.1
 */
public interface JsonAndCodeExceptionInterface<T> {

    /**
     * 获取错误编码
     *
     * @return 返回错误编码，默认是500
     */
    String getErrorCode();

    /**
     * 设置错误码
     *
     * @param errorCode 错误码
     */
    T setErrorCode(String errorCode);

    /**
     * 是否是Json格式的错误消息
     */
    boolean isJson();

    /**
     * 设置是否是Json格式的异常信息
     *
     * @param isJson 是否是Json格式异常
     */
    T setJson(boolean isJson);
}
