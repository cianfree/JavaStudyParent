package B_09_ChainOfResponsibility;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ChainForType1 extends Chain {
    @Override
    public void process(Context context) {
        if (context.getType() == 1) {
            System.out.println("Handle By ChainForType1");
        } else if (null != next) {
            next.process(context);
        }
    }
}
