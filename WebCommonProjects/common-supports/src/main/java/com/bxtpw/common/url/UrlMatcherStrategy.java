package com.bxtpw.common.url;

/**
 * <pre>
 * URL匹配策略接口定义
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/22 13:42
 * @since 0.1
 */
public interface UrlMatcherStrategy {
    /**
     * 检测指定的URL是否符合指定的匹配规则
     * @param url
     * @return
     */
    boolean matches(String url);

    /**
     * 设置匹配表达式
     * @param pattern
     */
    void setPattern(String pattern);
}
