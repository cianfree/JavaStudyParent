package Database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static Database.JdbcHelper.UnderlineAndHumpNameConverter.unwrapperDBAttr;
import static Database.JdbcHelper.UnderlineAndHumpNameConverter.wrapperDBAttr;

/**
 * 接口定义
 *
 * @author Arvin
 * @time 2016/11/17 18:40
 */
public class JdbcHelper {


    /**
     * 数据库表名称或字段转换器
     *
     * @author Arvin
     * @time 2016/11/17 19:10
     */
    public static interface NameConverter {

        /**
         * 把Java中的名称转成数据库中的名称
         *
         * @param javaName java中的名称
         * @return 返回数据库对应的名称
         */
        String convertToDBName(String javaName);

        /**
         * 把数据库中的名称转成java中的名称
         *
         * @param dbName 数据库中的名称
         * @return 返回Java对应的名称
         */
        String convertToJavaName(String dbName);
    }

    /**
     * 模型构造器
     *
     * @author Arvin
     * @time 2016/11/18 10:50
     */
    public static interface ModelBuilder<T> {

        /**
         * 根据结果集构造结果对象
         *
         * @param resultSet 结果集
         */
        T build(ResultSet resultSet);

    }

    /**
     * 驼峰和下划线名称构造器, 数据库使用下划线，Java使用驼峰
     *
     * @author Arvin
     * @time 2016/11/17 19:13
     */
    public static class UnderlineAndHumpNameConverter implements NameConverter {

        private static class Holder {
            private static UnderlineAndHumpNameConverter INSTANCE = new UnderlineAndHumpNameConverter();
        }

        public static UnderlineAndHumpNameConverter getInstance() {
            return Holder.INSTANCE;
        }

        private UnderlineAndHumpNameConverter() {
        }

        @Override
        public String convertToDBName(String javaName) {
            return toUnderlineString(javaName);
        }

        /**
         * 转换成用下划线分割的字符串
         */
        public static String toUnderlineString(String string) {
            return wrapperDBAttr(firstLetterToLowerCase(string).replaceAll("([A-Z]+)", "_$1").toLowerCase());
        }

        /**
         * 包装数据库属性，前后加上 `
         *
         * @param dbAttr 数据库属性
         */
        public static String wrapperDBAttr(String dbAttr) {
            String result = dbAttr.matches("^ *`.*` *$") ? dbAttr.trim() : ("`" + dbAttr.trim().replaceFirst("^ *`", "").replaceFirst("` *$", "") + "`").replaceAll("\\.", "`.`");
            return result.replaceAll("(^ *`) *(.*) *(` *$)", "$1$2$3");
        }

        /**
         * 取消包装
         *
         * @param dbAttr
         * @return
         */
        public static String unwrapperDBAttr(String dbAttr) {
            return dbAttr.replaceFirst(" *^`", "").replaceFirst(" *` *$", "");
        }

        /**
         * 首字母小写
         */
        public static String firstLetterToLowerCase(String value) {
            return null != value && value.length() > 0 ? String.valueOf(value.charAt(0)).toLowerCase() + value.substring(1) : null;
        }

        @Override
        public String convertToJavaName(String dbFieldName) {
            if (null == dbFieldName || dbFieldName.trim().equals("") || !dbFieldName.contains("_")) {
                return dbFieldName;
            }
            String[] array = dbFieldName.split("_");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                String sub = array[i];
                if (i > 0) {
                    sub = String.valueOf(sub.charAt(0)).toUpperCase() + sub.substring(1);
                }
                builder.append(sub);
            }
            return builder.toString();
        }
    }

    /**
     * Jdbc Helper Exception
     *
     * @author Arvin
     * @time 2016/11/17 18:43
     */
    public static class JdbcHelperException extends RuntimeException {

        public JdbcHelperException() {
        }

        public JdbcHelperException(String message) {
            super(message);
        }

        public JdbcHelperException(String message, Throwable cause) {
            super(message, cause);
        }

        public JdbcHelperException(Throwable cause) {
            super(cause);
        }

        public JdbcHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    /**
     * @author Arvin
     * @time 2016/11/18 10:08
     */
    public static class PrimaryNotFoundException extends JdbcHelperException {

        public PrimaryNotFoundException() {
            super("Primary key not found!");
        }
    }

    /**
     * 不合法的内置类型
     *
     * @author Arvin
     * @time 2016/11/18 11:02
     */
    public static class InvalidPrimitiveTypeException extends JdbcHelperException {

        public InvalidPrimitiveTypeException() {
            super("Invalid primitive type, valid type is [Integer，int，Long，long，Double，double，Short，short，Date，String]!");
        }
    }

    /**
     * 全局配置
     *
     * @author Arvin
     * @time 2016/11/18 12:35
     */
    public static class GlobalConfig {

        private static NameConverter defaultTableNameConverter = UnderlineAndHumpNameConverter.getInstance();

        private static NameConverter defaultFieldNameConverter = UnderlineAndHumpNameConverter.getInstance();

        /** 数据库表要忽略的java属性 */
        private static Map<String, Set<String>> tableIgnoredJavaFields = new ConcurrentHashMap<>();

        /** 是否自动转换空值 */
        private static boolean autoConvertNullField = false;

        /** 数据库表 -> java字段对应数据库字段映射 */
        private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> tableJavaToDBFieldMap = new ConcurrentHashMap<>();

        /** 数据库表 -> 数据库字段对应java字段映射 */
        private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> tableDBToJavaFieldMap = new ConcurrentHashMap<>();

        /** 类型 对应的默认值 映射 */
        private static ConcurrentHashMap<Class<?>, Object> typeDefaultValueMap = new ConcurrentHashMap<>();

        /** 数据库表 -> 对应的主键java字段 */
        private static ConcurrentHashMap<String, Set<String>> tablePrimaryKeysMap = new ConcurrentHashMap<>();

        /** java数据类型到数据库表名的映射 */
        private static ConcurrentHashMap<Class<?>, String> typeToTableMap = new ConcurrentHashMap<>();

        /** 数据库表名称到java数据类型的映射 */
        private static ConcurrentHashMap<String, Class<?>> tableToTypeMap = new ConcurrentHashMap<>();

        public static NameConverter getDefaultTableNameConverter() {
            return defaultTableNameConverter;
        }

        public static NameConverter getDefaultFieldNameConverter() {
            return defaultFieldNameConverter;
        }

        public static boolean isAutoConvertNullField() {
            return autoConvertNullField;
        }

        public synchronized static void setAutoConvertNullField(boolean autoConvertNullField) {
            GlobalConfig.autoConvertNullField = autoConvertNullField;
        }

        public synchronized static void setDefaultTableNameConverter(NameConverter defaultFieldNameConverter) {
            GlobalConfig.defaultTableNameConverter = defaultFieldNameConverter;
        }

        public synchronized static void setDefaultFieldNameConverter(NameConverter defaultFieldNameConverter) {
            GlobalConfig.defaultFieldNameConverter = defaultFieldNameConverter;
        }

        public synchronized static void ignoredJavaFields(String table, String... ignoredJavaFields) {
            Set<String> tableIgnoredFields = getTableIgnoredJavaFields(table);
            if (ignoredJavaFields != null && ignoredJavaFields.length > 0) {
                for (String ignoredJavaField : ignoredJavaFields) {
                    if (isNotBlank(ignoredJavaField)) {
                        tableIgnoredFields.add(ignoredJavaField);
                    }
                }
            }
        }

        public synchronized static void ignoredJavaFields(String table, Collection<String> ignoredJavaFields) {
            Set<String> tableIgnoredFields = getTableIgnoredJavaFields(table);
            if (ignoredJavaFields != null && ignoredJavaFields.size() > 0) {
                for (String ignoredJavaField : ignoredJavaFields) {
                    if (isNotBlank(ignoredJavaField)) {
                        tableIgnoredFields.add(ignoredJavaField);
                    }
                }
            }
        }

        private synchronized static Set<String> getTableIgnoredJavaFields(String table) {
            Set<String> tableIgnoredFields = GlobalConfig.tableIgnoredJavaFields.get(table);
            if (tableIgnoredFields == null) {
                tableIgnoredFields = new HashSet<>();
                GlobalConfig.tableIgnoredJavaFields.put(table, tableIgnoredFields);
            }
            return tableIgnoredFields;
        }

        private static boolean isNotBlank(String value) {
            return null != value && !value.equals("");
        }

        public static Set<String> getIgnoredJavaFields(String table) {
            return GlobalConfig.tableIgnoredJavaFields.get(table);
        }

        public synchronized static void setFieldMap(String table, String javaFieldName, String dbFieldName) {
            ConcurrentHashMap<String, String> java2DbMap = getJava2DBFieldMap(table);
            ConcurrentHashMap<String, String> db2JavaMap = getDB2JavaFieldMap(table);
            if (isNotBlank(javaFieldName) && isNotBlank(dbFieldName)) {
                java2DbMap.put(javaFieldName, dbFieldName);
                db2JavaMap.put(dbFieldName, javaFieldName);
            }
        }

        private synchronized static ConcurrentHashMap<String, String> getJava2DBFieldMap(String table) {
            ConcurrentHashMap<String, String> java2DbMap = GlobalConfig.tableJavaToDBFieldMap.get(table);
            if (java2DbMap == null) {
                java2DbMap = new ConcurrentHashMap<>();
                GlobalConfig.tableJavaToDBFieldMap.put(table, java2DbMap);
            }
            return java2DbMap;
        }

        /**
         * key - java Field name
         * value - db field name
         */
        public synchronized static void setFieldMap(String table, Map<String, String> javaToDbFieldMap) {
            ConcurrentHashMap<String, String> java2DbMap = getJava2DBFieldMap(table);
            ConcurrentHashMap<String, String> db2JavaMap = getDB2JavaFieldMap(table);
            if (javaToDbFieldMap != null && !javaToDbFieldMap.isEmpty()) {
                for (Map.Entry<String, String> entry : javaToDbFieldMap.entrySet()) {
                    if (isNotBlank(entry.getKey()) && isNotBlank(entry.getValue())) {
                        java2DbMap.put(entry.getKey(), entry.getValue());
                        db2JavaMap.put(entry.getValue(), entry.getKey());
                    }
                }
            }
        }

        private synchronized static ConcurrentHashMap<String, String> getDB2JavaFieldMap(String table) {
            ConcurrentHashMap<String, String> db2JavaMap = GlobalConfig.tableDBToJavaFieldMap.get(table);
            if (db2JavaMap == null) {
                db2JavaMap = new ConcurrentHashMap<>();
                GlobalConfig.tableDBToJavaFieldMap.put(table, db2JavaMap);
            }
            return db2JavaMap;
        }

        public static String getDBField(String table, String javaFieldName) {
            Map<String, String> map = GlobalConfig.tableJavaToDBFieldMap.get(table);
            return map == null ? null : map.get(javaFieldName);
        }

        public static String getJavaField(String table, String dbFieldName) {
            Map<String, String> map = GlobalConfig.tableDBToJavaFieldMap.get(table);
            return map == null ? null : map.get(dbFieldName);
        }

        public synchronized static void setTypeDefaultValueMap(Class<?> type, Object defaultValue) {
            if (null != type) {
                GlobalConfig.typeDefaultValueMap.put(type, defaultValue);
            }
        }

        public synchronized static void setTypeDefaultValueMap(Map<Class<?>, Object> typeDefaultValueMap) {
            if (null != typeDefaultValueMap && !typeDefaultValueMap.isEmpty()) {
                GlobalConfig.typeDefaultValueMap.putAll(typeDefaultValueMap);
            }
        }

        public static Object getTypeDefaultValue(Class<?> type) {
            if (null == type) {
                return null;
            }
            return GlobalConfig.typeDefaultValueMap.get(type);
        }

        public synchronized static void setTablePrimaryKeysMap(String table, String... javaPrimaryFields) {
            if (null != javaPrimaryFields && javaPrimaryFields.length > 0) {
                Set<String> primaryKeys = new HashSet<>();
                for (String primaryKeyField : javaPrimaryFields) {
                    if (isNotBlank(primaryKeyField)) {
                        primaryKeys.add(primaryKeyField);
                    }
                }
                GlobalConfig.tablePrimaryKeysMap.put(table, primaryKeys);
            }
        }

        public synchronized static void addTablePrimaryKeysMap(String table, String... javaPrimaryFields) {
            if (null != javaPrimaryFields && javaPrimaryFields.length > 0) {
                Set<String> primaryKeys = GlobalConfig.tablePrimaryKeysMap.get(table);
                if (null == primaryKeys) {
                    primaryKeys = new HashSet<>();
                    GlobalConfig.tablePrimaryKeysMap.put(table, primaryKeys);
                }
                for (String primaryKeyField : javaPrimaryFields) {
                    if (isNotBlank(primaryKeyField)) {
                        primaryKeys.add(primaryKeyField);
                    }
                }
            }
        }

        public synchronized static void setTablePrimaryKeysMap(String table, Set<String> javaPrimaryFields) {
            if (null != javaPrimaryFields && javaPrimaryFields.size() > 0) {
                Set<String> primaryKeys = new HashSet<>();
                for (String primaryKeyField : javaPrimaryFields) {
                    if (isNotBlank(primaryKeyField)) {
                        primaryKeys.add(primaryKeyField);
                    }
                }
                GlobalConfig.tablePrimaryKeysMap.put(table, primaryKeys);
            }
        }

        public synchronized static void addTablePrimaryKeysMap(String table, Set<String> javaPrimaryFields) {
            if (null != javaPrimaryFields && javaPrimaryFields.size() > 0) {
                Set<String> primaryKeys = GlobalConfig.tablePrimaryKeysMap.get(table);
                if (null == primaryKeys) {
                    primaryKeys = new HashSet<>();
                    GlobalConfig.tablePrimaryKeysMap.put(table, primaryKeys);
                }
                for (String primaryKeyField : javaPrimaryFields) {
                    if (isNotBlank(primaryKeyField)) {
                        primaryKeys.add(primaryKeyField);
                    }
                }
            }
        }

        public static Set<String> getTablePrimaryKeys(String table) {
            return GlobalConfig.tablePrimaryKeysMap.get(table);
        }

        public synchronized static void setJavaAndTableMap(Class<?> type, String table) {
            if (null != type && isNotBlank(table)) {
                tableToTypeMap.put(table, type);
                typeToTableMap.put(type, table);
            }
        }

        public static String getTable(Class<?> type) {
            return GlobalConfig.typeToTableMap.get(type);
        }

        public static Class<?> getType(String table) {
            return GlobalConfig.tableToTypeMap.get(table);
        }

    }

    /**
     * JdbcHelper线程参数
     *
     * @author Arvin
     * @time 2016/11/18 11:27
     */
    public static class ThreadConfig {

        /** 数据库表名转换器 */
        private static ThreadLocal<NameConverter> tableNameConverter = new ThreadLocal<>();

        /** 数据库字段名称转换器 */
        private static ThreadLocal<NameConverter> fieldNameConverter = new ThreadLocal<>();

        /** 要忽略的java属性字段集合 */
        private static ThreadLocal<Set<String>> ignoredJavaFields = new ThreadLocal<>();

        /** 要操作的数据库表名称 */
        private static ThreadLocal<String> operateTable = new ThreadLocal<>();

        /** 是否需要覆盖性插入，默认是 否 */
        private static ThreadLocal<Boolean> replaceInsert = new ThreadLocal<>();

        /** 是否需要忽略未知属性，默认是 是 */
        private static ThreadLocal<Boolean> ignoredUnknownField = new ThreadLocal<>();

        /** 是否自动转换 null 的属性 */
        private static ThreadLocal<Boolean> autoConvertNullField = new ThreadLocal<>();

        /** Java属性名 -> 数据库属性名 */
        private static ThreadLocal<Map<String, String>> javaToDBFieldMap = new ThreadLocal<>();

        /** 数据库属性名 -> Java属性名 */
        private static ThreadLocal<Map<String, String>> dbToJavaFieldMap = new ThreadLocal<>();

        /** 是否启用全局的数据库属性名与java属性名的map， 默认是 启用 */
        private static ThreadLocal<Boolean> enabledGlobalFieldMap = new ThreadLocal<>();

        /** 数据库java属性对应的默认值映射 */
        private static ThreadLocal<Map<String, Object>> javaFieldDefaultValueMap = new ThreadLocal<>();

        /** 是否启用全局数据库java属性对应的默认值映射， 默认是 启用 */
        private static ThreadLocal<Boolean> enabledGlobalJavaFieldDefaultValueMap = new ThreadLocal<>();

        /** 模型类型对应的主键属性列表 */
        private static ThreadLocal<Set<String>> primaryKeys = new ThreadLocal<>();

        /** 是否启用全局模型类型对应的主键属性列表， 默认是 启用 */
        private static ThreadLocal<Boolean> enabledGlobalTypePrimaryKeysMap = new ThreadLocal<>();

        /** 启用Log */
        private static ThreadLocal<Boolean> enabledLog = new ThreadLocal<>();

        /** 当前页面 */
        private static ThreadLocal<Integer> currentPage = new ThreadLocal<>();

        /** 要查询多少数据 */
        private static ThreadLocal<Integer> pageSize = new ThreadLocal<>();

        /** 当前线程数据库链接 */
        private static ThreadLocal<Connection> connection = new ThreadLocal<>();

        public static NameConverter getTableNameConverter() {
            return null == tableNameConverter.get() ? GlobalConfig.getDefaultTableNameConverter() : tableNameConverter.get();
        }

        public static void setTableNameConverter(NameConverter tableNameConverter) {
            ThreadConfig.tableNameConverter.set(tableNameConverter);
        }

        public static NameConverter getFieldNameConverter() {
            return null == fieldNameConverter.get() ? GlobalConfig.getDefaultTableNameConverter() : fieldNameConverter.get();
        }

        public static void setFieldNameConverter(NameConverter fieldNameConverter) {
            ThreadConfig.fieldNameConverter.set(fieldNameConverter);
        }

        public static Set<String> getIgnoredJavaFields() {
            return ignoredJavaFields.get();
        }

        public static void setIgnoredJavaFields(Collection<String> ignoredJavaFields) {
            if (null != ignoredJavaFields && ignoredJavaFields.size() > 0) {
                Set<String> ignoredFields = new HashSet<>();
                for (String ignoredField : ignoredJavaFields) {
                    if (isNotBlank(ignoredField)) {
                        ignoredFields.add(ignoredField);
                    }
                }
                ThreadConfig.ignoredJavaFields.set(ignoredFields);
            }
        }

        public static void setIgnoredJavaFields(String... ignoredJavaFields) {
            if (null != ignoredJavaFields && ignoredJavaFields.length > 0) {
                Set<String> ignoredFields = new HashSet<>();
                for (String ignoredField : ignoredJavaFields) {
                    if (isNotBlank(ignoredField)) {
                        ignoredFields.add(ignoredField);
                    }
                }
                ThreadConfig.ignoredJavaFields.set(ignoredFields);
            }
        }

        public static String getOperateTable() {
            return operateTable.get();
        }

        public static void setOperateTable(String operateTable) {
            ThreadConfig.operateTable.set(operateTable);
        }

        public static boolean isReplaceInsert() {
            return null == replaceInsert.get() ? false : replaceInsert.get();
        }

        public static void replaceInsert(boolean replaceInsert) {
            ThreadConfig.replaceInsert.set(replaceInsert);
        }

        public static boolean isIgnoredUnknownField() {
            return null == ignoredUnknownField.get() ? true : ignoredUnknownField.get();
        }

        public static void ignoredUnknownField(boolean ignoredUnknownField) {
            ThreadConfig.ignoredUnknownField.set(ignoredUnknownField);
        }

        public static boolean isAutoConvertNullField() {
            return null == autoConvertNullField.get() ? GlobalConfig.isAutoConvertNullField() : autoConvertNullField.get();
        }

        public static void autoConvertNullField(boolean autoConvertNullField) {
            ThreadConfig.autoConvertNullField.set(autoConvertNullField);
        }

        public static String getDBField(String javaFieldName) {
            Map<String, String> map = javaToDBFieldMap.get();
            return map == null ? null : map.get(javaFieldName);
        }

        public static String getJavaField(String dbFieldName) {
            Map<String, String> map = dbToJavaFieldMap.get();
            return map == null ? null : map.get(dbFieldName);
        }

        private static boolean isNotBlank(String value) {
            return null != value && !value.equals("");
        }

        public static void addFieldMap(String javaFieldName, String dbFieldName) {
            Map<String, String> java2DbMap = getJava2DbFieldMap();
            Map<String, String> db2JavaMap = getDB2JavaFieldMap();
            if (isNotBlank(javaFieldName) && isNotBlank(dbFieldName)) {
                java2DbMap.put(javaFieldName, dbFieldName);
                db2JavaMap.put(dbFieldName, javaFieldName);
            }
        }

        private static Map<String, String> getJava2DbFieldMap() {
            Map<String, String> java2DbMap = javaToDBFieldMap.get();
            if (java2DbMap == null) {
                java2DbMap = new HashMap<>();
                javaToDBFieldMap.set(java2DbMap);
            }
            return java2DbMap;
        }

        /**
         * key - java Field name
         * value - db field name
         */
        public static void addFieldMap(Map<String, String> javaToDbFieldMap) {
            Map<String, String> java2DbMap = getJava2DbFieldMap();
            Map<String, String> db2JavaMap = getDB2JavaFieldMap();
            if (javaToDbFieldMap != null && !javaToDbFieldMap.isEmpty()) {
                for (Map.Entry<String, String> entry : javaToDbFieldMap.entrySet()) {
                    if (isNotBlank(entry.getKey()) && isNotBlank(entry.getValue())) {
                        java2DbMap.put(entry.getKey(), entry.getValue());
                        db2JavaMap.put(entry.getValue(), entry.getKey());
                    }
                }
            }
        }

        private static Map<String, String> getDB2JavaFieldMap() {
            Map<String, String> db2JavaMap = javaToDBFieldMap.get();
            if (db2JavaMap == null) {
                db2JavaMap = new HashMap<>();
                dbToJavaFieldMap.set(db2JavaMap);
            }
            return db2JavaMap;
        }

        public static boolean enabledGlobalFieldMap() {
            return enabledGlobalFieldMap.get() == null ? true : enabledGlobalFieldMap.get();
        }

        public static void enabledGlobalFieldMap(boolean enabled) {
            ThreadConfig.enabledGlobalFieldMap.set(enabled);
        }

        public static Object getJavaFieldDefaultValue(String javaFieldName) {
            Map<String, Object> map = javaFieldDefaultValueMap.get();
            return null == map ? null : map.get(javaFieldName);
        }

        public static void setJavaFieldDefaultValueMap(Map<String, Object> javaFieldDefaultValueMap) {
            Map<String, Object> map = ThreadConfig.javaFieldDefaultValueMap.get();
            if (map == null) {
                if (javaFieldDefaultValueMap != null) {
                    map = javaFieldDefaultValueMap;
                    ThreadConfig.javaFieldDefaultValueMap.set(map);
                    return;
                }
                map = new HashMap<>();
                ThreadConfig.javaFieldDefaultValueMap.set(map);
            } else {
                if (null != javaFieldDefaultValueMap && !javaFieldDefaultValueMap.isEmpty()) {
                    map.putAll(javaFieldDefaultValueMap);
                }
            }
        }

        public static void setJavaFieldDefaultValueMap(String javaFieldName, Object defaultValue) {
            Map<String, Object> map = ThreadConfig.javaFieldDefaultValueMap.get();
            if (map == null) {
                map = new HashMap<>();
                ThreadConfig.javaFieldDefaultValueMap.set(map);
            }
            map.put(javaFieldName, defaultValue);
        }

        public static boolean enabledGlobalJavaFieldDefaultValueMap() {
            return enabledGlobalJavaFieldDefaultValueMap.get() == null ? true : enabledGlobalJavaFieldDefaultValueMap.get();
        }

        public static void enabledGlobalJavaFieldDefaultValueMap(boolean enabeld) {
            ThreadConfig.enabledGlobalJavaFieldDefaultValueMap.set(enabeld);
        }

        public static Set<String> getPrimaryKeys() {
            return ThreadConfig.primaryKeys.get();
        }

        public static void setPrimaryKeys(String[] primaryKeys) {
            Set<String> list = ThreadConfig.primaryKeys.get();
            if (null == list) {
                list = new HashSet<>();
                ThreadConfig.primaryKeys.set(list);
            } else {
                list.clear();
            }
            if (null != primaryKeys && primaryKeys.length > 0) {
                for (String primaryKey : primaryKeys) {
                    if (null != primaryKey && !"".equals(primaryKey)) {
                        list.add(primaryKey);
                    }
                }
            }
        }

        public static boolean enabledGlobalTypePrimaryKeysMap() {
            return enabledGlobalTypePrimaryKeysMap.get() == null ? true : enabledGlobalTypePrimaryKeysMap.get();
        }

        public static void enabledGlobalTypePrimaryKeysMap(boolean enabled) {
            ThreadConfig.enabledGlobalTypePrimaryKeysMap.set(enabled);
        }

        public static void enabledLog(boolean enabled) {
            ThreadConfig.enabledLog.set(enabled);
        }

        public static boolean enabledLog() {
            return ThreadConfig.enabledLog.get() == null ? true : ThreadConfig.enabledLog.get();
        }

        public static Integer getCurrentPage() {
            return currentPage.get();
        }

        public static void setCurrentPage(int currentPage) {
            ThreadConfig.currentPage.set(currentPage < 1 ? 1 : currentPage);
        }

        public static Integer getPageSize() {
            return pageSize.get();
        }

        public static void setPageSize(int pageSize) {
            ThreadConfig.pageSize.set(pageSize < 1 ? 1 : pageSize);
        }

        /**
         * 判断是否需要分页
         */
        public static boolean isPaging() {
            Integer currentPage = ThreadConfig.currentPage.get();
            if (currentPage == null || currentPage < 1) {
                return false;
            }
            Integer pageSize = ThreadConfig.pageSize.get();
            if (pageSize == null || pageSize < 1) {
                return false;
            }
            return true;
        }

        public static void setConnection(Connection currentConnection) {
            ThreadConfig.connection.set(currentConnection);
        }

        public static Connection getConnection() {
            return ThreadConfig.connection.get();
        }
    }

    /**
     * 对应数据库属性名称
     *
     * @author Arvin
     * @time 2016/11/18 9:55
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface DBField {

        /**
         * 对应的数据库属性名称
         */
        String value() default "";
    }

    /**
     * 表示要忽略的忽略属性
     *
     * @author Arvin
     * @time 2016/11/18 9:55
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface IgnoredField {
    }

    /**
     * 表示要插入的时候要忽略的属性， 有可能使用了自增主键，因此这里可能不需要插入自增的主键
     *
     * @author Arvin
     * @time 2016/11/18 9:55
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface InsertIgnoredField {
    }

    /**
     * 标识是否是主键, 在属性上使用
     *
     * @author Arvin
     * @time 2016/11/18 9:55
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface PrimaryKey {
    }


    /**
     * 数据库表名称
     *
     * @author Arvin
     * @time 2016/11/18 9:57
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Table {

        /**
         * 数据库表名称
         */
        String value() default "";
    }

    /**
     * 表示要更新的时候要忽略的属性，有些字段我们可能不需要更新
     *
     * @author Arvin
     * @time 2016/11/18 9:55
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface UpdateIgnoredField {
    }

    /**
     * MySQL驱动
     */
    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    /**
     * Oracle驱动
     */
    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    /**
     * MySQL JDBC_URL
     */
    public static final String JDBC_URL_MYSQL = "jdbc:mysql://{host}:{port}/{schema}?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE";
    /**
     * Oracle JDBC_URL
     */
    public static final String JDBC_URL_ORACLE = "jdbc:oracle:thin:@{host}:{port}:{schema}";

    private final String driver;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public static class Builder {
        private String driver = DRIVER_MYSQL;
        private String username = "root";
        private String password = "admin";
        private String schema;
        private String jdbcUrl = JDBC_URL_MYSQL;
        private int port = 3306;
        private String host = "localhost";

        public Builder(String schema) {
            this.schema = schema;
        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        private Builder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public JdbcHelper build() {
            String url = jdbcUrl.replace("{host}", host).replace("{port}", "" + port).replace("{schema}", schema);
            return new JdbcHelper(driver, url, username, password);
        }
    }

    /**
     * 反射工具类
     *
     * @author Arvin
     * @time 2016/10/25 19:27
     */
    public static class ReflectUtils {

        /**
         * 查找指定类型的属性列表
         *
         * @param clazz     类名
         * @param fieldType 字段类型， 如果为空就返回所有的字段
         * @return 返回指定类型的属性列表，如果为空就返回所有的字段
         */
        public static List<Field> getNoneStaticDeclaredFields(Class<?> clazz, Class<?> fieldType) {
            Field[] declaredFields = clazz.getDeclaredFields();
            if (null != declaredFields && declaredFields.length > 0) {
                List<Field> fields = new ArrayList<>();
                for (Field field : declaredFields) {
                    if (Modifier.isStatic(field.getModifiers())) { // 过滤静态属性
                        continue;
                    }
                    if (null == fieldType) {
                        fields.add(field);
                    } else if (field.getType().equals(fieldType)) {
                        fields.add(field);
                    }
                }
                return fields;
            }
            return new ArrayList<>();
        }

        /**
         * 获取字段的值
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
         * 搜索字段，包含私有的，从当前类开始搜索，如果当前类没有，继续往父类中查找，直到找到或到Object为止
         */
        public static Field findDeclaredField(Class<?> clazz, String fieldName) {
            for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
                try {
                    return superClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {
                }
            }
            return null;
        }
    }

    /**
     * @author Arvin
     * @time 2016/10/25 18:49
     */
    public interface SQLBuilder {

        /**
         * 获取SQL语句
         */
        String getSql();

        /**
         * 获取参数
         */
        Object[] getParams();

        /**
         * 获取参数
         */
        List<Object> getListParams();

    }

    public abstract static class AbstractSqlBuilder<S> implements SQLBuilder {
        /** 参数列表 */
        private List<Object> params = new ArrayList<>();

        protected String sql;

        /**
         * 获取自己
         */
        protected abstract S getSelf();

        @Override
        public String getSql() {
            if (null == sql) {
                sql = buildSql();
            }
            return sql;
        }

        protected abstract String buildSql();

        @Override
        public Object[] getParams() {
            return params.toArray();
        }

        @Override
        public List<Object> getListParams() {
            return params;
        }

        /**
         * 添加参数
         *
         * @param values 参数值
         */
        protected S addParams(Object... values) {
            if (null != values) {
                for (Object value : values) {
                    params.add(value);
                }
            }
            return getSelf();
        }

        /**
         * 添加参数
         *
         * @param params 参数列表
         */
        protected S addListParams(List<Object> params) {
            if (null != params && !params.isEmpty()) {
                this.params.addAll(params);
            }
            return getSelf();
        }
    }

    public static class InsertBuilder extends AbstractSqlBuilder<InsertBuilder> {

        private Object model;

        public InsertBuilder(Object model) {
            this.model = model;
        }

        @Override
        protected InsertBuilder getSelf() {
            return this;
        }

        @Override
        protected String buildSql() {
            String table = getTablename(model.getClass());
            boolean replaceInto = isReplaceInto();
            StringBuilder builder;
            if (replaceInto) {
                builder = new StringBuilder("REPLACE INTO ");
            } else {
                builder = new StringBuilder("INSERT INTO ");
            }
            builder.append(table);
            StringBuilder fieldsBuilder = new StringBuilder("(");
            StringBuilder valuesBuilder = new StringBuilder("VALUES(");

            List<Field> fields = ReflectUtils.getNoneStaticDeclaredFields(model.getClass(), null);
            for (Field field : fields) {
                if (!isIgnoreJavaField(field, table, field.getName())) {
                    String dbFieldName = getDBFieldName(field, table, field.getName());
                    fieldsBuilder.append(dbFieldName).append(",");
                    Object value = ReflectUtils.getFieldValue(this.model, field);
                    if (null == value) {
                        value = getJavaFieldDefaultValue(field, field.getName(), null);
                    }
                    valuesBuilder.append("?,");
                    addParams(value);
                }
            }
            return builder.append(fieldsBuilder.toString().replaceFirst(",$", ")")).append(" ").append(valuesBuilder.toString().replaceFirst(",$", ")")).toString();
        }
    }

    public static class MapInsertBuilder extends AbstractSqlBuilder<MapInsertBuilder> {

        private Map<String, Object> dataMap;

        /** 要操作的数据库表 */
        private String table;

        public MapInsertBuilder(String table, Map<String, Object> dataMap) {
            this.dataMap = dataMap;
            this.table = table;
        }

        @Override
        protected MapInsertBuilder getSelf() {
            return this;
        }

        @Override
        protected String buildSql() {
            boolean replaceInto = isReplaceInto();
            StringBuilder builder;
            if (replaceInto) {
                builder = new StringBuilder("REPLACE INTO ");
            } else {
                builder = new StringBuilder("INSERT INTO ");
            }
            builder.append(table);
            StringBuilder fieldsBuilder = new StringBuilder("(");
            StringBuilder valuesBuilder = new StringBuilder("VALUES(");

            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                String dbFieldName = getDBFieldName(null, this.table, entry.getKey());
                Object value = entry.getValue();
                if (null == value) {
                    value = getJavaFieldDefaultValue(null, entry.getKey(), null);
                }
                fieldsBuilder.append(dbFieldName).append(",");
                valuesBuilder.append("?,");
                addParams(value);
            }
            return builder.append(fieldsBuilder.toString().replaceFirst(",$", ")")).append(" ").append(valuesBuilder.toString().replaceFirst(",$", ")")).toString();
        }
    }

    private JdbcHelper(String driver, String jdbcUrl, String username, String password) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;

        initContext();
    }

    public static Builder mysqlBuilder(String schema) {
        return new Builder(schema);
    }

    public static Builder oracleBuilder(String instance) {
        return new Builder(null == instance || "".equals(instance.trim()) ? "XE" : instance)
                .driver(DRIVER_ORACLE)
                .jdbcUrl(JDBC_URL_ORACLE)
                .port(1521)
                .username("SCOTT")
                .password("TIGER")
                ;
    }

    private Connection openConnection() {
        try {
            Connection connection = ThreadConfig.getConnection();
            if (null == connection) {
                connection = DriverManager.getConnection(jdbcUrl, username, password);
                connection.setAutoCommit(true);
                ThreadConfig.setConnection(connection);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("不能获取数据库链接");
        }
    }

    private JdbcHelper close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public JdbcHelper close(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private JdbcHelper close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private JdbcHelper close(Connection conn, Statement statement, ResultSet resultSet) {
        close(conn);
        close(statement);
        close(resultSet);
        return this;
    }

    private void initContext() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化异常：" + this.toString());
        }
    }

    public static boolean isNotBlank(String value) {
        return null != value && !value.equals("");
    }

    /**
     * 配置当前线程数据库表名转换器
     *
     * @param tableNameConverter 数据库表名转换器
     */
    public JdbcHelper tableNameConverter(NameConverter tableNameConverter) {
        ThreadConfig.setTableNameConverter(tableNameConverter);
        return this;
    }

    /**
     * 获取指定的数据库表名称， 先从全局中获取，如果没有就从标签获取，如果没有就直接进行转换，然后方法哦GloablConfig中
     *
     * @param modelType 数据库表名称
     */
    private static String getTablename(Class<?> modelType) {
        // 先从当前线程中获取，如果当前线程中设置了表名称，就使用当前线程的
        String table = ThreadConfig.getOperateTable();
        if (isNotBlank(table)) {
            return wrapperDBAttr(table);
        }
        if (modelType == null) {
            throw new RuntimeException("Can not parse table name!");
        }
        table = GlobalConfig.getTable(modelType);
        if (isNotBlank(table)) {
            return table;
        }
        // 解析annotation
        Table tableAnnotation = modelType.getAnnotation(Table.class);
        if (null != tableAnnotation) {
            table = tableAnnotation.value();
            if (isNotBlank(table)) {
                GlobalConfig.setJavaAndTableMap(modelType, table);
                return table;
            }
        }
        // 根据名称计算
        NameConverter tableNameConverter = ThreadConfig.getTableNameConverter();
        tableNameConverter = tableNameConverter == null ? UnderlineAndHumpNameConverter.getInstance() : tableNameConverter;
        table = tableNameConverter.convertToDBName(modelType.getSimpleName());
        GlobalConfig.setJavaAndTableMap(modelType, table);
        return table;
    }

    /**
     * 配置当前线程数据库字段名称转换器
     *
     * @param fieldNameConverter 数据库字段名称转换器
     */
    public JdbcHelper fieldNameConverter(NameConverter fieldNameConverter) {
        ThreadConfig.setFieldNameConverter(fieldNameConverter);
        return this;
    }

    private static String getDBFieldName(Field field, String table, String javaFieldName) {
        if (null != field) {
            javaFieldName = field.getName();
        }
        if (!isNotBlank(javaFieldName)) {
            throw new RuntimeException("Java field name should not be null!");
        }
        String dbField = ThreadConfig.getDBField(javaFieldName);
        if (isNotBlank(dbField)) {
            return dbField;
        }
        // 去全局配置中找
        if (isNotBlank(table) && ThreadConfig.enabledGlobalFieldMap()) {
            dbField = GlobalConfig.getDBField(table, javaFieldName);
            if (isNotBlank(dbField)) {
                return dbField;
            }
        }
        if (null != field) {
            // 还是找不到，检测Annotation
            DBField dbFieldAnnotation = field.getAnnotation(DBField.class);
            if (null != dbFieldAnnotation) {
                dbField = dbFieldAnnotation.value();
                if (isNotBlank(dbField)) {
                    return dbField;
                }
            }
        }
        // 还是找不到，通过计算
        NameConverter fieldNameConverter = ThreadConfig.getFieldNameConverter();
        fieldNameConverter = null == fieldNameConverter ? UnderlineAndHumpNameConverter.getInstance() : fieldNameConverter;
        return fieldNameConverter.convertToDBName(javaFieldName);
    }

    /**
     * 将数据库字段转成Java模型字段
     */
    private static String getModelFieldName(String table, String dbColumnName) {
        if (null == dbColumnName || dbColumnName.trim().equals("")) {
            throw new RuntimeException("DB field name should not be null!");
        }
        // 先从当前线程配置中查询
        String javaFieldName = ThreadConfig.getJavaField(dbColumnName);
        if (isNotBlank(javaFieldName)) {
            return javaFieldName;
        }
        // 从全局配置中获取
        if (isNotBlank(table)) {
            javaFieldName = GlobalConfig.getJavaField(table, javaFieldName);
            if (isNotBlank(javaFieldName)) {
                return javaFieldName;
            }
        }
        // 还是找不到，通过计算
        NameConverter fieldNameConverter = ThreadConfig.getFieldNameConverter();
        fieldNameConverter = null == fieldNameConverter ? UnderlineAndHumpNameConverter.getInstance() : fieldNameConverter;
        return fieldNameConverter.convertToJavaName(dbColumnName);
    }

    /**
     * 配置当前线程需要忽略的java字段名称
     *
     * @param ignoredFields 要忽略的java字段属性名称
     */
    public JdbcHelper ignoredFields(String... ignoredFields) {
        ThreadConfig.setIgnoredJavaFields(ignoredFields);
        return this;
    }

    /**
     * 配置当前线程需要忽略的java字段名称
     *
     * @param ignoredFields 要忽略的java字段属性名称集合
     */
    public JdbcHelper ignoredFields(Collection<String> ignoredFields) {
        ThreadConfig.setIgnoredJavaFields(ignoredFields);
        return this;
    }

    /**
     * 当前线程是否需要忽略该属性
     *
     * @param javaFieldName
     * @return
     */
    private static boolean isIgnoreJavaField(Field field, String table, String javaFieldName) {
        Set<String> ignoredFields = ThreadConfig.getIgnoredJavaFields();
        if (null != ignoredFields && ignoredFields.contains(javaFieldName)) {
            return true;
        }
        if (isNotBlank(table)) {
            Set<String> globalIgnoredFields = GlobalConfig.getIgnoredJavaFields(table);
            if (null != globalIgnoredFields && globalIgnoredFields.contains(javaFieldName)) {
                return true;
            }
        }
        if (field != null) {
            if (null != field.getAnnotation(IgnoredField.class) || null != field.getAnnotation(InsertIgnoredField.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 配置当前线程需要操作的表名称
     *
     * @param table 数据库表的名称
     */
    public JdbcHelper table(String table) {
        ThreadConfig.setOperateTable(table);
        return this;
    }

    /**
     * 是否需要覆盖性插入
     *
     * @param replaceInsert 是否需要覆盖性插入
     */
    public JdbcHelper replaceInsert(boolean replaceInsert) {
        ThreadConfig.replaceInsert(replaceInsert);
        return this;
    }

    public static boolean isReplaceInto() {
        return ThreadConfig.isReplaceInsert();
    }

    /**
     * 是否自动转换空值， 在插入或更新的时候有效
     *
     * @param autoConvertNullField 是否自动转换
     */
    public JdbcHelper autoConvertNullField(boolean autoConvertNullField) {
        ThreadConfig.autoConvertNullField(autoConvertNullField);
        return this;
    }

    /**
     * 当前线程是否启动全局属性字段映射， 默认是开启的
     *
     * @param enabled 是否开启
     */
    public JdbcHelper enabledGlobalFieldMap(boolean enabled) {
        ThreadConfig.enabledGlobalFieldMap(enabled);
        return this;
    }

    /**
     * 定义仅当前线程有效的数据库表，java属性名称和数据库属性的映射
     *
     * @param javaAndDBFieldMap java属性-数据库属性映射
     */
    public JdbcHelper fieldMap(Map<String, String> javaAndDBFieldMap) {
        ThreadConfig.addFieldMap(javaAndDBFieldMap);
        return this;
    }

    /**
     * Java - 数据库属性映射
     *
     * @param javaFieldName java属性名称
     * @param dbFieldName   数据库属性名称
     */
    public JdbcHelper fieldMap(String javaFieldName, String dbFieldName) {
        ThreadConfig.addFieldMap(javaFieldName, dbFieldName);
        return this;
    }

    /**
     * 当前线程是否开启全局java字段默认值的map
     *
     * @param enabled 是否开启
     */
    public JdbcHelper enabledGlobalJavaFieldDefaultValueMap(boolean enabled) {
        ThreadConfig.enabledGlobalJavaFieldDefaultValueMap(enabled);
        return this;
    }

    /**
     * 配置当前线程的java字段默认值的Map
     *
     * @param javaFieldDefaultValueMap key为java属性名称
     */
    public JdbcHelper javaFieldDefaultValueMap(Map<String, Object> javaFieldDefaultValueMap) {
        ThreadConfig.setJavaFieldDefaultValueMap(javaFieldDefaultValueMap);
        return this;
    }

    /**
     * 配置当前线程的java字段默认值
     *
     * @param javaFieldName java属性名称
     * @param defaultValue  默认值
     */
    public JdbcHelper javaFieldDefaultValue(String javaFieldName, Object defaultValue) {
        ThreadConfig.setJavaFieldDefaultValueMap(javaFieldName, defaultValue);
        return this;
    }

    private static Object getJavaFieldDefaultValue(Field field, String javaFieldName, Object defaultValue) {
        if (null != field) {
            javaFieldName = field.getName();
        }
        Object value = ThreadConfig.getJavaFieldDefaultValue(javaFieldName);
        if (null != value) {
            return value;
        }
        // 从全局配置中获取
        if (null != field && ThreadConfig.enabledGlobalJavaFieldDefaultValueMap()) {
            return GlobalConfig.getTypeDefaultValue(field.getType());
        }
        return defaultValue;
    }

    /**
     * 如果调用了该该方法，就不会使用全局的表主键配置了
     *
     * @param primaryKeyFields 主键
     */
    public JdbcHelper primaryKeys(String... primaryKeyFields) {
        ThreadConfig.setPrimaryKeys(primaryKeyFields);
        return this;
    }

    public JdbcHelper enabledLog(boolean enabledLog) {
        ThreadConfig.enabledLog(enabledLog);
        return this;
    }

    public boolean enabledLog() {
        return ThreadConfig.enabledLog();
    }

    private void showLog(String sql, Class<?> resultType, Object... params) {
        showLog(sql, null == resultType ? "" : resultType.getName(), params);
    }

    private void showLog(String sql, String resultType, Object... params) {
        if (!enabledLog()) {
            return;
        }
        int index = sql.indexOf(" ");
        String sqlType = sql.substring(0, index).toUpperCase();
        System.out.println(">>> -------------------------------------------------------------------------------------------------");
        System.out.println(">>> Execute Info:\t" + sqlType);
        System.out.println(">>> ResultType: \t" + (null == resultType || "".equals(resultType) ? "Not Required!" : resultType));
        System.out.println(">>> TableNameConverter: " + ThreadConfig.getTableNameConverter().getClass());
        System.out.println(">>> FieldNameConverter: " + ThreadConfig.getFieldNameConverter().getClass());
        System.out.println(">>> Paging: " + ThreadConfig.isPaging());
        System.out.println(">>> AutoConvertNullField: " + ThreadConfig.isAutoConvertNullField());
        System.out.println(">>> SQL:\t\t\t" + sql);
        if (null != params && params.length > 0) {
            System.out.print(">>> Parameters: \t");
            StringBuilder builder = new StringBuilder();
            for (Object obj : params) {
                builder.append(null != obj ? obj.toString() + "" : "'null'").append(", ");
            }
            System.out.println(builder.toString().replaceFirst(", $", ""));
        } else {
            System.out.println(">>> Not Parameters!");
        }
        System.out.println(">>> -------------------------------------------------------------------------------------------------");
    }

    /**
     * 设置当前线程，进行分页
     *
     * @param currentPage 查询的当前页
     * @param pageSize    查询数量
     */
    public JdbcHelper paging(int currentPage, int pageSize) {
        ThreadConfig.setCurrentPage(currentPage);
        ThreadConfig.setPageSize(pageSize);
        return this;
    }

    /**
     * 插入一个模型对象，表的名称由客户端提供， 默认使用下划线来计算数据库字段,如果先自己定义，执行 table 方法
     *
     * @param model 模型对象
     * @return 返回数据库影响行数
     */
    <T> int insertModel(T model) {
        InsertBuilder builder = new InsertBuilder(model);
        return executeUpdate(builder.getSql(), builder.getParams());
    }

    /**
     * 插入Map
     *
     * @param table   数据库表名称
     * @param dataMap 数据Map
     */
    int insertMap(String table, Map<String, Object> dataMap) {
        MapInsertBuilder builder = new MapInsertBuilder(table, dataMap);
        return executeUpdate(builder.getSql(), builder.getParams());
    }

    /**
     * @author Arvin
     * @time 2016/11/15 14:20
     */
    public class MapBatchInsertBuilder {

        private List<Map<String, Object>> dataListMap;

        private String insertSql;

        private String table;

        private List<Object[]> paramsList = new ArrayList<>();

        public MapBatchInsertBuilder(String table, List<Map<String, Object>> dataListMap) {
            this.dataListMap = dataListMap;
            this.table = table;
            buildInsertSql();
        }

        private void buildInsertSql() {
            for (Map<String, Object> dataMap : dataListMap) {
                MapInsertBuilder builder = new MapInsertBuilder(table, dataMap);
                if (null == insertSql) {
                    this.insertSql = builder.getSql();
                } else {
                    builder.getSql();
                }
                paramsList.add(builder.getParams());
            }
        }

        public String getInsertSql() {
            return insertSql;
        }

        public List<Object[]> getParamsList() {
            return paramsList;
        }
    }

    /**
     * 批量插入数据Map， 默认使用下划线分割key
     *
     * @param batchSize   每次批量操作多少条记录
     * @param table       数据库表
     * @param dataMapList 数据Map列表
     * @return 返回数据库影响行数
     */
    int insertMapsByBatch(int batchSize, String table, List<Map<String, Object>> dataMapList) {
        MapBatchInsertBuilder builder = new MapBatchInsertBuilder(table, dataMapList);
        return executeBatchUpdate(batchSize, builder.getInsertSql(), builder.getParamsList());
    }

    /**
     * 批量Insert
     *
     * @author Arvin
     * @time 2016/11/15 14:20
     */
    public class BatchInsertBuilder<T> {

        private String insertSql;

        private List<Object[]> paramsList = new ArrayList<>();

        public BatchInsertBuilder(List<T> dataList) {
            buildInsertSql(dataList);
        }

        private void buildInsertSql(List<T> dataList) {
            for (T data : dataList) {
                InsertBuilder builder = new InsertBuilder(data);
                if (null == insertSql) {
                    this.insertSql = builder.getSql();
                } else {
                    builder.getSql();
                }
                paramsList.add(builder.getParams());
            }
        }

        public String getInsertSql() {
            return insertSql;
        }

        public List<Object[]> getParamsList() {
            return paramsList;
        }
    }

    /**
     * 批量插入数据， 默认使用下划线分割key
     *
     * @param batchSize 每次批量操作多少条记录
     * @param table     数据库表
     * @param modelList 数据模型列表
     * @return 返回数据库影响行数
     */
    <T> int insertModelsByBatch(int batchSize, String table, List<T> modelList) {
        BatchInsertBuilder<T> builder = new BatchInsertBuilder<>(modelList);
        return executeBatchUpdate(batchSize, builder.getInsertSql(), builder.getParamsList());
    }

    /**
     * 插入
     *
     * @param prepareInsertSql 预处理的SQL
     * @param params           参数
     */
    int insertBySql(String prepareInsertSql, Object... params) {
        return executeUpdate(prepareInsertSql, params);
    }

    /**
     * 批量插入
     *
     * @param batchSize        每次批量操作多少条记录
     * @param prepareInsertSql 预处理SQL
     * @param paramsList       参数列表
     */
    int insertByBatchForSql(int batchSize, String prepareInsertSql, List<Object[]> paramsList) {
        return executeBatchUpdate(batchSize, prepareInsertSql, paramsList);
    }

    /**
     * 删除一个模型, 会根据主键来删除，主键的查找规则为：
     * 1. 检查是否传了 primaryKeys 参数，如果传了就直接使用，并验证是否存在
     * 2. 如果没有传，检测是否存在全局的类型-主键属性列表配置，如果有就直接用，没有就继续第三步
     * 3. 如果第 2 步骤没有找到，直接从类中检测 PrimaryKey 标签，把有 PrimaryKey 标签的作为主键并添加到全局的配置中
     * 4. 如果第 3 步骤还是没有，直接抛出 PrimaryKeyNotFoundException
     *
     * @param model       要删除的模型对象
     * @param primaryKeys java主键属性，如果填写了，就会忽略全局的配置，没有填写就按照全局的配置，全局都没有的话就使用ID
     * @param <T>         模型的类型
     * @throws PrimaryNotFoundException 主键找不到异常
     */
    <T> int deleteByPrimaryKey(T model, String... primaryKeys) throws PrimaryNotFoundException {
        List<String> primaryKeyList = getPrimaryKeyList(model.getClass(), primaryKeys);
        String[] pkArray = new String[primaryKeyList.size()];
        pkArray = primaryKeyList.toArray(pkArray);
        Object[] pkValues = getFieldValues(model, pkArray);
        String table = getTablename(model.getClass());
        StringBuilder builder = new StringBuilder("DELETE FROM ").append(table).append(" WHERE");
        for (String pkName : pkArray) {
            builder.append(" ").append(getDBFieldName(null, table, pkName)).append("=? AND");
        }
        String deleteSql = builder.toString().replaceAll("AND *$", "");
        return executeUpdate(deleteSql, pkValues);
    }

    private Object[] getFieldValues(Object obj, String[] fieldsName) {
        if (null == fieldsName || fieldsName.length < 1) {
            throw new RuntimeException("Invalid Field name!");
        }
        Object[] values = new Object[fieldsName.length];
        int i = 0;
        for (String fieldName : fieldsName) {
            values[i] = ReflectUtils.getFieldValue(obj, ReflectUtils.findDeclaredField(obj.getClass(), fieldName));
        }
        return values;
    }

    /**
     * 删除一个模型, 会根据主键来删除，主键的查找规则为：
     * 1. 检查是否传了 primaryKeys 参数，如果传了就直接使用，并验证是否存在
     * 2. 如果没有传，检测是否存在全局的类型-主键属性列表配置，如果有就直接用，没有就继续第三步
     * 3. 如果第 2 步骤没有找到，直接从类中检测 PrimaryKey 标签，把有 PrimaryKey 标签的作为主键并添加到全局的配置中
     * 4. 如果第 3 步骤还是没有，直接抛出 PrimaryKeyNotFoundException
     *
     * @param modelType
     * @param customPrimaryKeys
     * @return
     */
    private List<String> getPrimaryKeyList(Class<?> modelType, String[] customPrimaryKeys) {
        if (customPrimaryKeys != null && customPrimaryKeys.length < 1) {
            return Arrays.asList(customPrimaryKeys);
        }
        String table = getTablename(modelType);
        Set<String> primaryKeySet = GlobalConfig.getTablePrimaryKeys(table);
        if (primaryKeySet != null && !primaryKeySet.isEmpty()) {
            return new ArrayList<>(primaryKeySet);
        }
        List<Field> fields = ReflectUtils.getNoneStaticDeclaredFields(modelType, null);
        if (fields == null || fields.isEmpty()) {
            throw new PrimaryNotFoundException();
        }
        List<String> resultList = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(PrimaryKey.class) != null) {
                resultList.add(field.getName());
            }
        }
        if (resultList.isEmpty()) {
            throw new PrimaryNotFoundException();
        }
        return resultList;
    }

    /**
     * 删除
     *
     * @param prepareSql 预处理SQL
     * @param params     参数列表
     */
    int delete(String prepareSql, Object... params) {
        return executeUpdate(prepareSql, params);
    }

    /**
     * 更新一个模型, 会根据主键来更新，主键的查找规则为：
     * 1. 检查是否传了 primaryKeys 参数，如果传了就直接使用，并验证是否存在
     * 2. 如果没有传，检测是否存在全局的类型-主键属性列表配置，如果有就直接用，没有就继续第三步
     * 3. 如果第 2 步骤没有找到，直接从类中检测 PrimaryKey 标签，把有 PrimaryKey 标签的作为主键并添加到全局的配置中
     * 4. 如果第 3 步骤还是没有，直接抛出 PrimaryKeyNotFoundException
     *
     * @param model       要删除的模型对象
     * @param primaryKeys java主键属性，如果填写了，就会忽略全局的配置，没有填写就按照全局的配置，全局都没有的话就使用ID
     * @param <T>         模型的类型
     * @throws PrimaryNotFoundException 主键找不到异常
     */
    <T> int update(T model, String... primaryKeys) throws PrimaryNotFoundException {
        // TODO
        return 0;
    }

    /**
     * 更新Map
     *
     * @param table   数据库表名称
     * @param dataMap 数据Map
     * @throws PrimaryNotFoundException 主键找不到异常
     */
    int update(String table, Map<String, Object> dataMap, String... primaryKeys) throws PrimaryNotFoundException {
        // TODO
        return 0;
    }

    /**
     * 更新
     *
     * @param prepareSql 预处理语句
     * @param params     参数列表
     */
    int update(String prepareSql, Object... params) {
        return executeUpdate(prepareSql, params);
    }

    /**
     * 根据ID获取指定的模型对象
     *
     * @param modelType    模型类型
     * @param idValue      ID值
     * @param modelBuilder 模型构造器
     * @param <T>          模型类型
     * @param <K>          模型的主键类型
     */
    <T, K> T getById(Class<T> modelType, K idValue, ModelBuilder<T> modelBuilder) {
        return getByPrimaryKey(modelType, "id", idValue, modelBuilder);
    }

    /**
     * 获取单个主键的模型
     *
     * @param modelType       模型类型
     * @param primaryKey      主键java属性
     * @param primaryKeyValue 主键值
     * @param modelBuilder    模型构造器
     * @param <T>             模型类型
     * @param <K>             主键类型
     * @throws PrimaryNotFoundException 主键找不到异常
     */
    <T, K> T getByPrimaryKey(Class<T> modelType, String primaryKey, K primaryKeyValue, ModelBuilder<T> modelBuilder) throws PrimaryNotFoundException {
        return getByPrimaryKeys(modelType, new String[]{primaryKey}, new Object[]{primaryKeyValue}, modelBuilder);
    }

    /**
     * 联合主键获取模型
     *
     * @param modelType        模型类型
     * @param primaryKeys      主键java属性数组
     * @param primaryKeyValues 主键值列表
     * @param <T>              主键类型
     * @throws PrimaryNotFoundException 主键找不到异常
     */
    <T> T getByPrimaryKeys(Class<T> modelType, String[] primaryKeys, Object[] primaryKeyValues, ModelBuilder<T> modelBuilder) throws PrimaryNotFoundException {
        if (modelType == null || primaryKeys == null || primaryKeys.length < 1 || primaryKeyValues == null || primaryKeyValues.length < 1) {
            throw new RuntimeException("Invalid params!");
        }
        String table = getTablename(modelType);
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + table + " WHERE ");
        for (String pk : primaryKeys) {
            sqlBuilder.append(wrapperDBAttr(pk)).append("=? AND ");
        }
        String prepareSql = sqlBuilder.toString().replaceFirst("AND *$", "LIMIT 1");
        return getModel(prepareSql, modelType, modelBuilder, primaryKeyValues);
    }

    /**
     * 查询数量，如果没有就返回0
     *
     * @param prepareSql count查询语句
     * @param params     参数
     */
    int getCount(String prepareSql, Object... params) {
        return getPrimitive(prepareSql, Integer.class, params);
    }

    /**
     * 查询数据Map
     *
     * @param prepareSql 查询sql
     * @param params     参数
     */
    Map<String, Object> getMap(String prepareSql, Object... params) {
        showLog(prepareSql, "java.util.Map<String, Object>", params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String table = ThreadConfig.getOperateTable();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return toMap(table, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return null;
    }

    private Map<String, Object> toMap(String table, ResultSet rs) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; ++i) {
                String columnName = getModelFieldName(table, metaData.getColumnName(i));
                Object columnValue = rs.getObject(i);
                resultMap.put(columnName, columnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 查询符合条件Map列表，如果要进行分页查询，请先调用 paging 方法
     *
     * @param prepareSql 查询sql
     * @param params     参数列表
     */
    List<Map<String, Object>> listMap(String prepareSql, Object... params) {
        if (ThreadConfig.isPaging()) {
            prepareSql = prepareSql + " LIMIT " + (ThreadConfig.getCurrentPage() - 1) * ThreadConfig.getPageSize() + "," + ThreadConfig.getPageSize();
        }
        showLog(prepareSql, "List<Map<String, Object>>", params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String table = ThreadConfig.getOperateTable();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                resultList.add(toMap(table, rs));
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    /**
     * 查询模型
     *
     * @param prepareSql   查询sql
     * @param requiredType 要求的模型类型
     * @param modelBuilder 模型Builder
     * @param params       参数
     * @param <T>          类型
     */
    <T> T getModel(String prepareSql, Class<T> requiredType, ModelBuilder<T> modelBuilder, Object... params) {
        showLog(prepareSql, requiredType, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (null == modelBuilder) {
                    return toObject(requiredType, rs);
                } else {
                    return modelBuilder.build(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return null;
    }

    private <T> T toObject(Class<T> requiredType, ResultSet rs) {
        try {
            String table = rs.getMetaData().getTableName(1);
            List<Field> fields = ReflectUtils.getNoneStaticDeclaredFields(requiredType, null);
            if (null != fields && !fields.isEmpty()) {
                T result = requiredType.newInstance();
                for (Field field : fields) {
                    String dbFieldName = unwrapperDBAttr(getDBFieldName(field, table, field.getName()));
                    try {
                        setFieldValue(result, field, rs.getObject(dbFieldName));
                    } catch (SQLException e) {
                        if (ThreadConfig.isIgnoredUnknownField()) {
                            if (ThreadConfig.enabledLog()) {
                                System.out.println("Ignored Field: " + field.getName());
                            }
                        } else {
                            throw new RuntimeException("没有对应 [" + field.getName() + "] 的属性！");
                        }
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置属性的值
     */
    private static void setFieldValue(Object obj, Field field, Object value) {
        field.setAccessible(true);
        Object realValue = value;
        try {
            if (value instanceof Number) {
                realValue = getNumberValue(value, field.getType());
            }
            field.set(obj, realValue);
        } catch (IllegalAccessException ignored) {
        }
    }

    private static <T> Object getNumberValue(Object value, Class<T> targetType) {
        if (null == value) {
            try {
                return targetType.newInstance();
            } catch (Exception e) {
            }
        }
        if (value instanceof Number) {
            Number number = (Number) value;
            if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
                return number.intValue();
            } else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
                return number.longValue();
            } else if (targetType.equals(Short.class) || targetType.equals(short.class)) {
                return number.shortValue();
            } else if (targetType.equals(Double.class) || targetType.equals(double.class)) {
                return number.doubleValue();
            } else if (targetType.equals(Float.class) || targetType.equals(float.class)) {
                return number.floatValue();
            } else if (targetType.equals(Byte.class) || targetType.equals(byte.class)) {
                return number.byteValue();
            }
        }
        return value;
    }

    /**
     * 查询模型列表，如果要进行分页查询，请先调用 paging 方法
     *
     * @param prepareSql   查询sql
     * @param requiredType 要求的模型类型
     * @param params       参数
     * @param <T>          类型
     */
    <T> List<T> listModel(String prepareSql, Class<T> requiredType, ModelBuilder<T> modelBuilder, Object... params) {
        if (ThreadConfig.isPaging()) {
            prepareSql = prepareSql + " LIMIT " + (ThreadConfig.getCurrentPage() - 1) * ThreadConfig.getPageSize() + "," + ThreadConfig.getPageSize();
        }
        showLog(prepareSql, requiredType, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (null == modelBuilder) {
                    resultList.add(toObject(requiredType, rs));
                } else {
                    resultList.add(modelBuilder.build(rs));
                }
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    /**
     * 查询内置类型，包含：
     * <p>
     * Integer，int，Long，long，Double，double，Short，short，Date，String
     *
     * @param prepareSql   sql
     * @param requiredType 结果类型
     * @param params       参数
     * @param <T>          结果类型
     * @throws InvalidPrimitiveTypeException 不合法的内置类型
     */
    <T> T getPrimitive(String prepareSql, Class<T> requiredType, Object... params) throws InvalidPrimitiveTypeException {
        showLog(prepareSql, requiredType, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Object obj = rs.getObject(1);
                if (null != obj) {
                    return requiredType.cast(obj instanceof Number ? getNumberValue(obj, requiredType) : obj);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return null;
    }

    /**
     * 如果要进行分页查询，请先调用 paging 方法
     * <p>
     * 查询内置列表，包含：
     * <p>
     * Integer，int，Long，long，Double，double，Short，short，Date，String
     *
     * @param prepareSql   sql
     * @param requiredType 结果类型
     * @param params       参数
     * @param <T>          结果类型
     * @throws InvalidPrimitiveTypeException 不合法的内置类型
     */
    <T> List<T> listPrimitive(String prepareSql, Class<T> requiredType, Object... params) throws InvalidPrimitiveTypeException {
        showLog(prepareSql, requiredType, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object obj = rs.getObject(1);
                if (null != obj) {
                    resultList.add(requiredType.cast(obj instanceof Number ? getNumberValue(obj, requiredType) : obj));
                } else {
                    resultList.add(null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    /**
     * 执行更新语句接口
     *
     * @param prepareSql sql
     * @param params     参数列表
     */
    int executeUpdate(String prepareSql, Object... params) {
        showLog(prepareSql, Integer.class, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(prepareSql);
            fixParams(pstmt, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt);
        }
        return 0;
    }

    // TODO 可放到线程变量中
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void fixParams(PreparedStatement pstmt, Object[] params) throws SQLException {
        if (null != params && params.length > 0) {
            if (params.length == 1 && params[0] instanceof List) {
                params = ((List) params[0]).toArray();
            }
            for (int i = 0; i < params.length; ++i) {
                Object param = params[i];
                if (param instanceof Date) {
                    pstmt.setString(i + 1, dateFormat.format((Date) param));
                } else {
                    try {
                        pstmt.setObject(i + 1, params[i]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 执行批量更新
     *
     * @param batchSize  每次更新多少
     * @param prepareSql sql
     * @param paramsList 参数
     */
    int executeBatchUpdate(int batchSize, String prepareSql, List<Object[]> paramsList) {
        showLog(prepareSql, Integer.class);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = openConnection();
            int totalSize = paramsList.size();
            // 要执行多少次批处理
            int batchPage = totalSize % batchSize == 0 ? totalSize / batchSize : totalSize / batchSize + 1;
            int effectiveCount = 0;
            for (int i = 0; i < batchPage; ++i) {
                int begIndex = i * batchSize;
                int endIndex = i + batchSize - 1;
                endIndex = endIndex >= totalSize ? totalSize - 1 : endIndex;
                try {
                    if (null != pstmt) {
                        pstmt.close();
                    }
                } catch (Exception ignored) {
                }

                pstmt = conn.prepareStatement(prepareSql);
                for (int j = begIndex; j <= endIndex; ++j) {
                    fixParams(pstmt, paramsList.get(j));
                    pstmt.addBatch();
                }
                // 执行
                int[] result = pstmt.executeBatch();
                for (Integer k : result) {
                    effectiveCount += k;
                }
            }
            return effectiveCount;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt);
        }
        return 0;
    }
}
