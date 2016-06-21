package B_09_ChainOfResponsibility;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        Chain chain = createChain();

        Context context = new Context(1);
        chain.process(context);

        context.setType(3);
        chain.process(context);
    }

    /**
     * 构造好责任链
     *
     * @return
     */
    private static Chain createChain() {
        Chain type1 = new ChainForType1();
        Chain type2 = new ChainForType2();
        Chain type3 = new ChainForType3();
        type1.setNext(type2);
        type2.setNext(type3);
        return type1;
    }
}
