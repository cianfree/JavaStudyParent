package 生产者消费者.桌面搜索;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 索引文件
 * Created by Arvin on 2016/5/28.
 */
public class IndexFileMain {

    public static void main(String[] args) {
        File folder = new File("F:\\projects\\JavaStudyParent");

        File[] roots = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        // 队列仓库存储量
        final int BOUND = 10;
        // 消费者数量
        final int CONSUMER_COUNT = 10;

        final BlockingQueue<File> fileQueue = new LinkedBlockingDeque<>(BOUND);

        // 创建多个生产者
        for (File root : roots) {
            new Thread(new FileCrawler(root, fileQueue)).start();
        }


        // 创建多个消费者
        for (int i = 0; i < CONSUMER_COUNT; ++i) {
            new Thread(new Indexer(fileQueue)).start();
        }
    }

}

/**
 * 生产者
 */
class FileCrawler implements Runnable {

    /**
     * 阻塞队列
     */
    private final BlockingQueue<File> fileQueue;

    /**
     * 要索引的根文件
     */
    private final File root;

    public FileCrawler(File root, BlockingQueue<File> fileQueue) {
        this.root = root;
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 生产者
     *
     * @param root
     * @throws InterruptedException
     */
    private void crawl(File root) throws InterruptedException {
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    crawl(file);
                } else {
                    System.out.println("生产文件: " + file.getPath());
                    fileQueue.put(file);
                }
            }
        }
    }
}

/**
 * 消费者
 */
class Indexer implements Runnable {

    /**
     * 阻塞队列， 要索引的文件列表
     */
    private final BlockingQueue<File> fileQueue;

    public Indexer(BlockingQueue<File> fileQueue) {
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        // 循环索引文件
        while (true) {
            try {
                indexFile(fileQueue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 对文件进行索引
     *
     * @param file
     * @throws InterruptedException
     */
    private void indexFile(File file) throws InterruptedException {
        System.out.println("准备消费文件： " + file.getPath());
    }
}
