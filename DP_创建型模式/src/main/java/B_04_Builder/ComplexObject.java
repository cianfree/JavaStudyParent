package B_04_Builder;

/**
 * <pre>
 * 复杂的对象，构建过程比较复杂，比如你可能之前通过很多的构造函数实现，但是使用建造者模式，参数会少很多
 *
 * </pre>
 * Created by Arvin on 2016/4/26.
 */
public class ComplexObject {

    private final int partA;
    private int partB;

    private String partC;
    private Integer partD;

    public ComplexObject(int partA, int partB, String partC, Integer partD) {
        this.partA = partA;
        this.partB = partB;
        this.partC = partC;
        this.partD = partD;
    }

    public int getPartA() {
        return partA;
    }

    public int getPartB() {
        return partB;
    }

    public void setPartB(int partB) {
        this.partB = partB;
    }

    public String getPartC() {
        return partC;
    }

    public void setPartC(String partC) {
        this.partC = partC;
    }

    public Integer getPartD() {
        return partD;
    }

    public void setPartD(Integer partD) {
        this.partD = partD;
    }
}
