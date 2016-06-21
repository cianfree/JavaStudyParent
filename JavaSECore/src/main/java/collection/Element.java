package collection;

/**
 * 列表元素定义
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/4/17 11:47
 * @since 0.1
 */
public class Element implements Comparable<Element> {

    private int id;
    private String value;

    public Element(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Element o) {
        return id - o.id;
    }
}
