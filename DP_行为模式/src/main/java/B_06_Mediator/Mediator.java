package B_06_Mediator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arvin on 2016/4/27.
 */
public abstract class Mediator {
    protected Set<Colleague> colleagues = new HashSet<>();

    public void addColleague(Colleague colleague) {
        colleagues.add(colleague);
    }

    /**
     * 提醒其他成员
     *
     * @param colleague
     */
    public abstract void talk(Colleague colleague);

    /**
     * A, B 对话
     *
     * @param a
     * @param b
     */
    public abstract void chat(Colleague a, Colleague b);
}
