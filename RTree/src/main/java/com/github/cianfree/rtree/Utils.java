package com.github.cianfree.rtree;

import java.util.Collection;

/**
 * @author Arvin
 * @time 2016/12/12 10:27
 */
public class Utils {

    public static boolean isEmpty(Collection<?> cols) {
        return null == cols || cols.isEmpty();
    }
}
