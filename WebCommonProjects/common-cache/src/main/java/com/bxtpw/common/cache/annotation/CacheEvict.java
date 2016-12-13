package com.bxtpw.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 清除缓存
 * @author 夏集球
 * @time 2015年4月26日 上午10:02:34
 * @version 0.1
 * @since 0.1
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CacheEvict {
    
     
    /**
     * 缓存中的 key
     * 
     * @author 夏集球
     * @time 2015年4月26日 上午9:29:56
     * @version 0.1
     * @since 0.1
     * @return
     */
    String key(); 
    
    /**
     * 属性
    * @author 夏集球
    * @time 2015年4月26日 上午11:34:57
    * @version 0.1
    * @since 0.1
    * @return
     */
    String[] field() default "";
}
