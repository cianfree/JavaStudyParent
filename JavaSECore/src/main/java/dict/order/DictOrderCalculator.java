package dict.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Arvin
 * @time 2016/10/8$ 14:16$
 */
public class DictOrderCalculator {

    public static void main(String[] args) {
        System.out.println(getSign("pressId", "time", "privateKey", "levelId"));
    }

    public static String getSign(Object... params) {
        if (null == params || params.length < 1) {
            return "";
        }
        List<String> list = new ArrayList<>();
        for (Object param : params) {
            list.add(String.valueOf(param));
        }
        Collections.sort(list); // 利用String的比较进行字典排序
        return join(list, ""); // 合并字符串，并进行加密
    }

    public static String join(List<String> list, String sep) {
        if (null == list || list.isEmpty()) {
            return "";
        }
        int size = list.size();
        if (size == 1) {
            return list.get(0);
        }
        String realSep = null == sep ? "" : sep;
        StringBuilder builder = new StringBuilder();
        int i;
        for (i = 0; i < size - 1; ++i) {
            builder.append(list.get(i)).append(realSep);
        }
        builder.append(list.get(i));
        return builder.toString();
    }
}
