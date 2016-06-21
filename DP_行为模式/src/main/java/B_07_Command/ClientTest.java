package B_07_Command;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        Computer computer = new Computer();
        ShutdownCommand shutdownCommand = new ShutdownCommand(computer);
        StartupCommand startupCommand = new StartupCommand(computer);

        // 客户端发起了开机请求
        startupCommand.execute();

        // 客户端发起了关系请求
        shutdownCommand.execute();
    }
}
