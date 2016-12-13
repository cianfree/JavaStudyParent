package com.github.cianfree.rtree.ip;

/**
 * @author Arvin
 * @time 2016/12/13 9:46
 */
public class IPData {

    private String address;

    private String begin;
    private String end;

    public IPData() {
    }

    public String getAddress() {
        return address;
    }

    public IPData setAddress(String address) {
        this.address = address;
        return this;
    }

    public IPData(String address, String begin, String end) {
        this.address = address;
        this.begin = begin;
        this.end = end;
    }

    public String getBegin() {
        return begin;
    }

    public IPData setBegin(String begin) {
        this.begin = begin;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public IPData setEnd(String end) {
        this.end = end;
        return this;
    }
}
