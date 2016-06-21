package S_01_Adapter;

/**
 * 自己提供给别人的接口
 * Created by Arvin on 2016/4/26.
 */
public interface MyService {

    /**
     * 这个是我们自己的接口定义，但是我引用了第三方的库来实现
     *
     * @param name
     */
    void sayHi(String name);
}
