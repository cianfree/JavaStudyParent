package B_10_Iterator;

/**
 * Created by Arvin on 2016/4/27.
 */
public class StringContainer implements Container<String> {

    private final String[] strings = {"first", "second", "third", "fourth", "fifth"};

    @Override
    public Iterator<String> iterator() {
        return new StringIterator();
    }

    class StringIterator implements Iterator<String> {
        // 从0开始遍历
        private int index = 0;

        @Override
        public boolean hasNext() {
            return strings.length > index;
        }

        @Override
        public String next() {
            return strings[index++];
        }
    }
}
