package B_03_AbstractFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class IntelCPU implements CPU {
    @Override
    public void process() {
        System.out.println("Process by intel CPU!");
    }
}
