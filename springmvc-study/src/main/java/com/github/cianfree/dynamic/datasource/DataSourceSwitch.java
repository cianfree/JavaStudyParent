package com.github.cianfree.dynamic.datasource;

/**
 * 切换当前线程的DataSource
 *
 * @author Arvin
 * @time 2016/11/16 11:30
 */
public class DataSourceSwitch {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

    /**
     * @param dataSourceType 数据库类型
     * @return void
     */
    public static void switchDataSource(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    /**
     * @return String
     */
    public static DataSourceType getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * @return void
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
