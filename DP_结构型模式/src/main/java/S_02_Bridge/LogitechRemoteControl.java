package S_02_Bridge;

/**
 * Created by Arvin on 2016/4/26.
 */
public class LogitechRemoteControl extends AbstractRemoteControl {

    public LogitechRemoteControl(ITV tv) {
        super(tv);
    }

    public void setChannelKeyboard(int channel) {
        setChannel(channel);
        System.out.println("Logitech use keyword to set channel.");
    }
}
