package com.github.search.simple.model;

import com.github.search.simple.utils.JsonUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 缓存元素
 *
 * @author Arvin
 * @time 2016/12/27 17:34
 */
public class Element {

    public static final Comparator<Element> defaultComparator = new Comparator<Element>() {
        @Override
        public int compare(Element o1, Element o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    /** 唯一标识符 */
    private String id;

    /** 元素集合 */
    private Set<Field> fs = new HashSet<>();

    public Element() {
    }

    public Element(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Field> getFs() {
        return fs;
    }

    public void setFs(Set<Field> fs) {
        this.fs = fs;
    }

    /**
     * 添加要索引的属性
     *
     * @param field 属性
     */
    public Element add(String field, String value, boolean needIndex) {
        this.fs.add(new Field(field, value, needIndex));
        return this;
    }

    public Element add(Field field) {
        this.fs.add(field);
        return this;
    }

    public Field getField(String fieldName) {
        if (fs != null && !fs.isEmpty()) {
            for (Field field : fs) {
                if (field.getN().equals(fieldName)) {
                    return field;
                }
            }
        }
        return null;
    }

    public String getString(String fieldName) {
        Field field = getField(fieldName);
        return null == field ? null : field.getV();
    }

    public Integer getInt(String fieldName) {
        Field field = getField(fieldName);
        return null == field ? null : Integer.parseInt(field.getV());
    }

    public Long getLong(String fieldName) {
        Field field = getField(fieldName);
        return null == field ? null : Long.parseLong(field.getV());
    }

    public Date getTime(String fieldName) {
        Field field = getField(fieldName);
        return null == field ? null : new Date(Long.parseLong(field.getV()));
    }

    /**
     * 转换成字符串
     *
     * @param element 要转换的元素
     */
    public static String toString(Element element) {
        //return Json.toJson(element).replaceAll("\"([a-zA-Z0-9_]+)\":", "$1:");
        return JsonUtils.toJson(element);
    }

    public static Element fromString(String elementString) {
        //return Json.toObject(elementString.replaceAll("([a-zA-Z0-9_]+):", "\"$1\":"), Element.class);
        return JsonUtils.toObject(elementString, Element.class);
    }

    public static void main(String[] args) {
        Element element = new Element("1");
        element.add("name", "Arvin", true);
        element.add("age", "22", true);

        String json = Element.toString(element);
        System.out.println(json);

        Element e = Element.fromString(json);
        System.out.println(JsonUtils.toJson(e));
    }
}
