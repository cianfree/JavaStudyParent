package S_01_Adapter;

/**
 * 组合模式实现
 * Created by Arvin on 2016/4/26.
 */
public class MyServiceCompAdapter implements MyService {

    private SunService sunService = new SunService();

    @Override
    public void sayHi(String name) {
        sunService.sayHi(name);
    }
}
