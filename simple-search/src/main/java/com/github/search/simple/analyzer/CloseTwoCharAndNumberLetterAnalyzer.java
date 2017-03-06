package com.github.search.simple.analyzer;

import java.util.HashSet;
import java.util.Set;

/**
 * 临近及其连续数字,连续字母分词器
 * <p/>
 * "小学100字hello作文文学" 的分词结果为：
 * <p/>
 * 小学， 100， hello， 作文，文文，文学
 *
 * @author Arvin
 * @time 2016/12/8 9:18
 */
public class CloseTwoCharAndNumberLetterAnalyzer extends AbstractAnalyzer {

    /** 叹词 */
    private static final String TAN_CI_REGEX = "[啊咦嘿嗨嚯哟哈嘻呵唉哎呸啐哼]$";

    private static class Holder {
        private static CloseTwoCharAndNumberLetterAnalyzer INSTANCE = new CloseTwoCharAndNumberLetterAnalyzer();
    }

    public static CloseTwoCharAndNumberLetterAnalyzer getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Set<String> analyzer(String content, String prefix) {
        Set<String> result = new HashSet<>();
        String notChineseContent = filterNotChineseWord(content);
        if (notChineseContent.length() < 3) {
            result.add(null != prefix ? prefix + notChineseContent : notChineseContent);
            return result;
        }
        Set<String> subContents = splitContentByNotChineseAndEnWord(content);
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
        if (len > 1) {
            StringBuilder numberLetterBuilder = new StringBuilder();
            StringBuilder notNumberBuilder = new StringBuilder(2);
            boolean preIsNumberOrLetter = false;
            String temp;
            for (int i = 0; i < len; ++i) {
                char c = content.charAt(i);
                if ((c >= 48 && c <= 57) || (c >= 97 && c <= 122) || (c >= 65 && c <= 90)) { // 数字或字母
                    if (!preIsNumberOrLetter) {  // 前一个不是数字或字母，如果还没有超过两个中文，清空
                        if (notNumberBuilder.length() != 2) {
                            notNumberBuilder.setLength(0); // 清空
                        }
                    }
                    numberLetterBuilder.append(c); // 追加同一个词
                    preIsNumberOrLetter = true;
                } else {
                    if (!preIsNumberOrLetter) {  // 前面一个是中文，追加
                        if (notNumberBuilder.length() < 2) {
                            notNumberBuilder.append(c);
                        }
                        // 检测长度，如果为2就截取一个词
                        if (notNumberBuilder.length() == 2) {
                            temp = notNumberBuilder.toString();
                            if (!temp.matches(TAN_CI_REGEX)) {
                                result.add(prefix == null ? temp : prefix + temp);
                            }
                            notNumberBuilder.setLength(0); // 重置
                            i--; // 截取后，往前继续计算一次，计算每每相邻的中文字符组成一个双子词组
                        }
                    } else { // 前面一个是数字，将数字和字母进行词转换
                        if (numberLetterBuilder.length() > 1) {
                            result.add(prefix == null ? numberLetterBuilder.toString() : prefix + numberLetterBuilder.toString());
                            numberLetterBuilder.setLength(0); // 重置
                        }
                        notNumberBuilder.append(c);
                    }
                    preIsNumberOrLetter = false;
                }
            }
            // 最后进行一次检测
            if (numberLetterBuilder.length() > 1) {
                result.add(prefix == null ? numberLetterBuilder.toString() : prefix + numberLetterBuilder.toString());
            }
            if (notNumberBuilder.length() == 2) {
                temp = notNumberBuilder.toString();
                if (!temp.matches(TAN_CI_REGEX)) {
                    result.add(prefix == null ? temp : prefix + temp);
                }
            }
        }
        return result;
    }
}
