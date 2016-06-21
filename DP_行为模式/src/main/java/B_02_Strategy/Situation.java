package B_02_Strategy;

/**
 * 处境
 * Created by Arvin on 2016/4/26.
 */
public class Situation {

    private OverSpeedStrategy strategy;

    public Situation(OverSpeedStrategy strategy) {
        this.strategy = strategy;
    }

    public void handleByPolice(int speed) {
        this.strategy.process(speed);
    }
}
