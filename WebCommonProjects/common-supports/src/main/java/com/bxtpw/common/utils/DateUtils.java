package com.bxtpw.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 * 时间工具类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 16:32
 * @since 0.1
 */
public class DateUtils {

    /**
     * 禁止外部实例化
     */
    private DateUtils() {
    }

    /**
     * 判断给定的时间是否是属于当月内的
     *
     * @param time 要判断的时间
     * @return
     */
    public static boolean isTimeInCurrentMonth(Date time) {
        if (null == time) {
            return false;
        } else {
            Date currentTime = new Date();
            Calendar currentTimeCal = Calendar.getInstance();
            currentTimeCal.setTime(currentTime);
            Calendar givenTimeCal = Calendar.getInstance();
            givenTimeCal.setTime(time);
            return currentTimeCal.get(Calendar.YEAR) == givenTimeCal.get(Calendar.YEAR)//
                    && //
                    currentTimeCal.get(Calendar.MONTH) == givenTimeCal.get(Calendar.MONTH);
        }
    }

    /**
     * 获取给定的两个日其中更早的一个
     *
     * @param first  要检查的第一个时间
     * @param second 要检查的第二个时间
     * @return
     */
    public static Date getEarlyTime(Date first, Date second) {
        if (isBothNotNull(first, second)) {
            return first.before(second) ? first : second;
        }
        return null != first ? first : second;
    }

    /**
     * 获取两个时间中更晚的时间
     *
     * @param first  要检查的第一个时间
     * @param second 要检查的第二个时间
     * @return
     */
    public static Date getLaterTime(Date first, Date second) {
        if (isBothNotNull(first, second)) {
            return first.after(second) ? first : second;
        }
        return null != first ? first : second;
    }

    /**
     * 检测一个或多个时间是否都不为空
     *
     * @param times 需要检测的一个或多个时间
     * @return
     */
    public static boolean isBothNotNull(Date... times) {
        if (null != times && times.length > 0) {
            for (Date time : times) {
                if (null == time) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取指定时间的最开始，比如传入 2015-08-08, 返回 2015-08-08 00:00:00
     *
     * @param time
     * @return
     */
    public static Date getDayBegin(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定时间的最后时间，比如传入 2015-08-08, 返回 2015-08-08 23:59:59
     *
     * @param time
     * @return
     */
    public static Date getDayEnd(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
}
