package B_02_Strategy;

/**
 * Created by Arvin on 2016/4/26.
 */
public class NicePolice implements OverSpeedStrategy {
    @Override
    public void process(int speed) {
        System.out.println("This is your first time, be sure don't do it again!");
    }
}
