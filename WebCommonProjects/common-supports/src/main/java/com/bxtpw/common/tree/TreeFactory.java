package com.bxtpw.common.tree;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 家谱树工厂， 提供默认的Reader和Writer
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年12月2日 下午4:07:59
 * @since 0.1
 */
public class TreeFactory {

    /**
     * 主键ID属性名称
     */
    private static final String DEFAULT_ID_FILED_NAME = "id";

    /**
     * 默认父级属性名称
     */
    private static final String DEFAULT_PARENT_FILED_NAME = "parent";

    /**
     * 默认子级属性名称
     */
    private static final String DEFAULT_CHILDREN_FILED_NAME = "children";

    private TreeFactory() {
    }

    /**
     * 单例
     *
     * @author 夏集球
     * @version 0.1
     * @time 2015年12月2日 下午4:12:43
     * @since 0.1
     */
    private interface NodeObjectReader {
        /**
         * 读取主键
         *
         * @param obj
         * @return
         * @author 夏集球
         * @time 2015年12月2日 下午3:56:30
         * @version 0.1
         * @since 0.1
         */
        public Object getPrimarkKey(Object obj);

        /**
         * 获取父级元素
         *
         * @param obj
         * @return
         * @author 夏集球
         * @time 2015年12月2日 下午3:42:07
         * @version 0.1
         * @since 0.1
         */
        public Object getParent(Object obj);

        /**
         * 获取子级元素
         *
         * @param obj
         * @return
         * @author 夏集球
         * @time 2015年12月2日 下午3:42:36
         * @version 0.1
         * @since 0.1
         */
        public List<?> getChildren(Object obj);

        /**
         * 默认的反射节点读取器
         */
        static final NodeObjectReader ReflectReader = new NodeObjectReader() {

            @Override
            public Object getPrimarkKey(Object obj) {
                return getFieldValue(obj, DEFAULT_ID_FILED_NAME);
            }

            @Override
            public Object getParent(Object obj) {
                return getFieldValue(obj, DEFAULT_PARENT_FILED_NAME);
            }

            @Override
            public List<?> getChildren(Object obj) {
                return (List<?>) getFieldValue(obj, DEFAULT_CHILDREN_FILED_NAME);
            }

            /**
             * 获取属性
             * @author 夏集球
             * @time 2015年12月2日 下午4:17:00
             * @version 0.1
             * @since 0.1
             * @param obj
             * @param fieldName
             * @return
             */
            private Object getFieldValue(Object obj, String fieldName) {
                if (null != obj) {
                    try {
                        Field idField = findField(obj.getClass(), fieldName);
                        idField.setAccessible(true);
                        return idField.get(obj);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        };
    }

    /**
     * 单例写操作器
     *
     * @author 夏集球
     * @version 0.1
     * @time 2015年12月2日 下午4:12:43
     * @since 0.1
     */
    private interface NodeObjectWriter {
        /**
         * 设置父级元素
         *
         * @param obj
         * @param parent
         * @return
         * @author 夏集球
         * @time 2015年12月2日 下午3:42:07
         * @version 0.1
         * @since 0.1
         */
        public void setParent(Object obj, Object parent);

        /**
         * 设置子级元素
         *
         * @param obj
         * @param children
         * @return
         * @author 夏集球
         * @time 2015年12月2日 下午3:42:36
         * @version 0.1
         * @since 0.1
         */
        public void setChildren(Object obj, List<?> children);

        /**
         * 默认的反射节点写操作器
         */
        static final NodeObjectWriter ReflectWriter = new NodeObjectWriter() {

            @Override
            public void setParent(Object obj, Object parent) {
                setFieldValue(obj, DEFAULT_PARENT_FILED_NAME, parent);
            }

            @Override
            public void setChildren(Object obj, List<?> children) {
                setFieldValue(obj, DEFAULT_CHILDREN_FILED_NAME, children);
            }

            /**
             * 获取属性
             * @author 夏集球
             * @time 2015年12月2日 下午4:17:00
             * @version 0.1
             * @since 0.1
             * @param obj
             * @param fieldName
             * @return
             */
            private void setFieldValue(Object obj, String fieldName, Object fieldValue) {
                if (null != obj) {
                    try {
                        Field idField = findField(obj.getClass(), fieldName);
                        idField.setAccessible(true);
                        idField.set(obj, fieldValue);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        };
    }

    /**
     * 创建默认的读取器
     *
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:11:48
     * @version 0.1
     * @since 0.1
     */
    @SuppressWarnings("unchecked")
    public static final <T, K> NodeReader<T, K> createDefaultReader() {
        return new NodeReader<T, K>() {

            private static final long serialVersionUID = 1L;

            @Override
            public K getPrimarkKey(T obj) {
                return (K) NodeObjectReader.ReflectReader.getPrimarkKey(obj);
            }

            @Override
            public T getParent(T obj) {
                return (T) NodeObjectReader.ReflectReader.getParent(obj);
            }

            @Override
            public List<T> getChildren(T obj) {
                return (List<T>) NodeObjectReader.ReflectReader.getChildren(obj);
            }
        };
    }

    /**
     * 构造默认的节点写操作器
     *
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午4:28:15
     * @version 0.1
     * @since 0.1
     */
    public static final <T> NodeWriter<T> createDefaultWriter() {
        return new NodeWriter<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void setParent(T obj, T parent) {
                NodeObjectWriter.ReflectWriter.setParent(obj, parent);
            }

            @Override
            public void setChildren(T obj, List<T> children) {
                NodeObjectWriter.ReflectWriter.setChildren(obj, children);
            }
        };
    }

    /**
     * 搜索Field
     *
     * @param clazz
     * @param fieldName
     * @return
     * @author 夏集球
     * @time 2015年12月2日 下午5:32:40
     * @version 0.1
     * @since 0.1
     */
    private static Field findField(Class<?> clazz, String fieldName) {
        Class<?> superClass = clazz;
        while (!superClass.equals(Object.class)) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                if (null != field) {
                    return field;
                }
            } catch (NoSuchFieldException | SecurityException e) {
                superClass = superClass.getSuperclass();
                continue;
            }
        }
        return null;

    }
}
