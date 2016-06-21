package B_08_Visitor;

/**
 * 定义元素接口
 * Created by Arvin on 2016/4/27.
 */
public interface Element {

    void accept(Visitor visitor);
}
