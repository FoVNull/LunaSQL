package MysqlOperation.domin;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

public class Delete {
    public void delete(String sql,Connection conn) throws SQLException{
        System.out.println(sql);
        PreparedStatement pst;
        pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();
        System.out.println(sql);
    }
}
