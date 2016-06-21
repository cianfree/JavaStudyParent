package com.bxtpw.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 * 字符串工具类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 16:25
 * @since 0.1
 */
public class StrUtils {

    /**
     * 禁止外部实例化
     */
    private StrUtils() {
    }

    /**
     * MD5加密十六进制表示字符数组
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 对指定的字符串进行MD5加密
     *
     * @param val
     * @return
     */
    public static String md5Hex(final String val) {
        try {
            byte[] btInput = val.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 指定字符串是否是空串或是null,或是空白字符，trim之后为空字符串
     *
     * @param val 要检查的字符串
     * @return
     */
    public static boolean isBlank(final String val) {
        return null == val || "".equals(val.trim());
    }

    /**
     * 判断指定的字符串是否不是空串，与isBlank相反，即不是isBlank的就是isNotBlank
     *
     * @param val 要检查的字符串
     * @return
     */
    public static boolean isNotBlank(final String val) {
        return null != val && !"".equals(val.trim());
    }

    /**
     * 获取字符串
     *
     * @param strValue     源字符串
     * @param defaultValue 默认字符串
     * @return
     */
    public static String getString(String strValue, String defaultValue) {
        return isNotBlank(strValue) ? strValue : defaultValue;
    }
}
