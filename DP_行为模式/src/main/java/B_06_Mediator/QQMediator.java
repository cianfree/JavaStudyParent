package B_06_Mediator;

/**
 * Created by Arvin on 2016/4/27.
 */
public class QQMediator extends Mediator {
    @Override
    public void talk(Colleague colleague) {
        colleague.talk();
    }

    @Override
    public void chat(Colleague a, Colleague b) {
        a.talk();
        b.talk();
    }
}
