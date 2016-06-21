package B_01_TemplateMethod;

/**
 * 交通工具
 * Created by Arvin on 2016/4/26.
 */
public abstract class Vehicle {

    protected boolean isReady;

    public abstract void start();

    public abstract void run();

    public abstract void stop();

    /**
     * 公共逻辑的封装
     */
    public void testVehicle() {
        start();
        if (isReady) {
            run();
            stop();
        }
    }

}
