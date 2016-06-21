package S_02_Bridge;

/**
 * Created by Arvin on 2016/4/26.
 */
public abstract class AbstractRemoteControl {
    /**
     * @uml.property name="tv"
     * @uml.associationEnd
     */
    private ITV tv;

    public AbstractRemoteControl(ITV tv) {
        this.tv = tv;
    }

    public void turnOn() {
        tv.on();
    }

    public void turnOff() {
        tv.off();
    }

    public void setChannel(int channel) {
        tv.switchChannel(channel);
    }
}