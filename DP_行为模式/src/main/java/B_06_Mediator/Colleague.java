package B_06_Mediator;

/**
 * 抽象同事类
 * Created by Arvin on 2016/4/27.
 */
public abstract class Colleague {

    private String name;
    private String content;

    public Colleague(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public abstract void talk();
}
