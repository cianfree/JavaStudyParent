package utils;

/**
 * @author Arvin
 * @time 2017/2/9 19:02
 */
public class Utils {


    public static int[] getArray() {
        return new int[]{5, 1, 2, 3, 6, 8, 10, 5};
    }

    public static int[] getArray2() {
        return new int[]{5, 1, 2, 3, 6, 8, 10, 5, 9};
    }

    public static void show(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * 交换位置
     *
     * @param array
     * @param first
     * @param second
     */
    public static int[] swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
        return array;
    }

    public static int[] copy(int[] source) {
        int[] result = new int[source.length];
        for (int i = 0; i < source.length; ++i) {
            result[i] = source[i];
        }
        return result;
    }

    public static void copy(int[] source, int[] target, int beg, int end) {
        for (int i = beg; i <= end; ++i) {
            target[i] = source[i];
        }
    }
}
