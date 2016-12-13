package com.github.cianfree.ip;

/**
 * 比较类型
 *
 * @author Arvin
 * @time 2016/12/9 14:50
 */
public enum CompareType {

    /** 在之前 */
    BEFORE,

    /** 在之前，但有重合 */
    BEFORE_MIXED,

    /** 之后 */
    AFTER,

    /** 在之后，有重合 */
    AFTER_MIXED,

    /** 包含 */
    INCLUDE,

    /** 在...之间 */
    BETWEEN,

    /** 相等 */
    EQUALS

}
