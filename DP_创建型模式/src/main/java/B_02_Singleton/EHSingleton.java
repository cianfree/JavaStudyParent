package B_02_Singleton;

/**
 * 饿汉式
 * Created by Arvin on 2016/4/26.
 */
public class EHSingleton {

    private static final EHSingleton INSTANCE = new EHSingleton();

    private EHSingleton() {
        synchronized (EHSingleton.class) {
            if (null != INSTANCE) {
                throw new RuntimeException("Can't create another instance");
            }
        }
        System.out.println("Create EHSingleton Instance!");
    }

    public static EHSingleton getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        EHSingleton instance1 = EHSingleton.getInstance();
        EHSingleton instance2 = EHSingleton.getInstance();

        System.out.println(instance1 == instance2);
    }

}
