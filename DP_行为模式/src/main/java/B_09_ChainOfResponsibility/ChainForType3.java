package B_09_ChainOfResponsibility;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ChainForType3 extends Chain {
    @Override
    public void process(Context context) {
        if (context.getType() == 3) {
            System.out.println("Handle By ChainForType3");
        } else if (null != next) {
            next.process(context);
        }
    }
}
