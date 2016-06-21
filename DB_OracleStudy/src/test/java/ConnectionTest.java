import org.junit.Test;

import java.sql.*;

/**
 * 基本Oracle链接测试
 * Created by Arvin on 2016/4/27.
 */
public class ConnectionTest {
    @Test
    public void testConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        //加入oracle的驱动，“”里面是驱动的路径
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // 数据库连接:
        // oracle代表链接的是oracle数据库
        // thin:@MyDbComputerNameOrIP代表的是数据库所在的IP地址（可以保留thin:）；
        // 1521代表链接数据库的端口号；
        // ORCL代表的是数据库名称
        String url = "jdbc:oracle:thin:@localhost:1521:XE";

        // 数据库用户登陆名 ( 也有说是 schema 名字的 )
        String UserName = "SCOTT";
        // 密码
        String Password = "TIGER";
        conn = DriverManager.getConnection(url, UserName, Password);
        System.out.println("连接成功： " + conn);
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT DNAME FROM DEPT");
        ResultSet result = stmt.getResultSet();
        while(result.next()) {
            String dname = result.getString(1);
            System.out.println(dname);
        }
        stmt.close();
        conn.close();
    }
}
