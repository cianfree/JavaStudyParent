package B_09_ChainOfResponsibility;

/**
 * 上下文
 * Created by Arvin on 2016/4/27.
 */
public class Context {

    private int type;

    public Context(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
