package com.github.cianfree.autoinject.bytype;

/**
 * @author Arvin
 * @time 2016/11/16 10:24
 */
public class MyType {

    private String content;

    public MyType(String content) {
        this.content = content;
    }

    public MyType() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyType{" +
                "content='" + content + '\'' +
                '}';
    }
}
