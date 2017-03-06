package com.github.search.simple.strategy.str;

/**
 * 等值策略
 *
 * @author Arvin
 * @time 2016/10/11$ 10:46$
 */
public class EqualsStrategy extends AbstractStrategy {

    private static class Holder {
        private static final EqualsStrategy INSTANCE = new EqualsStrategy();
    }

    public static EqualsStrategy getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float accept(String keyword, String checkContent) {
        return keyword.equals(checkContent) ? 1 : NOT_SIMILARITY;
    }
}
