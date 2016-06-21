package B_03_State;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Rich implements State {
    @Override
    public void saySomething(StateContext sc) {
        System.out.println("I'm poor currently, and spend much time working.");
        sc.changeState(new Poor());
    }
}
