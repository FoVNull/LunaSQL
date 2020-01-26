package MysqlOperation.domin;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    public void update(String[] sql, Connection conn) throws SQLException {
        int i;
        PreparedStatement pst;
        for(i=0;i<sql.length;i++){
            if(sql[i]!=null) {
                pst = (PreparedStatement) conn.prepareStatement(sql[i]);
                //System.out.println(sql[i]);
                pst.executeUpdate();
            }
        }
    }
}
