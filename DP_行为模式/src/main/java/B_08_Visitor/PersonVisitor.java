package B_08_Visitor;

/**
 * 人去访问
 * Created by Arvin on 2016/4/27.
 */
public class PersonVisitor implements Visitor {
    @Override
    public void visit(City city) {
        System.out.println("人来到了城市");
    }

    @Override
    public void visit(Museum museum) {
        System.out.println("人去博物馆参观");
    }

    @Override
    public void visit(Park park) {
        System.out.println("人去公园游玩");
    }
}
