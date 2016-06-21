package com.bxtpw.common.url;

import java.util.regex.Pattern;

/**
 * <pre>
 * 正则匹配策略实现
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/22 13:44
 * @since 0.1
 */
public final class RegexUrlMatcherStrategy implements UrlMatcherStrategy {

    /**
     * 用于匹配的正则表达式
     */
    private Pattern pattern;

    @Override
    public boolean matches(final String url) {
        return null != url && this.pattern.matcher(url).find();
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }
}