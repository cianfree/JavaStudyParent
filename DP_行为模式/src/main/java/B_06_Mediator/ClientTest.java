package B_06_Mediator;

/**
 * Created by Arvin on 2016/4/27.
 */
public class ClientTest {

    public static void main(String[] args) {
        QQMediator mediator = new QQMediator();
        Monitor monitor = new Monitor("A");
        TuanZhiShu tuanZhiShu = new TuanZhiShu("B");

        Student studentC = new Student("C");
        Student studentD = new Student("D");

        mediator.addColleague(monitor);
        mediator.addColleague(tuanZhiShu);
        mediator.addColleague(studentC);
        mediator.addColleague(studentD);

        // 班长发通知
        System.out.println("下面的班长发布一个通知的场景：");
        monitor.setContent("明天下午2点开年级会，收到回复^^。");
        mediator.talk(monitor);

        tuanZhiShu.setContent("好的，一定到！");
        mediator.talk(tuanZhiShu);

        studentC.setContent("明天有点事情，可能晚点到");
        mediator.talk(studentC);

        studentD.setContent("明天去不了了！");
        mediator.talk(studentD);

        // 模拟两个同事之间的对话
        System.out.println("下面是两个同学的私下交流：");
        studentC.setContent("你觉得咱们“软件项目管理老师”讲的怎么样？");
        studentD.setContent("我觉得有些地方讲得不是很好！");
        mediator.chat(studentC, studentD);
    }
}
