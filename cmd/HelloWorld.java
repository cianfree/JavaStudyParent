
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, " + (null != args && args.length > 0 ? args[0] : "World!"));
    }
}