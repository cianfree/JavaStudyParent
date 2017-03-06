package loadproperties;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载peoperties文件
 * Created by Arvin on 2016/4/27.
 */
public class LoadPropertiesTest {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();

        LoadPropertiesTest loader = new LoadPropertiesTest();

        System.out.println("user.dir: " + System.getProperty("user.dir"));

        // 方式一：加 / 表示项目classpath根目录开始search，不加的话表示从给定class的路径下往下search，
        // 写成/loadproperties/config.properties 或 config.properties均可以
        InputStream inStream = LoadPropertiesTest.class.getResourceAsStream("config.properties");
        //InputStream inStream = GetClassTest.class.getResourceAsStream("../loadproperties/config.properties");
        properties.load(inStream);
        System.out.println("方式一：" + JSON.toJSONString(properties));


        // 方式二：一定要加 /
        inStream = loader.getClass().getResourceAsStream("/loadproperties/config.properties");
        properties.load(inStream);
        System.out.println("方式二：" + JSON.toJSONString(properties));

        // 方式三: 不能加 /
        inStream = LoadPropertiesTest.class.getClassLoader().getResourceAsStream("loadproperties/config.properties");
        properties.load(inStream);
        System.out.println("方式三：" + JSON.toJSONString(properties));


        // 方式四：不能加 /
        inStream = loader.getClass().getClassLoader().getResourceAsStream("loadproperties/config.properties");
        properties.load(inStream);
        System.out.println("方式四：" + JSON.toJSONString(properties));

        // 方式五： 使用绝对路径
        inStream = new FileInputStream(new File("F:\\projects\\IdeaProjects\\InterviewParent\\JavaSE\\src\\main\\java\\loadproperties\\config.properties"));
        properties.load(inStream);
        System.out.println("方式五：" + JSON.toJSONString(properties));
    }
}
