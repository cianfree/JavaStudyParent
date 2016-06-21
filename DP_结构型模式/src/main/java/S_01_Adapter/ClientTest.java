package S_01_Adapter;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        MyService service = new MyServiceCompAdapter();

        service.sayHi("Arvin");
    }
}
