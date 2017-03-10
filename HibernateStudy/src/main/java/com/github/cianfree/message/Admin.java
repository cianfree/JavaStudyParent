package com.github.cianfree.message;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 注解方式
 *
 * @author Arvin
 * @time 2017/2/14 16:51
 */
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    public Admin() {
    }

    public Admin(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
