package com.bxtpw.common.url;

/**
 * <pre>
 * URL全匹配策略
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/22 13:45
 * @since 0.1
 */
public final class ExactUrlMatcherStrategy implements UrlMatcherStrategy {

    /**
     * 匹配的URL
     */
    private String pattern;

    @Override
    public boolean matches(final String url) {
        return url.equals(this.pattern);
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}