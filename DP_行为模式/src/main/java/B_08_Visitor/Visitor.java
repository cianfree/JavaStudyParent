package B_08_Visitor;

/**
 * 定义访问者接口
 * Created by Arvin on 2016/4/27.
 */
public interface Visitor {

    void visit(City city);

    void visit(Museum museum);

    void visit(Park park);
}
