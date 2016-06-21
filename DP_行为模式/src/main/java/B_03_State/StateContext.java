package B_03_State;

/**
 * Created by Arvin on 2016/4/26.
 */
public class StateContext {

    private State currentState;

    public StateContext() {
        currentState = new Poor();
    }

    public void changeState(State newState) {
        this.currentState = newState;
    }

    public void saySomething() {
        this.currentState.saySomething(this);
    }
}
