package B_11_Interpreter;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        Context context = new Context("Arvin");

        Explain sayHiExplain = new SayHiExplain();
        Explain substringExplain = new SubStringExplain(5);

        System.out.println(sayHiExplain.explain(context));
        System.out.println(substringExplain.explain(context));
    }
}
