package collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvin on 2016/4/17.
 */
public class FailFastList {

    public static void main(String[] args) throws InterruptedException {

        final List<Element> list = getList();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Element elt : list) {
                    if (elt.getId() == 2) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //list.remove(1);
                list.add(new Element(11, "11"));
            }
        }).start();

        Thread.sleep(1000);
    }

    public static List<Element> getList() {
        List<Element> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            list.add(new Element(i, String.valueOf(i)));
        }
        return list;
    }
}
