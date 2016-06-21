package com.bxtpw.common.exception;

/**
 * 错误编码
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/3 10:01
 * @since 0.1
 */
public class ErrorCode {

    /**
     * 内部错误前缀编码
     */
    public static final String PREFIX_INTERNAL = "INTERNAL_";

    /**
     * 内部500错误
     */
    public static final String INTERNAL_500 = PREFIX_INTERNAL + "500";

    /**
     * 参数校验错误异常错误码前缀
     */
    public static final String PREFIX_PARAMS_VALIDATE = "PV_";

    /**
     * 参数校验不正确
     */
    public static final String PARAMS_VALIDATE_400 = PREFIX_PARAMS_VALIDATE + "400";

    /**
     * 业务异常错误码前缀
     */
    public static final String PREFIX_BUSINESS = "BU_";
}
