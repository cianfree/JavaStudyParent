package B_04_Observer;

import java.util.ArrayList;

/**
 * Created by Arvin on 2016/4/26.
 */
public class HeadHunter implements Subject {
    private ArrayList<Observer> userList;
    private ArrayList<String> jobs;

    public HeadHunter() {
        userList = new ArrayList<>();
        jobs = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        userList.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        userList.remove(o);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer o : userList) {
            o.update(this);
        }
    }

    public void addJob(String job) {
        this.jobs.add(job);
        notifyAllObservers();
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public String toString() {
        return jobs.toString();
    }
}
