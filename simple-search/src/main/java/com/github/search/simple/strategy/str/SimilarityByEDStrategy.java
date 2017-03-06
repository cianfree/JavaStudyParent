package com.github.search.simple.strategy.str;

import com.github.search.simple.utils.EditDistanceUtils;

/**
 * 基于编辑距离计算的相似度策略
 *
 * @author Arvin
 * @time 2016/10/11$ 10:46$
 */
public class SimilarityByEDStrategy extends AbstractStrategy {

    private static class Holder {
        private static final SimilarityByEDStrategy INSTANCE = new SimilarityByEDStrategy();
    }

    public static SimilarityByEDStrategy getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float accept(String keyword, String checkContent) {
        float similarity = checkContent.equals(keyword) ? 1F : EditDistanceUtils.similarity(keyword, checkContent);
        return similarity > 0F ? similarity : NOT_SIMILARITY;
    }
}
