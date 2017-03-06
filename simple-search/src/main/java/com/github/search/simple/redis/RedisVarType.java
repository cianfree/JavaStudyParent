package com.github.search.simple.redis;

/**
 * Redis 参数类型
 *
 * @author Arvin
 * @time 2017/1/9 23:02
 */
public class RedisVarType {

    /** key 值不存在 */
    public static final String NONE = "none";

    /** 字符串 */
    public static final String STRING = "string";

    /** List */
    public static final String LIST = "list";

    /** 集合 */
    public static final String SET = "set";

    /** 有序集合 */
    public static final String ZSET = "zset";

    /** 哈希表 */
    public static final String HASH = "hash";
}
