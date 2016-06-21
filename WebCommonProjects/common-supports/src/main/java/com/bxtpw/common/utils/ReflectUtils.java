package com.bxtpw.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * 反射工具类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 16:52
 * @since 0.1
 */
public class ReflectUtils {

    /**
     * 禁止外部实例化
     */
    private ReflectUtils() {
    }

    /**
     * 查找指定类型的属性列表
     *
     * @param clazz     类名
     * @param fieldType 字段类型， 如果为空就返回所有的字段
     * @return 返回指定类型的属性列表，如果为空就返回所有的字段
     */
    public static List<Field> getDeclaredFields(Class<?> clazz, Class<?> fieldType) {
        Field[] declaredFields = clazz.getDeclaredFields();
        if (null != declaredFields && declaredFields.length > 0) {
            if (null == fieldType) return Arrays.asList(declaredFields);
            List<Field> fields = new ArrayList<>();
            for (Field field : declaredFields) {
                if (field.getType().equals(fieldType)) {
                    fields.add(field);
                }
            }
            return fields;
        }
        return new ArrayList<>();
    }

    /**
     * 获取字段的值
     *
     * @param obj
     * @param field
     * @return
     */
    public static Object getFieldValue(Object obj, Field field) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 设置属性的值
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setFieldValue(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 搜索字段，包含私有的，从当前类开始搜索，如果当前类没有，继续往父类中查找，直到找到或到Object为止
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field findDeclaredField(Class<?> clazz, String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 搜索字段，只会搜索出public的，从当前类开始搜索，如果当前类没有，继续往父类中查找，直到找到或到Object为止
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field findPublicField(Class<?> clazz, String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getField(fieldName);
            } catch (NoSuchFieldException | SecurityException e) {
            }
        }
        return null;
    }


    /**
     * 搜索方法包含私有的，从当前类开始搜索，如果当前类没有，继续往父类中查找，直到找到或到Object为止
     *
     * @param clazz
     * @param methodName
     * @param parameterTypes 参数类型
     * @return
     */
    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException | SecurityException e) {
            }
        }
        return null;
    }

    /**
     * 搜索方法包含public的，从当前类开始搜索，如果当前类没有，继续往父类中查找，直到找到或到Object为止
     *
     * @param clazz
     * @param methodName
     * @param parameterTypes 参数类型
     * @return
     */
    public static Method findPublicMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException | SecurityException e) {
            }
        }
        return null;
    }
}
