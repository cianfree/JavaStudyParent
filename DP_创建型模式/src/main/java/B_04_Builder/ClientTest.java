package B_04_Builder;

import com.alibaba.fastjson.JSON;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        ComplexObjectBuilder builder = new ComplexObjectBuilder();

        ComplexObject object = builder
                .buildPartA(10)
                .buildPartB(20)
                .buildPartC("CCC")
                .buildPartD(100)
                .build();

        System.out.println(JSON.toJSONString(object));
    }
}
