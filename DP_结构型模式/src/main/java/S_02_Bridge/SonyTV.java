package S_02_Bridge;

/**
 * Created by Arvin on 2016/4/26.
 */
public class SonyTV implements ITV {

    @Override
    public void on() {
        System.out.println("Sony is turned on.");
    }

    @Override
    public void off() {
        System.out.println("Sony is turned off.");
    }

    @Override
    public void switchChannel(int channel) {
        System.out.println("Sony: channel - " + channel);
    }
}