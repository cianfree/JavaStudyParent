package B_08_Visitor;

/**
 * 猴子去访问
 * Created by Arvin on 2016/4/27.
 */
public class MonkeyVisitor implements Visitor {
    @Override
    public void visit(City city) {
        System.out.println("猴子来到了城市");
    }

    @Override
    public void visit(Museum museum) {
        System.out.println("猴子走丢在博物馆");
    }

    @Override
    public void visit(Park park) {
        System.out.println("猴子被送到公园");
    }
}
