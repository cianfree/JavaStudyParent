package collection;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by Arvin on 2016/4/17.
 */
public class PriorityQueueClient {

    public static void main(String[] args) {

        Comparator<Element> comparator = new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                return o1.getId() - o2.getId();
            }
        };

        PriorityQueue<Element> queue = new PriorityQueue<>(); //new PriorityQueue<>(11, null);

        queue.add(new Element(2, "2"));
        queue.add(new Element(6, "6"));
        queue.add(new Element(1, "1"));

        handleQueue(queue);

    }

    private static void handleQueue(PriorityQueue<Element> queue) {
        while (!queue.isEmpty()) {
            Element elt = queue.remove();
            System.out.println(elt.getId());
        }
    }

}
