package B_06_Mediator;

/**
 * 团支书
 * Created by Arvin on 2016/4/27.
 */
public class TuanZhiShu extends Colleague {

    public TuanZhiShu(String name) {
        super(name);
    }

    @Override
    public void talk() {
        System.out.println("团支书[" + getName() + "]说：" + this.getContent());
    }
}
