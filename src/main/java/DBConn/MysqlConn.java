package DBConn;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlConn {
    public Connection conn(String url,String user,String password)throws ClassNotFoundException, SQLException{
        String forword="jdbc:mysql://";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn= (Connection) DriverManager.getConnection(forword+url, user, password);
        //System.out.print(conn);
        return conn;
    }
    public ResultSet showDB(Connection conn)throws SQLException{
        String sql = "show databases;";
        ResultSet rs=null;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs = pst.executeQuery();
//        while(rs.next()){
//            System.out.println(rs.getString("Database")+"+");
//        }
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
