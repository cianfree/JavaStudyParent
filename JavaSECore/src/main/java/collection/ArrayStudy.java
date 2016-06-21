package collection;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Arvin on 2016/4/17.
 */
public class ArrayStudy {

    public static void main(String[] args) {
        Element[] array = new Element[10];

        array[0] = new Element(1, "1");

        System.out.println(array);
        System.out.println(JSON.toJSONString(array));
        System.out.println("Length: " + array.length);

        Element[] clone = array.clone();
        System.out.println("克隆对象的地址： " + clone);


        ArrayList<Element> list = new ArrayList<>(Arrays.asList(array));
        System.out.println(JSON.toJSONString(list));

        System.out.println(list.size());

    }
}
