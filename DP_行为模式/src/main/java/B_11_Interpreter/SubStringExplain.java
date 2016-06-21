package B_11_Interpreter;

/**
 * 获取子串解释器
 * Created by Arvin on 2016/4/27.
 */
public class SubStringExplain implements Explain {

    /**
     * 长度
     */
    private int len;

    public SubStringExplain(int len) {
        this.len = len;
    }

    @Override
    public String explain(Context context) {
        String input = context.getInput();
        if (null == input || input.length() < len) {
            return input;
        }
        return input.substring(0, len);
    }
}
