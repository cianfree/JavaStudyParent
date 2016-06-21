package B_11_Interpreter;

/**
 * 要解释的上下文, 这里简单的就是一个字符串
 * Created by Arvin on 2016/4/27.
 */
public class Context {

    private String input;

    public Context() {
    }

    public Context(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
