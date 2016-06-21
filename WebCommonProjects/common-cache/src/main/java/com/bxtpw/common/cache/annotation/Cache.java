package com.bxtpw.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于处理缓存读取 以及保存 数据到缓存中
 * 
 * @author 黎杰
 * @time 2015年4月26日 上午9:26:38
 * @version 0.1
 * @since 0.1
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * 缓存中的 key
     * 
     * @author 黎杰
     * @time 2015年4月26日 上午9:29:56
     * @version 0.1
     * @since 0.1
     * @return
     */
    String key();

    /**
     * 属性
     * 
     * @author 黎杰
     * @time 2015年4月26日 上午11:34:57
     * @version 0.1
     * @since 0.1
     * @return
     */
    String field() default "";

    /**
     * 缓存过期时间 默认 3600 根据具体业务（不改变的数据）可以设置更长 ，如 系统中的 区域信息
     * 
     * @author 黎杰
     * @time 2015年4月26日 上午9:31:02
     * @version 0.1
     * @since 0.1
     * @return
     */
    int expire() default 3600;

    /**
     * 缓存条件
     * 
     * @author 黎杰
     * @time 2015年7月4日 上午8:33:06
     * @version 0.1
     * @since 0.1
     * @return
     */
    String condition() default "";

}
