package B_04_Observer;

/**
 * Created by Arvin on 2016/4/26.
 */
public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyAllObservers();
}
