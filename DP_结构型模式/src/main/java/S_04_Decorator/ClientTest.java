package S_04_Decorator;

/**
 * Created by Arvin on 2016/4/26.
 */
public class ClientTest {

    public static void main(String[] args) {
        Agent agent = new Agent("Arvin");
        System.out.println(agent.getName());

        EditorDecorator editor = new EditorDecorator(agent);
        System.out.println(editor.getName());

        CityDecorator city = new CityDecorator(editor);
        System.out.println(city.getName());
    }
}
