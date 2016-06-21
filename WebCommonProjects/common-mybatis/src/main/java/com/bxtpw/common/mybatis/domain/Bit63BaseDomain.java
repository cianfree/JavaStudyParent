package com.bxtpw.common.mybatis.domain;

import java.io.Serializable;

/**
 * <pre>
 * 63位以内BIT类型存储实体基类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 17:20
 * @since 0.1
 */
public abstract class Bit63BaseDomain implements Serializable {
    /**
     * 设置tag
     *
     * @param currentTag 当前tag的值
     * @param constant   常量值
     * @param bool       对应常量的布尔值
     * @return
     */
    protected long calculateNewTagForBoolean(long currentTag, long constant, boolean bool) {
        if (bool) {
            return currentTag | constant;
        } else {
            return currentTag ^ constant;
        }
    }

    /**
     * 按位判断是否指定位的值是true
     *
     * @param tag      包含多个条件的值
     * @param constant 要判断的位
     * @return
     */
    protected boolean isBitTrue(long tag, long constant) {
        return (tag & constant) == constant;
    }

}
