package com.github.cianfree.ip;

/**
 * @author Arvin
 * @time 2016/12/9 14:41
 */
public class IpRange {

    private final int begin;
    private final int end;

    public IpRange(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }


    public int getEnd() {
        return end;
    }

    /**
     * 比较
     *
     * @param target 要比较的目标
     */
    public CompareType compareTo(IpRange target) {
        // TODO
        return null;
    }
}
