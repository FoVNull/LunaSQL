package MysqlOperation.domin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    public void delete(String sql, Connection conn) throws SQLException{
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();
    }
}
