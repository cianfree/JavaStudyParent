package B_07_Command;

/**
 * Computer 这里充当的是接收者的角色
 * Created by Arvin on 2016/4/27.
 */
public class Computer {

    public void shutdown() {
        System.out.println("Computer is shutdown!");
    }

    public void startup() {
        System.out.println("Computer is starting!");
    }
}
