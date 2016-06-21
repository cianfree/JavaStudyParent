package com.bxtpw.common.utils;

/**
 * <pre>
 * 数字工具类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 16:41
 * @since 0.1
 */
public class NumberUtils {

    /**
     * 数字工具类
     */
    private NumberUtils() {
    }

    /**
     * 数组转long， 63位的可以正常转换，只能表示正数
     *
     * @param bytes
     * @return
     */
    public static long bytes2PositiveLong(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return 0;
        }
        // 边界检查，超过63位的无法转换
        if (bytes.length > 63) throw new NumberFormatException("Bytes too long, less 64 is required");
        int len = bytes.length;
        long result = 0;
        for (int i = len - 1; i >= 0; --i) {
            result |= bytes[i] << i;
        }
        return result;
    }

    /**
     * bytes数字转十进制int， 31位的bytes可正常转换，只能表示正数
     *
     * @param bytes
     * @return
     */
    public static int bytes2PositiveInt(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return 0;
        }
        if (bytes.length > 31) throw new NumberFormatException("Bytes too long, less 31 is required");
        int len = bytes.length;
        int result = 0;
        for (int i = len - 1; i >= 0; --i) {
            result |= bytes[i] << i;
        }
        return result;
    }

    /**
     * 获取一个正数据，如果为空或小于等于0，返回默认值
     *
     * @param intValue
     * @param defaultValue
     * @return
     */
    public static Integer getPositiveInteger(Integer intValue, Integer defaultValue) {
        return null == intValue || intValue <= 0 ? defaultValue : intValue;
    }
}
