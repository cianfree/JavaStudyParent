package B_04_Observer;

import java.util.Scanner;

/**
 * Created by Arvin on 2016/4/26.
 */
public class JobSeeker implements Observer {

    private String name;

    public JobSeeker(String name) {
        this.name = name;
    }

    @Override
    public void update(Subject subject) {
        System.out.println(this.name + " got notified!");//print job list
        System.out.println(subject);
    }
}
