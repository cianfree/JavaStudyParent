package B_03_State;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Poor implements State {

    @Override
    public void saySomething(StateContext sc) {
        System.out.println("I'm rich currently, and play a lot.");
        sc.changeState(new Rich());
    }
}
