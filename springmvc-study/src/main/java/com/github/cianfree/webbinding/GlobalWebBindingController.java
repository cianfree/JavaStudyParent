package com.github.cianfree.webbinding;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Arvin
 * @time 2016/11/15 18:21
 */
@Controller
@RequestMapping("/global/webbinding/")
public class GlobalWebBindingController {

    public GlobalWebBindingController() {
        System.out.println("init GlobalWebBindingController");
    }

    @ResponseBody
    @RequestMapping("addItem")
    public Item addItem(Item item) {
        System.out.println("item: " + item);
        return item;
    }
}
