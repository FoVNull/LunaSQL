package DBConn;

import javax.swing.*;
import java.sql.*;

public class MysqlConn {
    public Connection conn(String url,String user,String password)throws SQLException{
        String forword="jdbc:mysql://";String backward="?useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
        //Class.forName("com.mysql.jdbc.Driver");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//8以上的版本用这个驱动
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"系统错误！",
            "请报告JKL4131@126.com",JOptionPane.ERROR_MESSAGE);
        }
        Connection conn=DriverManager.getConnection(forword+url+backward, user, password);
        return conn;
    }
    public ResultSet showDB(Connection conn)throws SQLException{
        String sql = "show databases;";
        ResultSet rs=null;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs = pst.executeQuery();
        return rs;
    }
    public ResultSet openDB(Connection conn,String dbName)throws SQLException{
        String sql="select table_name from information_schema.tables where table_schema='"+dbName+"'"; // and table_type='base table';";
        ResultSet rs;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs=pst.executeQuery();
        return rs;
    }

}
