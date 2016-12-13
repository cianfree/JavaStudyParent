package com.bxtpw.common.cache.utils;

/**
 * 缓存辅助 类
 * 
 * @author 夏集球
 * @time 2015年5月18日 上午10:48:42
 * @version 0.1
 * @since 0.1
 */
public class CacheUtils {
    /**
     * 用于保存 是否开启缓存的变量值
     */
    public static final ThreadLocal<Boolean> isOpenLocal = new ThreadLocal<Boolean>();
    
    private  CacheUtils(){
        
    } 
    

}
