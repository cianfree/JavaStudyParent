package B_08_Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvin on 2016/4/27.
 */
public class City implements Element {

    List<Element> places = new ArrayList<>();

    public City() {
        places.add(new Museum());
        places.add(new Park());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        if (!places.isEmpty()) {
            for (Element place : places) {
                place.accept(visitor);
            }
        }
    }
}
