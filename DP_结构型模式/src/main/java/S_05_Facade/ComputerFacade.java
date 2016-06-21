package S_05_Facade;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ComputerFacade {

    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hardDrive = new HardDrive();

    public void run() {
        cpu.process();
        memory.alloc();
        hardDrive.readData();
    }
}
