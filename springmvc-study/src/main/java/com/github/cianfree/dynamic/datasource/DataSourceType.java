package com.github.cianfree.dynamic.datasource;

/**
 * @author Arvin
 * @time 2016/11/16 11:31
 */
public enum DataSourceType {

    LOCAL("local"),
    TEST("test");

    private String key;

    DataSourceType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
