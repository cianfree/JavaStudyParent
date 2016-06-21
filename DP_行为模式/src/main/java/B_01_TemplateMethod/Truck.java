package B_01_TemplateMethod;

/**
 * 卡车
 * Created by Arvin on 2016/4/26.
 */
public class Truck extends Vehicle {
    @Override
    public void start() {
        this.isReady = true;
    }

    @Override
    public void run() {
        System.out.println("Truck is running!");
    }

    @Override
    public void stop() {
        System.out.println("Truck is stopped!");
    }
}
