package S_04_Decorator;

/**
 * Created by Arvin on 2016/4/26.
 */
public abstract class Decorator extends Agent {

    private Agent target;

    public Decorator(Agent target) {
        this.target = target;
    }

    @Override
    public String getName() {
        return target.getName();
    }
}
