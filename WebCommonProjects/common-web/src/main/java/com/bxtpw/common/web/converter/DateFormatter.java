package com.bxtpw.common.web.converter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 日期格式化类
 * 
 * @author 夏集球
 * 
 * @time 2015年5月7日 下午1:38:58
 * @version 0.1
 * @since 0.1
 */
public class DateFormatter {

    /**
     * 允许转换的格转换格式
     */
    private static final Map<String, SimpleDateFormat> FORMATTER_MAP = new HashMap<String, SimpleDateFormat>();

    /**
     * 初始化默认支持的
     */
    static {
        FORMATTER_MAP.put("[0-9]{4}-[0-9]{1,2}", new SimpleDateFormat("yyyy-MM"));
        FORMATTER_MAP.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}", new SimpleDateFormat("yyyy-MM-dd"));
        FORMATTER_MAP.put("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        FORMATTER_MAP.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}", new SimpleDateFormat("yyyy/MM/dd"));
        FORMATTER_MAP.put("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        FORMATTER_MAP.put("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日", new SimpleDateFormat("yyyy年MM月dd日"));
        FORMATTER_MAP.put("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日 [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}", new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"));
        FORMATTER_MAP.put("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日 [0-9]{1,2}时[0-9]{1,2}分[0-9]{1,2}秒", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒"));
    }

    /**
     * 构造器私有化
     * 
     * @author 夏集球
     * @time 2015年5月7日 下午1:39:21
     * @since 0.1
     */
    private DateFormatter() {

    }

    /**
     * 获取指定的时间转换器
     * 
     * @author 夏集球
     * @time 2015年5月7日 下午1:49:08
     * @since 0.1
     * @param dateStr
     * @return
     */
    public static SimpleDateFormat getFormatter(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        Set<String> patterns = FORMATTER_MAP.keySet();
        for (String pattern : patterns) {
            if (dateStr.matches(pattern)) {
                return FORMATTER_MAP.get(pattern);
            }
        }
        return null;
    }
}
