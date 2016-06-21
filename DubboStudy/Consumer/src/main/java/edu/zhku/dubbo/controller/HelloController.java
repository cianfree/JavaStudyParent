package edu.zhku.dubbo.controller;

import edu.zhku.dubbo.domain.User;
import edu.zhku.dubbo.service.HelloService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Arvin on 2016/6/14.
 */
@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @RequestMapping("sayHi")
    public String sayHi(String name) {
        return helloService.sayHi(name);
    }

    @RequestMapping("get/{id}")
    public User get(@PathVariable("id") Integer id) {
        return helloService.get(id);
    }
}
