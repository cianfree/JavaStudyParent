package com.bxtpw.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来根据条件判断是否开启缓存
 * 
 * @author 黎杰
 * @time 2015年4月26日 上午9:26:38
 * @version 0.1
 * @since 0.1
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface IsCache {
    /**
     * 
     * 是否开启缓存表达式
     * 
     * @author 黎杰
     * @time 2015年4月26日 上午9:29:56
     * @version 0.1
     * @since 0.1
     * @return
     */
    String value() default "";
}
