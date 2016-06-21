package edu.zhku.redis.test.spring;

import java.io.Serializable;

/**
 * 消息
 */
public class Msg implements Serializable {

    private String content;

    public Msg(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "content='" + content + '\'' +
                '}';
    }
}
