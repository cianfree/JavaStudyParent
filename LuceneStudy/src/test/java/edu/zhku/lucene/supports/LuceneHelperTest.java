package edu.zhku.lucene.supports;

import edu.zhku.lucene.model.Poem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 夏集球
 * @time 2016/9/18$ 23:17$
 */
public class LuceneHelperTest {

    @Before
    public void before() {
        System.out.println("Init...");
    }

    @After
    public void after() {
        System.out.println("After...");
        LuceneHelper.exit();
    }


    /**
     * 构造索引
     *
     * @throws Exception
     */
    @Test
    public void testIndex() throws Exception {
        // 索引
        System.out.println("开始索引......");
        LuceneHelper.index(DataProvider.getTestPoemList());
        System.out.println("索引完成......");

        List<Poem> poemList = LuceneHelper.search("关雎", 1, 10);

        printPoems(poemList);

    }

    /**
     * 打印诗词列表
     *
     * @param poemList 要打印的诗词列表
     */
    private void printPoems(List<Poem> poemList) {
        if (null != poemList && !poemList.isEmpty()) {
            for (Poem poem : poemList) {
                System.out.println(poem.getId() + ": " + poem.getAuthor() + ": " + poem.getTitle() + ": " + poem.getContent());
            }
        }
    }
}