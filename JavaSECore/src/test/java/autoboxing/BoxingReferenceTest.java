package autoboxing;

import java.lang.reflect.Field;

/**
 * 包装类型传递引用测试， 通过反射，final类型的都会被改变
 * Created by Arvin on 2016/5/16.
 */
public class BoxingReferenceTest {

    public static void main(String[] args) {
        //testIntegerChange();
        testValueChange();
    }

    private static void testValueChange() {
        Value result = new Value(10);
        printResult(result);
        // 反射修改
        try {
            Field field = result.getClass().getDeclaredField("value");
            System.out.println(field);
            field.setAccessible(true);
            field.set(result, 100);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        printResult(result);
    }

    private static void testIntegerChange() {
        Integer result = 10;
        printResult(result);
        changeInteger(result);
        printResult(result);
    }

    private static void changeInteger(Integer result) {
        try {
            Field field = result.getClass().getDeclaredField("value");
            System.out.println(field);
            field.setAccessible(true);
            field.set(result, 100);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void printResult(Object result) {
        System.out.println("Result = " + result);
    }
}

class Value {
    private final int value;

    public Value(int value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}