package B_01_TemplateMethod;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Car extends Vehicle {
    @Override
    public void start() {
        this.isReady = true;
    }

    @Override
    public void run() {
        System.out.println("Car is running!");
    }

    @Override
    public void stop() {
        System.out.println("Car is stopped!");
    }
}
