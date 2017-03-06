package com.github.search.simple.model;

import java.util.Comparator;

/**
 * @author Arvin
 * @time 2016/10/11$ 10:20$
 */
public class ResultItem {

    public static final Comparator<ResultItem> defaultComparator = new Comparator<ResultItem>() {
        @Override
        public int compare(ResultItem o1, ResultItem o2) {
            return o1.getSimilarity() == o2.getSimilarity() ? 0 : o1.getSimilarity() < o2.getSimilarity() ? 1 : -1;
        }
    };

    /** 结果ID */
    private Element element;
    /** 相似度 */
    private float similarity;

    public ResultItem() {
    }

    public ResultItem(Element element, float similarity) {
        this.element = element;
        this.similarity = similarity;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

}
