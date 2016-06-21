package B_05_Prototype;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {
    public static void main(String args[]) throws CloneNotSupportedException {
        A a = new A(1);

        for (int i = 2; i < 10; i++) {
            Prototype temp = a.clone();
            System.out.println(a == temp);
            temp.setSize(i);
            temp.printSize();
        }
    }
}