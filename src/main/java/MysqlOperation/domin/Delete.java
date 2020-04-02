package MysqlOperation.domin;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    public void delete(String sql, Connection conn) throws SQLException{
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();
    }

    public void deleteTable(Connection conn,String dbName,String tableName){
        try {
            String sql="DROP TABLE "+dbName+"."+tableName;
            PreparedStatement pst;
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"删除成功！",
                    "Message",JOptionPane.PLAIN_MESSAGE);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,"<html>"+e.getMessage()+"</html>",
                    "Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
