package B_06_Mediator;

/**
 * 班长
 * Created by Arvin on 2016/4/27.
 */
public class Monitor extends Colleague {

    public Monitor(String name) {
        super(name);
    }

    @Override
    public void talk() {
        System.out.println("班长[" + getName() + "]说：" + this.getContent());
    }
}
