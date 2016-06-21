package B_02_Strategy;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        NicePolice nicePolice = new NicePolice();
        HardPolice hardPolice = new HardPolice();

        Situation situation1 = new Situation(nicePolice);
        situation1.handleByPolice(100);

        Situation situation2 = new Situation(hardPolice);
        situation2.handleByPolice(100);
    }
}
