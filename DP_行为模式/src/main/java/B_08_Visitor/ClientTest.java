package B_08_Visitor;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        PersonVisitor personVisitor = new PersonVisitor();
        MonkeyVisitor monkeyVisitor = new MonkeyVisitor();

        City city = new City();
        // 有人过来访问了
        city.accept(personVisitor);
        // 猴子来了
        city.accept(monkeyVisitor);

        // 无论有什么变化的逻辑都可以通过Visitor进行扩展，无需修改元素
        // 比如现在需求变了，有个熊来了，此时我们不需要对Element进行任何的修改，只需要添加Visitor即可
        // 以不变（Element不变）应万变（不知道会什么什么过来访问，扩展Visitor即可）
    }
}
