package Database;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @time 2016/11/18 15:45
 */
public class JdbcHelperTest {

    @Test
    public void testInsertModel() {
        JdbcHelper jdbcHelper = JdbcHelper.mysqlBuilder("rise_db").build();

        JdbcHelper.GlobalConfig.setTypeDefaultValueMap(String.class, "str");

        Poem poem = new Poem();
        poem.setBeijing("beijign");

        jdbcHelper.ignoredFields("id")
                .ignoredFields("yuanwen")
                .table("t_poem")
                .autoConvertNullField(true)
                .insertModel(poem);


    }

}