package B_11_Interpreter;

/**
 * 解释为say hi
 * Created by Arvin on 2016/4/27.
 */
public class SayHiExplain implements Explain {

    @Override
    public String explain(Context context) {
        return "Hi, " + context.getInput();
    }
}
