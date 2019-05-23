package MysqlOperation.domin;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Customize {
    public ResultSet excuteSql(Connection conn,String sql) throws SQLException {
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        ResultSet rs=null;
        try {
            rs=pst.executeQuery();
        }
        catch (SQLException ex) {
            pst.executeUpdate();
        }
        return rs;
    }
}
