package B_09_ChainOfResponsibility;

/**
 * Created by Arvin on 2016/4/27.
 */
public abstract class Chain {

    protected Chain next;

    public Chain getNext() {
        return next;
    }

    public void setNext(Chain next) {
        this.next = next;
    }

    /**
     * 处理指定类型的消息
     *
     * @param context
     */
    public abstract void process(Context context);
}
