package B_11_Interpreter;

/**
 * 定义解释器
 * Created by Arvin on 2016/4/27.
 */
public interface Explain {

    /**
     * 解释上下文，并输出结果
     *
     * @param context
     * @return
     */
    String explain(Context context);
}
