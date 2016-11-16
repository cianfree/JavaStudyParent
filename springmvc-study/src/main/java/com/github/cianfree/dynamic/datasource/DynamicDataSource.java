package com.github.cianfree.dynamic.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Arvin
 * @time 2016/11/16 11:30
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("当前数据源： " + DataSourceSwitch.getDataSourceType());
        return DataSourceSwitch.getDataSourceType() == null ? null : DataSourceSwitch.getDataSourceType().getKey();
    }
}
