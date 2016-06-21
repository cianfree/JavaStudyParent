package B_01_SimpleFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class HumanFactory {

    /**
     * 根据类型创建 Human
     *
     * @param type
     * @return
     */
    public static Human createHuman(HumanType type) {
        switch (type) {
            case BOY:
                return new Boy();
            case GIRL:
                return new Girl();
        }
        throw new RuntimeException("NO specific type human implements!");
    }
}
