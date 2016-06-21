package B_07_Command;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ShutdownCommand implements Command {

    private Computer receiver;

    public ShutdownCommand(Computer computer) {
        this.receiver = computer;
    }

    @Override
    public void execute() {
        this.receiver.shutdown();
    }
}
