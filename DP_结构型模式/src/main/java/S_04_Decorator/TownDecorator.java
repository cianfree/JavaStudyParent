package S_04_Decorator;

/**
 * Created by Arvin on 2016/4/26.
 */
public class TownDecorator extends Decorator {
    public TownDecorator(Agent target) {
        super(target);
    }

    @Override
    public String getName() {
        return super.getName() + ",Town";
    }
}
