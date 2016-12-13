package com.github.cianfree.ip;

/**
 * @author Arvin
 * @time 2016/12/13 13:18
 */
public class IPModel {

    private String begin;

    private String end;

    private String address;
    private String comment;

    public IPModel() {
    }

    public IPModel(String begin, String end, String address, String comment) {
        this.begin = begin;
        this.end = end;
        this.address = address;
        this.comment = comment;
    }

    public String getBegin() {
        return begin;
    }

    public IPModel setBegin(String begin) {
        this.begin = begin;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public IPModel setEnd(String end) {
        this.end = end;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public IPModel setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public IPModel setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
