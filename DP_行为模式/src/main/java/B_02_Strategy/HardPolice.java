package B_02_Strategy;

/**
 * Created by Arvin on 2016/4/26.
 */
public class HardPolice implements OverSpeedStrategy {
    @Override
    public void process(int speed) {
        System.out.println("Your speed is " + speed + ", and should get a ticket!");
    }
}
