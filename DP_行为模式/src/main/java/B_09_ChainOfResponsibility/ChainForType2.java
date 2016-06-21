package B_09_ChainOfResponsibility;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ChainForType2 extends Chain {
    @Override
    public void process(Context context) {
        if (context.getType() == 2) {
            System.out.println("Handle By ChainForType2");
        } else if (null != next) {
            next.process(context);
        }
    }
}
