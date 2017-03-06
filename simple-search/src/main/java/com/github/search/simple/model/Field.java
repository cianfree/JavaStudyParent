package com.github.search.simple.model;

/**
 * 索引项
 *
 * @author Arvin
 * @time 2016/12/27 17:35
 */
public class Field {

    /** 名称 name */
    private String n;
    /** 值 field */
    private String v;
    /** 是否需要索引 needIndex */
    private int ni;

    public Field() {
    }

    public Field(String name, String value, boolean needIndex) {
        this.n = name;
        this.v = value;
        this.ni = needIndex ? 1 : 0;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public int getNi() {
        return ni;
    }

    public void setNi(int ni) {
        this.ni = ni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (n != null ? !n.equals(field.n) : field.n != null) return false;
        return !(v != null ? !v.equals(field.v) : field.v != null);

    }

    @Override
    public int hashCode() {
        int result = n != null ? n.hashCode() : 0;
        result = 31 * result + (v != null ? v.hashCode() : 0);
        return result;
    }
}
