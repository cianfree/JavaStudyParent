package collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 * 遍历过程中对集合元素进行移除
 * 1. 在使用for循环的时候，长度选用list.size()方法是没问题的，可以进行删除
 * 2. 如果使用定义一个变量size=list.size()，在循环for中使用size，那么如果移除了对象，会出现下标越界异常
 * 3. 如果使用foreach，那么会抛出ConcurrentModificationException，如果删除后立马break跳出，不会有异常
 * 4. 使用Iterator iter = list.iterator()进行删除的话，如果是iter.remove(); 那么不会有问题，如果使用list.remove(),也会
 *      抛出ConcurrentModificationException， 但是如果list.remove()后，跳出（break）循环，也不会有问题
 * 5. 反编译foreach过来，你会发现，实际上foreach的实现是iterator
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/4/17 11:34
 * @since 0.1
 */
public class IterateThenChangeElement {

    public static void main(String[] args) throws InterruptedException {

        List<Element> list = getList();

        try {
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).getId() == 2) {
                    list.remove(list.get(i));
                }
            }
            System.out.println("从0开始遍历过程中删除元素--成功");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("从0开始遍历过程中删除元素失败");
            e.printStackTrace();
            Thread.sleep(500);
        }

        list = getList();
        try {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getId() == 2) {
                    list.remove(list.get(i));
                }
            }
            System.out.println("从最后一个开始遍历过程中删除元素--成功");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("从最后一个开始遍历过程中删除元素失败：");
            e.printStackTrace();
            Thread.sleep(500);
        }

        list = getList();
        Iterator<Element> iter = list.iterator();
        try {
            while (iter.hasNext()) {
                Element val = iter.next();
                if (val.getId() == 2) {
                    list.remove(val);
                    break; // 删除后立马跳出也不会存在异常

                    //iter.remove();
                    //这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，
                    //则同样会出现ConcurrentModificationException
                }
            }
            System.out.println("使用Iterator遍历过程中删除元素--成功");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("使用Iterator遍历过程中删除元素失败");
            e.printStackTrace();
            Thread.sleep(500);
        }

        list = getList();
        try {
            for (Element val : list) {
                if (val.getId() == 2) {
                    list.remove(val);
                    break; // 删除后立马跳出也不会存在异常
                }
            }
            System.out.println("使用Foreach遍历过程中删除元素--成功");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("使用Foreach遍历过程中删除元素失败");
            e.printStackTrace();
            Thread.sleep(500);
        }


    }

    public static List<Element> getList() {
        List<Element> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            list.add(new Element(i, String.valueOf(i)));
        }
        return list;
    }
}
