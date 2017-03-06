package com.github.search.simple.analyzer;

import java.util.HashSet;
import java.util.Set;

/**
 * 单字分词器，会把每一个字或单词，数字进行切分
 *
 * @author Arvin
 * @time 2016/12/8 9:11
 */
public class OneWordAnalyzer extends AbstractAnalyzer {

    private static class Holder {
        private static final OneWordAnalyzer INSTANCE = new OneWordAnalyzer();
    }

    public static OneWordAnalyzer getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Set<String> analyzer(String content, String prefix) {
        Set<String> subContents = splitContentByNotChineseAndEnWord(content);
        Set<String> result = new HashSet<>();
        for (String subContent : subContents) {
            Set<String> subResult = analyzerPlainContent(result, subContent, prefix);
            if (null != subResult && !subResult.isEmpty()) {
                result.addAll(subResult);
            }
        }
        return result;
    }

    protected Set<String> analyzerPlainContent(Set<String> result, String content, String prefix) {

        result = null == result ? new HashSet<String>() : result;

        int len = null == content ? 0 : content.length();
        if (len > 0) {
            StringBuilder numberLetterBuilder = new StringBuilder();
            for (int i = 0; i < len; ++i) {
                char c = content.charAt(i);
                if ((c >= 48 && c <= 57) || (c >= 97 && c <= 122) || (c >= 65 && c <= 90)) { // 数字或字母
                    numberLetterBuilder.append(c); // 追加同一个词
                } else { // 当前是中文，添加到结果列表
                    result.add(null == prefix ? String.valueOf(c) : prefix + String.valueOf(c));
                    if (numberLetterBuilder.length() > 0) {
                        result.add(null == prefix ? numberLetterBuilder.toString() : prefix + numberLetterBuilder.toString());
                        numberLetterBuilder.setLength(0); // 重置
                    }
                }
            }
            // 最后，检查是否为空
            if (numberLetterBuilder.length() > 0) {
                result.add(null == prefix ? numberLetterBuilder.toString() : prefix + numberLetterBuilder.toString());
            }
        }
        return result;
    }
}
