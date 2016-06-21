package com.bxtpw.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 点击量缓存标签 用于当用户点击某篇文章 时 点击量不更新数据库中 而是放入缓存
 * 
 * @author 黎杰
 * @time 2015年5月18日 上午11:59:59
 * @version 0.1
 * @since 0.1
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface VisitorCache {
    /**
     * 表名
     * 
     * @author 黎杰
     * @time 2015年5月18日 下午12:01:42
     * @version 0.1
     * @since 0.1
     * @return
     */
    String tableName();

    /**
     * 字段
    * @author 黎杰
    * @time 2015年5月18日 下午12:02:09
    * @version 0.1
    * @since 0.1
    * @return
     */
    String tableColumn();
    /**
     * 表id值
    * @author 黎杰
    * @time 2015年5月18日 下午3:31:50
    * @version 0.1
    * @since 0.1
    * @return
     */
    
    String id();
    /**
     * 主键的名称
    * @author 黎杰
    * @time 2015年6月8日 上午11:28:30
    * @version 0.1
    * @since 0.1
    * @return
     */
    String  idName();

}
