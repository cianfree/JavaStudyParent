package B_06_Mediator;

/**
 * Created by Arvin on 2016/4/27.
 */
public class Student extends Colleague {
    public Student(String name) {
        super(name);
    }

    @Override
    public void talk() {
        System.out.println("学生[" + getName() + "]说：" + this.getContent());
    }
}
