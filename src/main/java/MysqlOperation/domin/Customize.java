package MysqlOperation.domin;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
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
            StringWriter sw=new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            if(sw.toString().contains("with executeQuery")){
                try {
                    pst.executeUpdate();
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(null,e.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null,ex.getMessage(),
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        return rs;
    }
}
