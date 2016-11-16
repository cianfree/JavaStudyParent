package com.github.cianfree.converter;

import com.github.cianfree.webbinding.Item;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Arvin
 * @time 2016/11/15 18:55
 */
public class ItemConverter implements Converter<String, Item> {
    @Override
    public Item convert(String s) {
        System.out.println("Custom convert item");
        Item item = new Item();
        if (s != null) {
            String[] arr = s.split(":");
            item.setKey(arr[0]);
            item.setValue(arr[1]);
        }
        return item;
    }
}
