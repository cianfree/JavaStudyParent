package collection;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 如何使得HashMap同步
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/4/17 12:58
 * @since 0.1
 */
public class HashMapSync {

    public static void main(String[] args) throws InterruptedException {
        testNullKeyAndNullValue();
    }

    /**
     * 测试Null key NUllValue
     */
    private static void testNullKeyAndNullValue() {

        Map<Integer, Integer> hashMap = new HashMap<>();

        hashMap.put(1, null);
        System.out.println("HashMap 设置null 值成功");
        hashMap.put(null, 1);
        System.out.println("HashMap 设置null key成功");

        Map<Integer, Integer> hashTable = new Hashtable<>();

        try {
            hashTable.put(1, null);
            System.out.println("Hashtable 设置null值成功");
        } catch (Exception e) {
            System.out.println("Hashtable 设置null值失败");
        }

        try {
            hashTable.put(null, 1);
            System.out.println("Hashtable 设置null key成功");
        } catch (Exception e) {
            System.out.println("Hashtable 设置null key失败");
        }
    }
}
