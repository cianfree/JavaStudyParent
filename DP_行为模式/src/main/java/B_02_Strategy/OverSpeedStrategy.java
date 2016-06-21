package B_02_Strategy;

/**
 * 定义超时处理策略
 * Created by Arvin on 2016/4/26.
 */
public interface OverSpeedStrategy {

    /**
     * 超速处理 接口
     *
     * @param speed 速度
     */
    void process(int speed);
}
