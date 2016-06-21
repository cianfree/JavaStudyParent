package B_01_TemplateMethod;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        Car car = new Car();
        testVehicle(car);

        Truck truck = new Truck();
        testVehicle(truck);
    }

    public static void testVehicle(Vehicle v) {
        v.testVehicle();
    }
}