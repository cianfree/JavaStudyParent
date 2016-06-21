package com.bxtpw.common.cache.listener;

import com.bxtpw.common.cache.CacheProvider;
import com.bxtpw.common.cache.annotation.VisitorCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 处理缓存更新操作
 *
 * @author 黎杰
 * @version 0.1
 * @time 2015年6月9日 下午5:09:56
 * @since 0.1
 */
public class CacheJobProcess implements ApplicationContextAware, Runnable {

    private static final Logger LOGGER = LogManager.getLogger(CacheJobProcess.class);

    private ApplicationContext context;

    private List<String> tableNames = new ArrayList<String>();

    private List<String> tableColumns = new ArrayList<String>();

    private List<String> tableIds = new ArrayList<String>();

    private CacheProvider cacheProvider;

    private DataSource dataSource;

    private Map<String, Boolean> includedMap = new HashMap<String, Boolean>();

    private String cronExpression;

    public CacheJobProcess() {
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        processAnnotation();
        doCacheJob();
    }

    /**
     * 任务调度
     *
     * @author 黎杰
     * @time 2015年6月9日 下午3:28:23
     * @version 0.1
     * @since 0.1
     */
    private void doCacheJob() {
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.initialize();
        s.schedule(this, new CronTrigger(cronExpression));
    }

    /**
     * 读取注解
     *
     * @author 黎杰
     * @time 2015年6月9日 下午3:28:38
     * @version 0.1
     * @since 0.1
     */
    @SuppressWarnings("rawtypes")
    private void processAnnotation() {
        Map<String, Object> beansMap = context.getBeansOfType(Object.class);
        if (null == beansMap || beansMap.isEmpty()) {
            return;
        }
        Iterator<String> beanNameIterator = beansMap.keySet().iterator();
        while (beanNameIterator.hasNext()) {
            Class tagetClass = beansMap.get(beanNameIterator.next()).getClass();
            ReflectionUtils.doWithMethods(tagetClass, new MethodCallback() {
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    VisitorCache visitorCache = method.getAnnotation(VisitorCache.class);
                    if (null != visitorCache) {
                        String tableName = visitorCache.tableName();
                        String tableColumn = visitorCache.tableColumn();
                        String idName = visitorCache.idName();
                        String key = tableName + File.separator + tableColumn + File.separator + idName;
                        if (!includedMap.containsKey(key)) {
                            tableNames.add(tableName);
                            tableColumns.add(tableColumn);
                            tableIds.add(idName);
                        }
                    }
                }
            });
        }
    }

    private String createVisitorSql(TableInfo tableInfo, Map<String, Integer> visitorCacheMap) {
        StringBuilder sb = new StringBuilder();
        String tableName = tableInfo.getTableName();
        String tableColum = tableInfo.getTableColumn();
        String tableId = tableInfo.getTableId();
        sb.append("update  ").append(tableName).append(" SET   ").append(tableColum).append("=case ").append(tableId);
        Set<String> keys = visitorCacheMap.keySet();
        for (String key : keys) {
            sb.append(" when ").append("'").append(key).append("'").append(" then    IF(").append(tableColum).append(",").append(tableColum).append("+").append(visitorCacheMap.get(key)).append(",")
                    .append(visitorCacheMap.get(key)).append(")");
        }
        sb.append(" end  where ").append(tableId).append(" in (");
        Iterator<String> idIterable = keys.iterator();
        while (idIterable.hasNext()) {
            sb.append("'").append(idIterable.next()).append("'").append(",");
        }
        int startCharIndex = sb.lastIndexOf(",");
        sb.delete(startCharIndex, startCharIndex + 1);
        sb.append(")");
        return sb.toString();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 表操作辅助类
     *
     * @author 黎杰
     * @version 0.1
     * @time 2015年6月8日 下午4:47:29
     * @since 0.1
     */
    class TableInfo {
        private String tableName;
        private String tableColumn;
        private String tableId;

        private TableInfo(String tableName, String tableColumn, String tableId) {
            this.tableName = tableName;
            this.tableColumn = tableColumn;
            this.tableId = tableId;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getTableColumn() {
            return tableColumn;
        }

        public void setTableColumn(String tableColumn) {
            this.tableColumn = tableColumn;
        }

        public String getTableId() {
            return tableId;
        }

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }
    }

    public void run() {
        Connection connection = null;
        Statement st = null;
        try {
            if ((tableNames.size() != tableColumns.size()) && (tableColumns.size() != tableIds.size())) {
                return;
            }
            for (int i = 0; i < tableNames.size(); i++) {
                Map<String, Integer> visitorCacheMap = cacheProvider.readCache(tableNames.get(i), tableColumns.get(i), Map.class);
                if (null != visitorCacheMap && !visitorCacheMap.isEmpty()) {
                    String sql = createVisitorSql(new TableInfo(tableNames.get(i), tableColumns.get(i), tableIds.get(i)), visitorCacheMap);
                    connection = dataSource.getConnection();
                    st = connection.createStatement();
                    st.execute(sql);
                    connection.close();
                    cacheProvider.removeCache(tableNames.get(i), tableColumns.get(i));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            if (null != st) {
                try {
                    st.close();
                } catch (SQLException e) {
                    LOGGER.error(e);
                } finally {
                    if (null != connection) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            LOGGER.error(e);
                        }
                    }
                }
            }
        }

    }
}
