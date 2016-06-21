package S_01_Adapter;

/**
 * 这个是第三方的实现，但是没有实现MyService接口，因此我们需要构建适配器，建立一个桥梁
 * Created by Arvin on 2016/4/26.
 */
public class SunService {

    void sayHi(String name) {
        System.out.println("Hi, " + name);
    }
}
