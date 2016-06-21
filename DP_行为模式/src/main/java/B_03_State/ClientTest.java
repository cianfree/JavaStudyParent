package B_03_State;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String args[]) {
        StateContext sc = new StateContext();
        sc.saySomething();
        sc.saySomething();
        sc.saySomething();
        sc.saySomething();
    }
}
