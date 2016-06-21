package S_04_Decorator;

/**
 * 市级服务商
 * Created by Arvin on 2016/4/26.
 */
public class CityDecorator extends Decorator {
    public CityDecorator(Agent target) {
        super(target);

    }

    @Override
    public String getName() {
        return super.getName() + ",City";
    }
}
