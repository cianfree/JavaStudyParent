package B_03_AbstractFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {

        AbstractCUPFactory factory = createFactory(FactoryList.AMD);
        Computer computer = new Computer(factory);

        computer.process();
        
    }

    private static AbstractCUPFactory createFactory(FactoryList factoryType) {
        switch (factoryType) {
            case INTEL:
                return new IntelCPUFactory();
            case AMD:
                return new AMDCPUFactory();
        }
        throw new RuntimeException("Unknown Factory type!");
    }
}
