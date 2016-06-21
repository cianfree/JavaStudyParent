package B_07_Command;

/**
 * Created by Arvin on 2016/4/27.
 */
public class StartupCommand implements Command {

    private Computer receiver;

    public StartupCommand(Computer computer) {
        this.receiver = computer;
    }

    @Override
    public void execute() {
        this.receiver.startup();
    }
}
