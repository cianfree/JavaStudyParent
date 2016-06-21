package S_02_Bridge;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {
    public static void main(String[] args) {
        ITV tv = new SonyTV();
        LogitechRemoteControl lrc = new LogitechRemoteControl(tv);
        lrc.setChannelKeyboard(100);
    }
}
