package edu.zhku.hessian.domain;

import java.io.Serializable;

/**
 * Created by Arvin on 2016/6/5.
 */
public class Role implements Serializable {

    private Integer id;

    private String name;

    public Role() {
    }

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
