package 同步工具类.FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 加载配置，演示使用FutureTask
 * Created by Arvin on 2016/5/28.
 */
public class LoadConfigure {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> configCountTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("执行Task。。。。。");
                Thread.sleep(2000);
                System.out.println("完成Task。。。。。");
                return 100;
            }
        });

        Thread taskThread = new Thread(configCountTask);
        taskThread.start();
        // 这里可以执行其他与本程序无关的操作
        System.out.println("ConfigCount: " + configCountTask.get());

        System.out.println("End of Main");

    }
}


