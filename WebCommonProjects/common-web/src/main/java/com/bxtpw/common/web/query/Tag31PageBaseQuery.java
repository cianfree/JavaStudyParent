package com.bxtpw.common.web.query;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 31位以内查询基类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/11 20:13
 * @since 0.1
 */
public abstract class Tag31PageBaseQuery extends PageBaseQuery {

    /**
     * Tag条件存储
     */
    private Map<Long, Boolean> conditions = new HashMap<>();

    /**
     * 条件True的tag值
     */
    private int trueTag;

    /**
     * 条件false的tag值
     */
    private int falseTag;

    /**
     * 添加TAG条件
     *
     * @param bool
     * @param constant
     */
    public void addTagCondition(Boolean bool, Long constant) {
        // 原来有这个条件，先删除
        if (bool != conditions.get(constant)) {
            // 移除指定key的条件
            removeTagCondition(constant);
        }
        // 添加到条件中
        addToTagCondition(bool, constant);
    }

    /**
     * 添加到tag条件中
     *
     * @param bool
     * @param constant
     */
    private void addToTagCondition(Boolean bool, Long constant) {
        if (null != bool) {
            if (bool) {
                trueTag |= constant;
            } else {
                falseTag ^= constant;
            }
            conditions.put(constant, bool);
        }
    }

    /**
     * 移除指定Key的条件
     *
     * @param constant
     */
    private void removeTagCondition(Long constant) {
        Boolean oldBool = conditions.get(constant);
        if (null != oldBool) {
            if (oldBool) {
                trueTag ^= constant;
            } else {
                falseTag ^= constant;
            }
            conditions.remove(constant);
        }
    }

    public int getTrueTag() {
        return trueTag;
    }

    public int getFalseTag() {
        return falseTag;
    }

    /**
     * 清空条件
     */
    public void clearTagCondition() {
        this.trueTag = 0;
        this.falseTag = 0;
        this.conditions.clear();
    }
}
