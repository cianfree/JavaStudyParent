package B_10_Iterator;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        StringContainer container = new StringContainer();

        Iterator<String> iterator = container.iterator();

        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
