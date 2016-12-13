package com.github.cianfree.rtree.ip;

/**
 * @author Arvin
 * @time 2016/12/13 13:06
 */
public class IPRange implements IRange {

    private final long begin;
    private final long end;

    public IPRange(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public IRange merge(IRange range) {
        if (range instanceof IPRange) {
            IPRange target = IPRange.class.cast(range);
            long newBegin = begin <= target.begin ? begin : target.begin;
            long newEnd = end >= target.end ? end : target.end;
            return new IPRange(newBegin, newEnd);
        }
        throw new RuntimeException("Invalid range: " + range);
    }

    @Override
    public int compareTo(IRange o) {
        if (o instanceof IPRange) {
            IPRange target = IPRange.class.cast(o);
            if (begin == target.begin && end == target.end) {
                return 0;
            }
            if (end <= target.begin) {
                return -1;
            } else if (begin >= target.end) {
                return 1;
            }
        }
        throw new RuntimeException("Invalid range: " + o);
    }

    @Override
    public boolean include(IRange range) {
        if (range instanceof IPRange) {
            IPRange target = IPRange.class.cast(range);
            return begin <= target.begin && end >= target.end;
        }
        throw new RuntimeException("Invalid range: " + range);
    }

    @Override
    public String toString() {
        return "IPRange{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}
