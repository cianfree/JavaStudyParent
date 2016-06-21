package B_08_Visitor;

/**
 * Created by Arvin on 2016/4/27.
 */
public class Museum implements Element {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
