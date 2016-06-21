package com.bxtpw.common.tree;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ������������ �ṩĬ�ϵ�Reader��Writer
 *
 * @author �ļ���
 * @version 0.1
 * @time 2015��12��2�� ����4:07:59
 * @since 0.1
 */
public class TreeFactory {

    /**
     * ����ID��������
     */
    private static final String DEFAULT_ID_FILED_NAME = "id";

    /**
     * Ĭ�ϸ�����������
     */
    private static final String DEFAULT_PARENT_FILED_NAME = "parent";

    /**
     * Ĭ���Ӽ���������
     */
    private static final String DEFAULT_CHILDREN_FILED_NAME = "children";

    private TreeFactory() {
    }

    /**
     * ����
     *
     * @author �ļ���
     * @version 0.1
     * @time 2015��12��2�� ����4:12:43
     * @since 0.1
     */
    private interface NodeObjectReader {
        /**
         * ��ȡ����
         *
         * @param obj
         * @return
         * @author �ļ���
         * @time 2015��12��2�� ����3:56:30
         * @version 0.1
         * @since 0.1
         */
        public Object getPrimarkKey(Object obj);

        /**
         * ��ȡ����Ԫ��
         *
         * @param obj
         * @return
         * @author �ļ���
         * @time 2015��12��2�� ����3:42:07
         * @version 0.1
         * @since 0.1
         */
        public Object getParent(Object obj);

        /**
         * ��ȡ�Ӽ�Ԫ��
         *
         * @param obj
         * @return
         * @author �ļ���
         * @time 2015��12��2�� ����3:42:36
         * @version 0.1
         * @since 0.1
         */
        public List<?> getChildren(Object obj);

        /**
         * Ĭ�ϵķ���ڵ��ȡ��
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
             * ��ȡ����
             * @author �ļ���
             * @time 2015��12��2�� ����4:17:00
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
     * ����д������
     *
     * @author �ļ���
     * @version 0.1
     * @time 2015��12��2�� ����4:12:43
     * @since 0.1
     */
    private interface NodeObjectWriter {
        /**
         * ���ø���Ԫ��
         *
         * @param obj
         * @param parent
         * @return
         * @author �ļ���
         * @time 2015��12��2�� ����3:42:07
         * @version 0.1
         * @since 0.1
         */
        public void setParent(Object obj, Object parent);

        /**
         * �����Ӽ�Ԫ��
         *
         * @param obj
         * @param children
         * @return
         * @author �ļ���
         * @time 2015��12��2�� ����3:42:36
         * @version 0.1
         * @since 0.1
         */
        public void setChildren(Object obj, List<?> children);

        /**
         * Ĭ�ϵķ���ڵ�д������
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
             * ��ȡ����
             * @author �ļ���
             * @time 2015��12��2�� ����4:17:00
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
     * ����Ĭ�ϵĶ�ȡ��
     *
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:11:48
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
     * ����Ĭ�ϵĽڵ�д������
     *
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����4:28:15
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
     * ����Field
     *
     * @param clazz
     * @param fieldName
     * @return
     * @author �ļ���
     * @time 2015��12��2�� ����5:32:40
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
