package MysqlOperation.domin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    public void delete(String sql, Connection conn) throws SQLException{
        System.out.println(sql);
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();

        System.out.println(sql);
    }
}
