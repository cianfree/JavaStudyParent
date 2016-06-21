package com.bxtpw.common.url;

/**
 * <pre>
 * 只需包含某部分URL就能匹配策略
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/22 13:46
 * @since 0.1
 */
public final class ContainsUrlMatcherStrategy implements UrlMatcherStrategy {

    /**
     * 被包含的字符串
     */
    private String pattern;

    @Override
    public boolean matches(final String url) {
        return url.contains(this.pattern);
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
