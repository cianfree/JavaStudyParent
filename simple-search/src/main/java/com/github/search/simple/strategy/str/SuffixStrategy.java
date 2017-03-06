package com.github.search.simple.strategy.str;

/**
 * 后缀策略， 前缀相同，长度相差越少相似度越高
 *
 * @author Arvin
 * @time 2016/10/11$ 10:46$
 */
public class SuffixStrategy extends AbstractStrategy {

    private static class Holder {
        private static final SuffixStrategy INSTANCE = new SuffixStrategy();
    }

    public static SuffixStrategy getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public float accept(String keyword, String checkContent) {
        if (checkContent.endsWith(keyword)) {
            int keywordLen = keyword.length();
            int contentLen = checkContent.length();
            return keywordLen > contentLen ? (float) contentLen / keywordLen : (float) keywordLen / contentLen;
        }
        return NOT_SIMILARITY;
    }
}
