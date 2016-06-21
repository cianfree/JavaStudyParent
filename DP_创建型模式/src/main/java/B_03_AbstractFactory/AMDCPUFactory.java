package B_03_AbstractFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public class AMDCPUFactory implements AbstractCUPFactory {
    @Override
    public CPU produceCPU() {
        return new AMDCPU();
    }
}
