package com.bxtpw.common.utils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 字符串编码工具类, 转码，编码
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/3/1 15:58
 * @since 0.1
 */
public class StringEncodingUtils {
    /**
     * UTF-8编码
     */
    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * ISO-8859-1编码
     */
    private static final Charset ISO88591 = Charset.forName("ISO-8859-1");

    /**
     * 是否是ISO-8859-1编码
     *
     * @param source 源字符串
     * @return 返回是否是ISO-8859-1编码
     */
    public static boolean isISO88591Encoding(String source) {
        if (null == source || "".equals(source.trim())) return true;
        if (source.equals(new String(source.getBytes(ISO88591), ISO88591))) {
            return true;
        }
        return false;
    }

    /**
     * 获取UTF8格式的字符串
     *
     * @param source 源字符串
     * @return 返回UTF-8格式的字符串
     */
    public static String getUTF8String(String source) {
        if (null == source || "".equals(source)) return source;
        if (isISO88591Encoding(source)) {
            return new String(source.getBytes(ISO88591), UTF8);
        }
        return source;
    }

    /**
     * 将对象的字符串属性转成UTF-8格式
     *
     * @param object 要转换字符串属性格式的对象
     */
    public static void filterStringToUTF8Format(Object object) {
        List<Field> stringFields = ReflectUtils.getDeclaredFields(object.getClass(), String.class);
        if (null != stringFields && !stringFields.isEmpty()) {
            for (Field field : stringFields) {
                Object obj = ReflectUtils.getFieldValue(object, field);
                ReflectUtils.setFieldValue(object, field, getUTF8String(null == obj ? null : obj.toString()));
            }
        }

    }
}
