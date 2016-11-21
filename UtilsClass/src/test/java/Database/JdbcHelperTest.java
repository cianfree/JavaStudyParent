package Database;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @time 2016/11/18 15:45
 */
public class JdbcHelperTest {

    private JdbcHelper localJdbcHelper = JdbcHelper.mysqlBuilder("kkpoem").build();

    @Test
    public void testListModel() {
        List<Poem> poemList = localJdbcHelper.paging(1, 2).listModel("select * from poem", Poem.class, null);

        System.out.println(poemList.size());

        System.out.println(JSON.toJSONString(poemList));
    }

    @Test
    public void testGetById() {

        Poem poem = localJdbcHelper.getById(Poem.class, 1, null);

        System.out.println(JSON.toJSONString(poem));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> dataMap = localJdbcHelper.table("poem").getMap("select * from poem limit 1");
        System.out.println(JSON.toJSONString(dataMap));
    }

    @Test
    public void testListMap() {
        List<Map<String, Object>> listMap = localJdbcHelper
                .table("poem")
                .paging(1, 2)
                .listMap("select * from poem");
        System.out.println(listMap.size());
    }

    @Test
    public void testInsertModel() {
        JdbcHelper jdbcHelper = JdbcHelper.mysqlBuilder("kkpoem").build();

        JdbcHelper.GlobalConfig.setTypeDefaultValueMap(String.class, "str");

        Poem poem = new Poem();
        poem.setBeijing("beijign");

        jdbcHelper.ignoredFields("id")
                .ignoredFields("yuanwen")
                .javaFieldDefaultValue("mingcheng", "hello")
                .table("t_poem")
                .autoConvertNullField(true)
                .insertModel(poem);
    }

    @Test
    public void testInsertMap() {
    }

}