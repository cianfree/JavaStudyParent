package B_05_Prototype;

/**
 * Created by Arvin on 2016/4/26.
 */
public class A implements Prototype, Cloneable {

    private Integer size;

    public A(Integer size) {
        this.size = size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void printSize() {
        System.out.println("Size ： " + size);
    }

    @Override
    public A clone() throws CloneNotSupportedException {
        return (A) super.clone();
    }
}
