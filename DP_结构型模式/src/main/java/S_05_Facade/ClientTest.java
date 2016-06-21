package S_05_Facade;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        // 没有使用外观模式的情况下，我们启动一个电脑需要记住多个类的接口，并且要安排好顺序
        startComputerWithoutFacade();

        // 使用外观模式启动一个电脑
        ComputerFacade computerFacade = new ComputerFacade();
        computerFacade.run();
    }

    /**
     * 没有使用外观模式的情况下，我们启动一个电脑需要记住多个类的接口，并且要安排好顺序
     */
    private static void startComputerWithoutFacade() {
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hardDrive = new HardDrive();

        cpu.process();
        memory.alloc();
        hardDrive.readData();
    }
}
