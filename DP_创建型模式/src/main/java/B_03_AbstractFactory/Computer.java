package B_03_AbstractFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Computer {

    private CPU cpu;

    public Computer(AbstractCUPFactory factory) {
        this.cpu = factory.produceCPU();
    }

    public void process() {
        this.cpu.process();
    }
}
