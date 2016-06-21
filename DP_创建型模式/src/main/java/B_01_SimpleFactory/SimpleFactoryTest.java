package B_01_SimpleFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class SimpleFactoryTest {

    public static void main(String[] args) {
        Human boy = HumanFactory.createHuman(HumanType.BOY);

        boy.talk();
        boy.walk();

        Human girl = HumanFactory.createHuman(HumanType.GIRL);

        girl.talk();
        girl.walk();
    }
}
