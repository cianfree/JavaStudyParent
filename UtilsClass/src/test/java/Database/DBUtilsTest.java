package Database;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 夏集球
 * @time 2016/9/24$ 21:29$
 */
public class DBUtilsTest {

    @Test
    public void testSelect() {
        DBUtils jdbc = DBUtils.mysqlBuilder("kkpoem").build();

        int count = jdbc.getInt("SELECT COUNT(*) FROM poem", 0);
        System.out.println("PoemCount: " + count);

        long c = jdbc.getLong("SELECT COUNT(*) FROM poem where updated_time > ?", 0L, new Date());
        System.out.println("CreateBeforeNow: " + c);
    }

    @Test
    public void testSelectPoem() {
        DBUtils jdbc = DBUtils.mysqlBuilder("kkpoem").build();
        Poem poem = jdbc.getObject("SELECT * FROM poem LIMIT 1", Poem.class);

        System.out.println(JSON.toJSONString(poem));

        List<Poem> poemList = jdbc.getModelList("SELECT * FROM poem LIMIT ?,?", Poem.class, 0, 5);
        System.out.println(JSON.toJSONString(poemList));


        Map<String, Object> map = jdbc.getMap("SELECT * FROM poem LIMIT 1");

        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void testInsert() {
        DBUtils jdbc = DBUtils.mysqlBuilder("kkpoem").build();
        Poem poem = jdbc.getObject("SELECT * FROM poem LIMIT 1", Poem.class);
        poem.setId(jdbc.getNextIntId(Poem.class));
        System.out.println("Insert Poem: " + poem.getId());

        // 两种写法都行
        jdbc.insert(poem, "viewCount", "voicePath");
        //jdbc.insert(poem, "view_count", "voice_path");

        poem = jdbc.getObject("SELECT * FROM poem where id = ?", Poem.class, poem.getId());
        System.out.println(JSON.toJSONString(poem));
    }

    @Test
    public void testDelete() {
        DBUtils jdbc = DBUtils.mysqlBuilder("kkpoem").build();
        System.out.println(jdbc.delete("poem", "id", 255084));
    }

    @Test
    public void testUpdate() {
        DBUtils jdbc = DBUtils.mysqlBuilder("kkpoem").build();

        Poem poem = jdbc.getObject("SELECT * FROM poem WHERE id = 1", Poem.class);

        String oldMingcheng = poem.getMingcheng();
        poem.setMingcheng("关雎关雎");

        jdbc.update(poem, "viewCount", "voicePath");

        poem = jdbc.getObject("SELECT * FROM poem WHERE id = 1", Poem.class);

        System.out.println(poem.getMingcheng());

        poem.setMingcheng(oldMingcheng);

        jdbc.update(poem, "viewCount", "voicePath");
        poem = jdbc.getObject("SELECT * FROM poem WHERE id = 1", Poem.class);

        System.out.println(poem.getMingcheng());
    }
}