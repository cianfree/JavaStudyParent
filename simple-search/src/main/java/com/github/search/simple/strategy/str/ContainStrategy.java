package com.github.search.simple.strategy.str;

/**
 * 包含策略，长度相差越少相似度越高
 *
 * @author Arvin
 * @time 2016/10/11$ 10:46$
 */
public class ContainStrategy extends AbstractStrategy {

    private static class Holder {
        private static final ContainStrategy INSTANCE = new ContainStrategy();
    }

    public static ContainStrategy getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float accept(String keyword, String checkContent) {
        if (checkContent.contains(keyword)) {
            int keywordLen = keyword.length();
            int contentLen = checkContent.length();
            return keywordLen > contentLen ? (float) contentLen / keywordLen : (float) keywordLen / contentLen;
        }
        return NOT_SIMILARITY;
    }
}
