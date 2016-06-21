package S_04_Decorator;

/**
 * 代理商
 * Created by Arvin on 2016/4/26.
 */
public class Agent {

    private String name;

    public Agent() {
    }

    public Agent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
