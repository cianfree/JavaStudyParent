package S_03_Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Composite implements Component {

    private List<Component> childComponents = new ArrayList<>();

    public void add(Component component) {
        childComponents.add(component);
    }

    public void remove(Component component) {
        childComponents.remove(component);
    }

    @Override
    public void show() {
        for (Component component : childComponents) {
            component.show();
        }
    }
}
