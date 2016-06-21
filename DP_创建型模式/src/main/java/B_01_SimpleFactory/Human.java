package B_01_SimpleFactory;

/**
 * Created by Arvin on 2016/4/26.
 */
public interface Human {

    void talk();

    void walk();
}

class Boy implements Human {

    @Override
    public void talk() {
        System.out.println("Boy is talking......");
    }

    @Override
    public void walk() {
        System.out.println("Boy is walking......");
    }
}

class Girl implements Human {

    @Override
    public void talk() {
        System.out.println("Girl is talking......");
    }

    @Override
    public void walk() {
        System.out.println("Girl is walking......");
    }
}