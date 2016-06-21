package B_04_Builder;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ComplexObjectBuilder {

    private int partA = 0;
    private int partB = 0;

    private String partC = "C";
    private Integer partD = 1;

    public ComplexObjectBuilder buildPartA(int partA) {
        this.partA = partA;
        return this;
    }

    public ComplexObjectBuilder buildPartB(int partB) {
        this.partB = partB;
        return this;
    }

    public ComplexObjectBuilder buildPartC(String partC) {
        this.partC = partC;
        return this;
    }

    public ComplexObjectBuilder buildPartD(Integer partD) {
        this.partD = partD;
        return this;
    }

    public ComplexObject build() {
        return new ComplexObject(partA, partB, partC, partD);
    }

}
