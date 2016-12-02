package Database;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/**
 * <pre>
 * POM 依赖：
 *  MySQL 驱动
 *  <dependency>
 *      <groupId>mysql</groupId>
 *      <artifactId>mysql-connector-java</artifactId>
 *      <version>5.1.29</version>
 *  </dependency>
 *
 *
 *  Oracle 驱动
 *  <dependency>
 *      <groupId>org.oracle</groupId>
 *      <artifactId>ojdbc11</artifactId>
 *      <version>11.2.0.2.4</version>
 *  </dependency>
 *
 *
 *  注： Oracle的驱动包，在Maven的中央没有，需要自己下载驱动包，然后安装到本地仓库或是安装到私有仓库才能引用
 *      驱动包下载地址： http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html
 *      或百度直接搜索： oracle driver download
 *      下载界面选择一个合适版本下载，如 ojdbc6.jar
 *  安装到本地仓库
 *      我们一般按照Oracle版本命名，如下载到的ojdbc.jar, 下载界面可能是Oracle Database 11g Release 2 (11.2.0.4) JDBC Drivers
 *      那么我们就定义好artifactId = ojdbc11, version=11.2.0.4, groupId = org.oracle
 *      mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.1.4 -Dpackaging=jar -Dfile=ojdbc6.jar
 *  安装到私有仓库
 *      mvn deploy:deploy-file -DgroupId=com.oracle -DartifactId=ojdbc11 -Dversion=11.2.0.1.4 -Dpackaging=jar -Dfile=ojdbc6.jar -Durl={private.repository.url} -DrepositoryId=thirdparty
 *      其中-Durl即为私有仓库地址，如： http://124.232.150.208:8181/nexus/content/repositories/thirdparty/
 *      注意，你需要在全局的settings.xml中配置好访问这个仓库地址的帐号和密码，才能发布上去
 *      当然，你也可以直接使用Nexus私有仓库后台的管理界面实现私有库的添加
 *
 * 使用：
 *  MySQL:  DBUtils.mysqlBuilder(schema); // 参数不能为空
 *  Oracle: DBUtils.oracleBuilder(instance); // 如果为空，默认就是XE
 *
 *  DBUtils jdbc = DBUtils.builder(schema) // 建造者模式, 参数为要连接的实例
 *                        .driver() // 默认MySQL驱动。定义驱动, mysql = DBUtils.DRIVER_MYSQL = com.mysql.jdbc.Driver, DBUtils.DRIVER_ORACLE = oracle.jdbc.driver.OracleDriver
 *                        .host() // 数据库主机, 默认localhost
 *                        .port() // 数据库主机端口， 默认3306
 *                        .username() // 数据库用户名，默认root
 *                        .password() // 数据库密码，默认admin
 *                        .underlineField() // 数据库字段规则，是否使用下划线分割，默认是true， false表示字段使用驼峰命名
 *                        .underlineTable() // 数据库表名规则，是否使用下划线分割，默认是true， false表示使用驼峰命名
 *                        .stdlog() // 是否开启System.out日志，默认开启
 *                        .ignoredUnknownField() // 查询的时候，返回对象时，是否忽略未知属性的赋值，默认是true，如果为false，那么当出现位置属性的时候，将抛出异常
 *
 * </pre>
 *
 * @author 夏集球
 * @time 2016/9/23$ 11:52$
 */
@SuppressWarnings({"unused"})
public class DBUtils {
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
    /**
     * 数据库字段是否使用下划线分割
     */
    private final boolean underlineFiled;
    /**
     * 数据库表是否使用下划线分割
     */
    private final boolean underlineTable;
    /**
     * 标准输出日志
     */
    private boolean stdlog = true;
    /**
     * 忽略未知属性
     */
    private boolean ignoredUnknownField = true;


    public static class Builder {
        private String driver = DRIVER_MYSQL;
        private String username = "root";
        private String password = "admin";
        private String schema;
        private String jdbcUrl = JDBC_URL_MYSQL;
        private int port = 3306;
        private String host = "localhost";
        private boolean underlineFiled = true;
        private boolean underlineTable = true;
        private boolean stdlog = true;
        private boolean ignoredUnknownField = true;

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

        public Builder underlineField(boolean underlineFiled) {
            this.underlineFiled = underlineFiled;
            return this;
        }

        public Builder underlineTable(boolean underlineTable) {
            this.underlineTable = underlineTable;
            return this;
        }

        private Builder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        private Builder stdlog(boolean stdlog) {
            this.stdlog = stdlog;
            return this;
        }

        private Builder ignoreUnknownField(boolean ignoredUnknownField) {
            this.ignoredUnknownField = ignoredUnknownField;
            return this;
        }

        public DBUtils build() {
            String url = jdbcUrl.replace("{host}", host).replace("{port}", "" + port).replace("{schema}", schema);
            return new DBUtils(driver, url, username, password, underlineFiled, underlineTable, stdlog, ignoredUnknownField);
        }
    }

    private DBUtils(String driver, String jdbcUrl, String username, String password, boolean underlineFiled, boolean underlineTable, boolean stdlog, boolean ignoredUnknownField) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.underlineFiled = underlineFiled;
        this.underlineTable = underlineTable;
        this.stdlog = stdlog;
        this.ignoredUnknownField = ignoredUnknownField;

        initContext();
    }

    private void initContext() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化异常：" + this.toString());
        }
    }

    private void showInfo(String sql, Class<?> resultType, boolean underlineTable, boolean underlineFiled, Object... params) {
        showInfo(sql, (null == resultType ? "Not Required!" : resultType.getName()), underlineTable, underlineFiled, params);
    }

    private void showInfo(String sql, String resultType, boolean underlineTable, boolean underlineFiled, Object... params) {
        if (!stdlog) {
            return;
        }
        int index = sql.indexOf(" ");
        String sqlType = sql.substring(0, index).toUpperCase();
        System.out.println(">>> -------------------------------------------------------------------------------------------------");
        System.out.println(">>> Execute Info:\t" + sqlType);
        System.out.println(">>> ResultType: \t" + (null == resultType ? "Not Required!" : resultType));
        System.out.println(">>> underlineField:\t" + underlineFiled);
        System.out.println(">>> underlineTable:\t" + underlineTable);
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

    @Override
    public String toString() {
        return "DBUtils{" +
                "driver='" + driver + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static SQL sqlBuilder() {
        return new SQL();
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

    public DBUtils setStdlog(boolean stdlog) {
        this.stdlog = stdlog;
        return this;
    }

    public boolean isUnderlineFiled() {
        return underlineFiled;
    }

    public boolean isUnderlineTable() {
        return underlineTable;
    }

    public boolean isStdlog() {
        return stdlog;
    }

    public boolean isIgnoredUnknownField() {
        return ignoredUnknownField;
    }

    public void setIgnoredUnknownField(boolean ignoredUnknownField) {
        this.ignoredUnknownField = ignoredUnknownField;
    }

    /**
     * 执行SQL更新操作
     */
    public int executeUpdate(String sql, Object... params) {
        showInfo(sql, Integer.class, underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(true);
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt);
        }
        return 0;
    }

    /**
     * 批量更新
     *
     * @param updateSql  sql语句
     * @param paramsList 参数
     * @param batchSize  每次批处理多少记录
     */
    public int executeBatchUpdate(String updateSql, List<Object[]> paramsList, int batchSize) {
        showInfo(updateSql, Integer.class, underlineTable, underlineFiled);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(true);

            int totalSize = paramsList.size();
            // 要执行多少次批处理
            int batchPage = totalSize % batchSize == 0 ? totalSize / batchSize : totalSize / batchSize + 1;
            int effectiveCount = 0;
            for (int i = 0; i < batchPage; ++i) {
                int begIndex = i * batchSize;
                int endIndex = begIndex + batchSize - 1;
                endIndex = endIndex >= totalSize ? totalSize - 1 : endIndex;
                try {
                    if (null != pstmt) {
                        pstmt.close();
                    }
                } catch (Exception ignored) {
                }

                pstmt = conn.prepareStatement(updateSql);
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

    /**
     * 插入数据库
     */
    public int insert(Object model) {
        if (null == model) {
            return 0;
        }
        return insert(getDBTableName(model.getClass(), underlineTable), model, underlineFiled);
    }

    public int insert(Object model, String... ignoredFields) {
        if (null == model) {
            return 0;
        }
        return insert(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, ignoredFields);
    }

    public int insert(String table, Object model, String... ignoredFields) {
        return insert(table, model, this.underlineFiled, ignoredFields);
    }

    public int insert(Object model, boolean underlineFiled) {
        return insert(getDBTableName(model.getClass(), underlineTable), model, underlineFiled);
    }

    public int insert(Object model, boolean underlineFiled, String... ignoredFields) {
        return insert(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, ignoredFields);
    }

    public int insert(String table, Object model, boolean underlineFiled, String... ignoredFields) {
        InsertSQL sql = new InsertSQL(table, model, underlineFiled, ignoredFields);
        return executeUpdate(sql.getSql(), sql.getParams());
    }

    public int delete(String table, String primaryKey, Object primaryKeyValue) {
        return executeUpdate("DELETE FROM `" + table + "` WHERE `" + primaryKey + "` = ?", primaryKeyValue);
    }

    public int delete(Class<?> modelType, String primaryKey, Object primaryKeyValue) {
        return delete(getDBTableName(modelType, underlineTable), primaryKey, primaryKeyValue);
    }

    public int delete(Class<?> modelType, boolean underlineTable, String primaryKey, Object primaryKeyValue) {
        return delete(getDBTableName(modelType, underlineTable), primaryKey, primaryKeyValue);
    }

    public int delete(Object model, String primaryKey) {
        return delete(getDBTableName(model.getClass(), underlineTable), primaryKey, getFieldValue(model, primaryKey));
    }

    public int delete(Object model, String primaryKey, boolean underlineTable) {
        return delete(getDBTableName(model.getClass(), underlineTable), primaryKey, getFieldValue(model, primaryKey));
    }

    public int delete(String sql, Object... params) {
        return executeUpdate(sql, params);
    }

    public int update(Object model, String... ignoredFields) {
        return update(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, ignoredFields, null);
    }

    public int update(Object model, String[] ignoredFields, String[] primaryKeys) {
        return update(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, ignoredFields, primaryKeys);
    }

    public int update(Object model, boolean underlineFiled, String[] primaryKeys) {
        return update(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, null, primaryKeys);
    }

    public int update(Object model, boolean underlineFiled, String[] ignoredFields, String[] primaryKeys) {
        return update(getDBTableName(model.getClass(), underlineTable), model, underlineFiled, ignoredFields, primaryKeys);
    }

    public int update(String table, Object model, String[] primaryKeys) {
        return update(table, model, underlineFiled, primaryKeys);
    }

    public int update(String table, Object model, String[] ignoredFields, String[] primaryKeys) {
        return update(table, model, underlineFiled, ignoredFields, primaryKeys);
    }

    /**
     * 更新对象
     */
    public int update(String table, Object model, boolean underlineFiled, String[] primaryKeys) {
        UpdateModelSQL updateSql = new UpdateModelSQL(table, model, underlineFiled, null, primaryKeys);
        return executeUpdate(updateSql.getSql(), updateSql.getParams());
    }

    public int update(String table, Object model, boolean underlineFiled, String[] ignoredFields, String[] primaryKeys) {
        UpdateModelSQL updateSql = new UpdateModelSQL(table, model, underlineFiled, ignoredFields, primaryKeys);
        return executeUpdate(updateSql.getSql(), updateSql.getParams());
    }

    public <T> T getById(Class<T> requiredType, boolean underlineTable, boolean underlineFiled, String idname, Object value) {
        return getById(getDBTableName(requiredType, underlineTable), requiredType, underlineTable, underlineFiled, idname, value);
    }

    public <T> T getById(String tableName, Class<T> requiredType, boolean underlineTable, boolean underlineFiled, String idname, Object value) {
        return getObject(
                "SELECT * FROM `" + tableName + "` WHERE `" + (null == idname || "".equals(idname.trim()) ? "id" : idname) + "`",
                requiredType,
                underlineFiled,
                new Object[]{value});
    }

    public <T> T getById(Class<T> requiredType, Object value) {
        return getById(requiredType, underlineTable, underlineFiled, "id", value);
    }

    public <T> T getById(String tableName, Class<T> requiredType, Object value) {
        return getById(tableName, requiredType, underlineTable, underlineFiled, "id", value);
    }

    public <T> T getObject(String sql, Class<T> requiredType, Object... params) {
        return getObject(sql, requiredType, underlineFiled, null, params);
    }

    public <T> T getObject(String sql, Class<T> requiredType, ModelConverter<T> converter, Object... params) {
        return getObject(sql, requiredType, underlineFiled, converter, params);
    }

    public <T> T getObject(String sql, Class<T> requiredType, List<Object> params) {
        return getObject(sql, requiredType, underlineFiled, null, params.toArray());
    }

    public <T> T getObject(String sql, Class<T> requiredType, ModelConverter<T> converter, List<Object> params) {
        return getObject(sql, requiredType, underlineFiled, converter, params.toArray());
    }

    public <T> T getObject(String sql, Class<T> requiredType, boolean underlineFiled, List<Object> params) {
        return getObject(sql, requiredType, underlineFiled, null, params.toArray());
    }

    public <T> T getObject(String sql, Class<T> requiredType, boolean underlineFiled, ModelConverter<T> converter, List<Object> params) {
        return getObject(sql, requiredType, underlineFiled, converter, params.toArray());
    }

    public <T> T getObject(String sql, Class<T> requiredType, boolean underlineFiled, ModelConverter<T> converter, Object... params) {
        showInfo(sql, requiredType, underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (null == converter) {
                    return toObject(requiredType, rs, underlineFiled);
                } else {
                    return converter.convert(rs);
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
     * 获取原始类型
     */
    public <T> T getPrimitive(String sql, Class<T> requiredType, T defaultValue, Object... params) {
        showInfo(sql, requiredType, underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Object obj = rs.getObject(1);
                if (null != obj) {
                    return requiredType.cast(obj);
                } else {
                    return defaultValue;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return defaultValue;
    }

    public <T> T getPrimitive(String sql, Class<T> requiredType, Object... params) {
        return getPrimitive(sql, requiredType, null, params);
    }

    public <T> T getPrimitive(String sql, Class<T> requiredType, List<Object> params) {
        return getPrimitive(sql, requiredType, null, params);
    }

    public <T> T getPrimitive(String sql, Class<T> requiredType, T defaultValue, List<Object> params) {
        return getPrimitive(sql, requiredType, defaultValue, params.toArray());
    }

    public int getInt(String sql, Integer defaultValue, Object... params) {
        Number result = getPrimitive(sql, Number.class, params);
        return null == result ? defaultValue : result.intValue();
    }

    public int getNextIntId(String tableName) {
        return getInt("SELECT MAX(`id`) FROM `" + tableName + "`", 0) + 1;
    }

    public int getNextIntId(String tableName, String idFieldName) {
        return getInt("SELECT MAX(`" + idFieldName + "`) FROM `" + tableName + "`", 0) + 1;
    }

    public int getNextIntId(Class<?> modelType, String idFieldName) {
        return getInt("SELECT MAX(`" + idFieldName + "`) FROM `" + getDBTableName(modelType, underlineTable) + "`", 0) + 1;
    }

    public int getNextIntId(Class<?> modelType) {
        return getInt("SELECT MAX(`id`) FROM `" + getDBTableName(modelType, underlineTable) + "`", 0) + 1;
    }

    public long getNextLongId(String tableName, String idFieldName) {
        return getLong("SELECT MAX(`" + idFieldName + "`) FROM `" + tableName + "`", 0L) + 1L;
    }

    public long getNextLongId(String tableName) {
        return getLong("SELECT MAX(`id`) FROM `" + tableName + "`", 0L) + 1L;
    }

    public long getNextLongId(Class<?> modelType, String idFieldName) {
        return getLong("SELECT MAX(`" + idFieldName + "`) FROM `" + getDBTableName(modelType, underlineTable) + "`", 0L) + 1L;
    }

    public long getNextLongId(Class<?> modelType) {
        return getLong("SELECT MAX(`id`) FROM `" + getDBTableName(modelType, underlineTable) + "`", 0L) + 1L;
    }

    public long getLong(String sql, Long defaultValue, Object... params) {
        Number result = getPrimitive(sql, Number.class, params);
        return null == result ? defaultValue : result.longValue();
    }

    public Date getTime(String sql, Object... params) {
        return getTime(sql, null, params);
    }

    public Date getTime(String sql, List<Object> params) {
        return getTime(sql, null, params.toArray());
    }

    public Date getTime(String sql, Date defaultValue, List<Object> params) {
        return getTime(sql, defaultValue, params.toArray());
    }

    public Date getTime(String sql, Date defaultValue, Object... params) {
        showInfo(sql, Date.class, underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Date result = rs.getDate(1);
                return null == result ? defaultValue : result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return defaultValue;
    }

    public <T> List<T> getPrimitiveList(String sql, Class<T> requiredType, List<Object> params) {
        return getPrimitiveList(sql, requiredType, params.toArray());
    }

    public <T> List<T> getPrimitiveList(String sql, Class<T> requiredType, Object... params) {
        showInfo(sql, List.class.getName() + "<" + requiredType.getSimpleName() + ">", underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object value = rs.getObject(1);
                if (value instanceof Number) {
                    Number number = (Number) value;
                    if (requiredType.equals(Integer.class) || requiredType.equals(int.class)) {
                        value = number.intValue();
                    } else if (requiredType.equals(Long.class) || requiredType.equals(long.class)) {
                        value = number.longValue();
                    } else if (requiredType.equals(Short.class) || requiredType.equals(short.class)) {
                        value = number.shortValue();
                    } else if (requiredType.equals(Double.class) || requiredType.equals(double.class)) {
                        value = number.doubleValue();
                    } else if (requiredType.equals(Float.class) || requiredType.equals(float.class)) {
                        value = number.floatValue();
                    } else if (requiredType.equals(Byte.class) || requiredType.equals(byte.class)) {
                        value = number.byteValue();
                    }
                }
                resultList.add(requiredType.cast(value));
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, boolean underlineFiled, List<Object> params) {
        return getModelList(sql, requiredType, underlineFiled, null, params.toArray());
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, boolean underlineFiled, ModelConverter<T> converter, List<Object> params) {
        return getModelList(sql, requiredType, underlineFiled, converter, params.toArray());
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, Object... params) {
        return getModelList(sql, requiredType, underlineFiled, params);
    }


    public <T> List<T> getModelList(String sql, Class<T> requiredType, ModelConverter<T> converter, Object... params) {
        return getModelList(sql, requiredType, underlineFiled, converter, params);
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, List<Object> params) {
        return getModelList(sql, requiredType, underlineFiled, params.toArray());
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, ModelConverter<T> converter, List<Object> params) {
        return getModelList(sql, requiredType, underlineFiled, converter, params.toArray());
    }

    public <T> List<T> getModelList(String sql, Class<T> requiredType, boolean underlineFiled, ModelConverter<T> converter, Object... params) {
        showInfo(sql, "java.util.List<" + requiredType.getSimpleName() + ">", underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                resultList.add(toObject(requiredType, rs, underlineFiled));
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    private <T> T toObject(Class<T> requiredType, ResultSet rs, boolean underlineFiled) {
        try {
            List<Field> fields = getDeclaredFields(requiredType, null);
            if (null != fields && !fields.isEmpty()) {
                T result = requiredType.newInstance();
                for (Field field : fields) {
                    String dbFieldName = getDBFieldName(field.getName(), underlineFiled);
                    try {
                        setFieldValue(result, field, rs.getObject(dbFieldName));
                    } catch (SQLException e) {
                        if (ignoredUnknownField) {
                            if (stdlog) {
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

    public static <T> T toObject(Map<String, Object> map, Class<T> requiredType) {
        try {
            List<Field> fields = getDeclaredFields(requiredType, null);
            if (null != fields && !fields.isEmpty()) {
                T result = requiredType.newInstance();
                for (Field field : fields) {
                    setFieldValue(result, field, map.get(field.getName()));
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getMap(String sql, Object... params) {
        return getMap(sql, underlineFiled, params);
    }

    public Map<String, Object> getMap(String sql, boolean underlineFiled, Object... params) {
        showInfo(sql, "java.util.Map<String, Object>", underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return toMap(rs, underlineFiled);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return null;
    }

    public List<Map<String, Object>> getListMap(String sql, List<Object> params) {
        return getListMap(sql, underlineFiled, params.toArray());
    }

    public List<Map<String, Object>> getListMap(String sql, boolean underlineFiled, List<Object> params) {
        return getListMap(sql, underlineFiled, params.toArray());
    }

    public List<Map<String, Object>> getListMap(String sql, boolean underlineFiled, Object... params) {
        showInfo(sql, "java.util.List<Map<String, Object>>", underlineTable, underlineFiled, params);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            conn = openConnection();
            pstmt = conn.prepareStatement(sql);
            fixParams(pstmt, params);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                resultList.add(toMap(rs, underlineFiled));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn).close(pstmt).close(rs);
        }
        return resultList;
    }

    private Map<String, Object> toMap(ResultSet rs, boolean underlineFiled) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; ++i) {
                String columnName = getModelFieldName(metaData.getColumnName(i), underlineFiled);
                Object columnValue = rs.getObject(i);
                resultMap.put(columnName, columnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 将数据库字段转成Java模型字段
     */
    public static String getModelFieldName(String name, boolean underlineFiled) {
        if (null == name || name.trim().equals("") || !name.contains("_")) {
            return name;
        }
        String[] array = name.split("_");
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

    /**
     * 获取数据库字段
     */
    public static String getDBFieldName(String name, boolean underlineFiled) {
        if (underlineFiled) {
            // 以下划线分割
            return firstLetterToLowerCase(name).replaceAll("([A-Z]+)", "_$1").toLowerCase();
        }
        return name;
    }

    /**
     * 获取数据库表名称
     */
    public static String getDBTableName(Class<?> modelType, boolean underlineTable) {
        String className = firstLetterToLowerCase(modelType.getSimpleName());
        if (underlineTable) {
            // 以下划线分割
            return className.replaceAll("([A-Z]+)", "_$1").toLowerCase();
        }
        return className;
    }

    /**
     * 首字母小写
     */
    private static String firstLetterToLowerCase(String value) {
        return null != value && value.length() > 0 ? String.valueOf(value.charAt(0)).toLowerCase() + value.substring(1) : null;
    }

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

    private Connection openConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("不能获取数据库链接");
        }
    }

    private DBUtils close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public DBUtils close(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private DBUtils close(ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private DBUtils close(Connection conn, Statement statement, ResultSet resultSet) {
        close(conn);
        close(statement);
        close(resultSet);
        return this;
    }

    /**
     * 查找指定类型的属性列表
     *
     * @param clazz     类名
     * @param fieldType 字段类型， 如果为空就返回所有的字段
     * @return 返回指定类型的属性列表，如果为空就返回所有的字段
     */
    private static List<Field> getDeclaredFields(Class<?> clazz, Class<?> fieldType) {
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
     */
    private static Object getFieldValue(Object obj, Field field) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 获取字段的值
     */
    private static Object getFieldValue(Object obj, String fieldName) {
        Field field = findDeclaredField(obj.getClass(), fieldName);
        if (null == field) {
            return null;
        }
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
    private static Field findDeclaredField(Class<?> clazz, String fieldName) {
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
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
                Number number = (Number) value;
                Class<?> fieldType = field.getType();
                if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                    realValue = number.intValue();
                } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    realValue = number.longValue();
                } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                    realValue = number.shortValue();
                } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                    realValue = number.doubleValue();
                } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                    realValue = number.floatValue();
                } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                    realValue = number.byteValue();
                }
            }
            field.set(obj, realValue);
        } catch (IllegalAccessException ignored) {
        }
    }

    /**
     * 设置属性的值
     */
    private static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = findDeclaredField(obj.getClass(), fieldName);
        if (null == field) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    private static abstract class BaseSQL {

        protected String sql;
        protected List<Object> params;

        protected boolean isInclude(String[] fields, String detectField) {
            if (null != fields && fields.length > 0) {
                for (String field : fields) {
                    if (null != field) {
                        field = field.contains("_") ? getModelFieldName(field, true) : field;
                        if (field.equals(detectField)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    /**
     * 插入语句
     */
    public static class InsertSQL extends BaseSQL {

        public InsertSQL(String table, Object model, boolean underlineField, String... ignoredFields) {
            table = null == table ? DBUtils.getDBTableName(model.getClass(), true) : table;
            StringBuilder builder = new StringBuilder("INSERT INTO `" + table + "`(");
            StringBuilder valuesBuilder = new StringBuilder("VALUES(");
            List<Object> params = new ArrayList<>();
            List<Field> fields = getDeclaredFields(model.getClass(), null);
            for (Field field : fields) {
                if (!isInclude(ignoredFields, field.getName())) {
                    String dbFieldName = getDBFieldName(field.getName(), underlineField);
                    builder.append("`").append(dbFieldName).append("`,");
                    valuesBuilder.append("?,");
                    params.add(getFieldValue(model, field));
                }
            }
            this.sql = builder.toString().replaceFirst(",$", ") ") + valuesBuilder.toString().replaceAll(",$", ")");
            this.params = params;
        }

        public InsertSQL(Object model, boolean underlineField, String... ignoredFields) {
            this(DBUtils.getDBTableName(model.getClass(), true), model, underlineField, ignoredFields);
        }
    }

    /**
     * 更新一个对象的模型
     */
    public static class UpdateModelSQL extends BaseSQL {

        public UpdateModelSQL(String table, Object model, boolean underlineField, String[] ignoredFields, String[] primaryKeys) {
            table = null == table ? DBUtils.getDBTableName(model.getClass(), true) : table;
            primaryKeys = primaryKeys == null || primaryKeys.length < 1 ? new String[]{"id"} : primaryKeys;
            StringBuilder setBuilder = new StringBuilder("UPDATE `" + table + "` SET ");
            StringBuilder whereBuilder = new StringBuilder(" WHERE ");
            List<Object> params = new ArrayList<>();
            List<Object> whereParams = new ArrayList<>();
            List<Field> fields = getDeclaredFields(model.getClass(), null);
            for (Field field : fields) {
                String dbFieldName = getDBFieldName(field.getName(), underlineField);
                Object fieldValue = getFieldValue(model, field);
                if (isInclude(primaryKeys, field.getName())) {
                    // 构造WHERE
                    whereBuilder.append("`").append(dbFieldName).append("`=? AND ");
                    whereParams.add(fieldValue);
                } else {
                    if (!isInclude(ignoredFields, field.getName())) {
                        setBuilder.append("`").append(dbFieldName).append("`=?,");
                        params.add(fieldValue);
                    }
                }
            }
            this.sql = setBuilder.toString().replaceFirst(",$", "") + whereBuilder.toString().replaceAll(" AND $", "");
            if (!whereParams.isEmpty()) {
                params.addAll(whereParams);
            }
            this.params = params;
        }

        public UpdateModelSQL(String table, Object model, String[] primaryKeys) {
            this(table, model, true, null, primaryKeys);
        }

        public UpdateModelSQL(String table, Object model, String[] ignoredFields, String[] primaryKeys) {
            this(table, model, true, ignoredFields, primaryKeys);
        }

        public UpdateModelSQL(Object model, boolean underlineField, String[] primaryKeys) {
            this(DBUtils.getDBTableName(model.getClass(), true), model, underlineField, null, primaryKeys);
        }

        public UpdateModelSQL(Object model, boolean underlineField, String[] ignoredFields, String[] primaryKeys) {
            this(DBUtils.getDBTableName(model.getClass(), true), model, underlineField, ignoredFields, primaryKeys);
        }

        public UpdateModelSQL(Object model, String[] ignoredFields, String... primaryKeys) {
            this(DBUtils.getDBTableName(model.getClass(), true), model, true, ignoredFields, primaryKeys);
        }
    }

    public interface ModelConverter<T> {

        T convert(ResultSet rs);
    }

    public static class SQL extends AbstractSQL<SQL> {

        @Override
        public SQL getSelf() {
            return this;
        }
    }

    private static abstract class AbstractSQL<T> {
        private static final String AND = ") \nAND (";
        private static final String OR = ") \nOR (";

        private SQLStatement sql = new SQLStatement();

        public abstract T getSelf();

        public T UPDATE(String table) {
            sql().statementType = SQLStatement.StatementType.UPDATE;
            sql().tables.add(table);
            return getSelf();
        }

        public T SET(String sets) {
            sql().sets.add(sets);
            return getSelf();
        }

        public T INSERT_INTO(String tableName) {
            sql().statementType = SQLStatement.StatementType.INSERT;
            sql().tables.add(tableName);
            return getSelf();
        }

        public T VALUES(String columns, String values) {
            sql().columns.add(columns);
            sql().values.add(values);
            return getSelf();
        }

        public T SELECT(String columns) {
            sql().statementType = SQLStatement.StatementType.SELECT;
            sql().select.add(columns);
            return getSelf();
        }

        public T SELECT_DISTINCT(String columns) {
            sql().distinct = true;
            SELECT(columns);
            return getSelf();
        }

        public T DELETE_FROM(String table) {
            sql().statementType = SQLStatement.StatementType.DELETE;
            sql().tables.add(table);
            return getSelf();
        }

        public T FROM(String table) {
            sql().tables.add(table);
            return getSelf();
        }

        public T JOIN(String join) {
            sql().join.add(join);
            return getSelf();
        }

        public T INNER_JOIN(String join) {
            sql().innerJoin.add(join);
            return getSelf();
        }

        public T LEFT_OUTER_JOIN(String join) {
            sql().leftOuterJoin.add(join);
            return getSelf();
        }

        public T RIGHT_OUTER_JOIN(String join) {
            sql().rightOuterJoin.add(join);
            return getSelf();
        }

        public T OUTER_JOIN(String join) {
            sql().outerJoin.add(join);
            return getSelf();
        }

        public T WHERE(String conditions) {
            sql().where.add(conditions);
            sql().lastList = sql().where;
            return getSelf();
        }

        public T OR() {
            sql().lastList.add(OR);
            return getSelf();
        }

        public T AND() {
            sql().lastList.add(AND);
            return getSelf();
        }

        public T GROUP_BY(String columns) {
            sql().groupBy.add(columns);
            return getSelf();
        }

        public T HAVING(String conditions) {
            sql().having.add(conditions);
            sql().lastList = sql().having;
            return getSelf();
        }

        public T ORDER_BY(String columns) {
            sql().orderBy.add(columns);
            return getSelf();
        }

        private SQLStatement sql() {
            return sql;
        }

        public <A extends Appendable> A usingAppender(A a) {
            sql().sql(a);
            return a;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sql().sql(sb);
            return sb.toString();
        }

        private static class SafeAppendable {
            private final Appendable a;
            private boolean empty = true;

            public SafeAppendable(Appendable a) {
                super();
                this.a = a;
            }

            public SafeAppendable append(CharSequence s) {
                try {
                    if (empty && s.length() > 0) {
                        empty = false;
                    }
                    a.append(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return this;
            }

            public boolean isEmpty() {
                return empty;
            }

        }

        private static class SQLStatement {

            public enum StatementType {
                DELETE, INSERT, SELECT, UPDATE
            }

            StatementType statementType;
            List<String> sets = new ArrayList<String>();
            List<String> select = new ArrayList<String>();
            List<String> tables = new ArrayList<String>();
            List<String> join = new ArrayList<String>();
            List<String> innerJoin = new ArrayList<String>();
            List<String> outerJoin = new ArrayList<String>();
            List<String> leftOuterJoin = new ArrayList<String>();
            List<String> rightOuterJoin = new ArrayList<String>();
            List<String> where = new ArrayList<String>();
            List<String> having = new ArrayList<String>();
            List<String> groupBy = new ArrayList<String>();
            List<String> orderBy = new ArrayList<String>();
            List<String> lastList = new ArrayList<String>();
            List<String> columns = new ArrayList<String>();
            List<String> values = new ArrayList<String>();
            boolean distinct;

            public SQLStatement() {
                // Prevent Synthetic Access
            }

            private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close,
                                   String conjunction) {
                if (!parts.isEmpty()) {
                    if (!builder.isEmpty()) {
                        builder.append("\n");
                    }
                    builder.append(keyword);
                    builder.append(" ");
                    builder.append(open);
                    String last = "________";
                    for (int i = 0, n = parts.size(); i < n; i++) {
                        String part = parts.get(i);
                        if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND) && !last.equals(OR)) {
                            builder.append(conjunction);
                        }
                        builder.append(part);
                        last = part;
                    }
                    builder.append(close);
                }
            }

            private String selectSQL(SafeAppendable builder) {
                if (distinct) {
                    sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
                } else {
                    sqlClause(builder, "SELECT", select, "", "", ", ");
                }

                sqlClause(builder, "FROM", tables, "", "", ", ");
                sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
                sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
                sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
                sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
                sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
                sqlClause(builder, "WHERE", where, "(", ")", " AND ");
                sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
                sqlClause(builder, "HAVING", having, "(", ")", " AND ");
                sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
                return builder.toString();
            }

            private String insertSQL(SafeAppendable builder) {
                sqlClause(builder, "INSERT INTO", tables, "", "", "");
                sqlClause(builder, "", columns, "(", ")", ", ");
                sqlClause(builder, "VALUES", values, "(", ")", ", ");
                return builder.toString();
            }

            private String deleteSQL(SafeAppendable builder) {
                sqlClause(builder, "DELETE FROM", tables, "", "", "");
                sqlClause(builder, "WHERE", where, "(", ")", " AND ");
                return builder.toString();
            }

            private String updateSQL(SafeAppendable builder) {

                sqlClause(builder, "UPDATE", tables, "", "", "");
                sqlClause(builder, "SET", sets, "", "", ", ");
                sqlClause(builder, "WHERE", where, "(", ")", " AND ");
                return builder.toString();
            }

            public String sql(Appendable a) {
                SafeAppendable builder = new SafeAppendable(a);
                if (statementType == null) {
                    return null;
                }

                String answer;

                switch (statementType) {
                    case DELETE:
                        answer = deleteSQL(builder);
                        break;

                    case INSERT:
                        answer = insertSQL(builder);
                        break;

                    case SELECT:
                        answer = selectSQL(builder);
                        break;

                    case UPDATE:
                        answer = updateSQL(builder);
                        break;

                    default:
                        answer = null;
                }

                return answer;
            }
        }
    }
}
