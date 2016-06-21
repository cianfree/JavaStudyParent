package S_03_Composite;

/**
 * Created by Arvin on 2016/4/26.
 */
public class Leaf implements Component {

    private String name;

    public Leaf(String name) {
        this.name = name;
    }

    @Override
    public void show() {
        System.out.println(this.name);
    }
}
