package com.github.search.simple.analyzer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @time 2016/12/8 10:23
 */
public abstract class AbstractAnalyzer implements Analyzer {

    /** 非中文，数字，字母 */
    public final static String NOT_CHINESE_EN_WORD = "[^\\u4e00-\\u9fa5a-zA-Z0-9]+";

    @Override
    public Set<String> analyzer(String content) {
        return analyzer(content, null);
    }

    /**
     * 将内容进行非中文数字单词进行切分
     *
     * @param content 要切分的字符串
     */
    protected Set<String> splitContentByNotChineseAndEnWord(String content) {
        Set<String> subContents = new HashSet<>();
        if (null != content) {
            String[] subContentArr = content.split(NOT_CHINESE_EN_WORD);
            if (subContentArr.length > 0) {
                for (String subContent : subContentArr) {
                    subContents.add(subContent);
                }
            }
        }
        return subContents;
    }

    protected String filterNotChineseWord(String content) {
        return content.replaceAll(NOT_CHINESE_EN_WORD, "");
    }
}
