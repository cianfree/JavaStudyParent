package com.bxtpw.common.web.query;

import com.bxtpw.common.utils.DateUtils;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Query对象工具类
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年8月12日 下午5:13:18
 * @since 0.1
 */
public class QueryUtils {

    /**
     * 开始时间验证
     */
    protected static final String REGEX_BEG_TIME = "(?i)^((beg*)|(begin.*)|(start.*)).*";
    /**
     * 结束验证时间
     */
    protected static final String REGEX_END_TIME = "(?i)^(end.*).*";

    /**
     * 重置日期类型的查询条件 把beg*,start* begin*开头的，重新设置时间为最早的 把end* 开头的时间，重新设置为一天中最晚的时间
     *
     * @param obj
     * @author 夏集球
     * @time 2015年8月12日 下午5:15:12
     * @since 0.1
     */
    public static void resetDateCondition(Object obj) {
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] fields = null;
            //循环取父类的fields
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                fields = clazz.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        checkAndResetQueryDate(obj, field);
                    }
                }
            }
        }
    }

    /**
     * 检查并重置时间
     *
     * @param obj
     * @param field
     * @author 夏集球
     * @time 2015年8月12日 下午5:31:39
     * @since 0.1
     */
    private static void checkAndResetQueryDate(Object obj, Field field) {
        if (isDateType(field)) {
            if (isBegTime(field.getName())) {
                resetBegTime(field, obj);
            } else if (isEndTime(field.getName())) {
                resetEndTime(field, obj);
            }
        }
    }

    /**
     * 重置结束时间
     *
     * @param field
     * @param obj
     * @author 夏集球
     * @time 2015年8月12日 下午5:29:24
     * @since 0.1
     */
    private static void resetEndTime(Field field, Object obj) {
        field.setAccessible(true);
        try {
            Object time = field.get(obj);
            if (time != null) {
                field.set(obj, DateUtils.getDayEnd((Date) time));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
        }
    }

    /**
     * 重置开始时间
     *
     * @param field
     * @param obj
     * @author 夏集球
     * @time 2015年8月12日 下午5:29:06
     * @since 0.1
     */
    private static void resetBegTime(Field field, Object obj) {
        field.setAccessible(true);
        try {
            Object time = field.get(obj);
            if (time != null) {
                field.set(obj, DateUtils.getDayBegin((Date) time));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
        }
    }

    /**
     * 是否是结束时间属性
     *
     * @param name
     * @return
     * @author 夏集球
     * @time 2015年8月12日 下午5:26:59
     * @since 0.1
     */
    private static boolean isEndTime(String name) {
        return name.matches(REGEX_END_TIME);
    }

    /**
     * 是否是开始时间属性
     *
     * @param name
     * @return
     * @author 夏集球
     * @time 2015年8月12日 下午5:26:15
     * @since 0.1
     */
    private static boolean isBegTime(String name) {
        return name.matches(REGEX_BEG_TIME);
    }

    /**
     * 是否是需要进行重置的属性
     *
     * @param field
     * @return
     * @author 夏集球
     * @time 2015年8月12日 下午5:24:02
     * @since 0.1
     */
    private static boolean isDateType(Field field) {
        return field.getType() == Date.class;
    }

    /**
     * 系统默认的pageSize
     */
    public static final int SYSTEM_DEFAULT_PAGE_SIZE = 5;
    /**
     * 系统默认的最大PageSIze
     */
    public static final int SYSTEM_DEFAULT_MAX_PAGE_SIZE = Integer.MAX_VALUE;

    /**
     * 重置分页查询参数
     *
     * @param query           查询对象
     * @param defaultPageSize 默认的pageSize
     * @param maxPageSize     pageSize的最大值
     */
    public static void resetPageParameter(PageBaseQuery query, Integer defaultPageSize, Integer maxPageSize) {
        int realDefaultPageSize = null == defaultPageSize || defaultPageSize <= 0 ? SYSTEM_DEFAULT_PAGE_SIZE : defaultPageSize;
        int realMaxPageSize = null == maxPageSize || maxPageSize <= 0 ? SYSTEM_DEFAULT_MAX_PAGE_SIZE : maxPageSize;
        int realPageSize = query.getPageSize() <= 0 ? realDefaultPageSize : query.getPageSize() > realMaxPageSize ? realMaxPageSize : query.getPageSize();
        int realPageNumber = query.getPageNumber() <= 0 ? 0 : query.getPageNumber();
        query.setPageSize(realPageSize);
        query.setPageNumber(realPageNumber);
    }
}
