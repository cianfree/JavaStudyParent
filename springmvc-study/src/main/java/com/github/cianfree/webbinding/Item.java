package com.github.cianfree.webbinding;

/**
 * @author Arvin
 * @time 2016/11/15 18:17
 */
public class Item {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
