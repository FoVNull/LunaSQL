package Log.LogListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LogStatus {
    public JSONObject searchGLog(Connection conn){
        String g_sql="SHOW VARIABLES LIKE 'general_log%'";
        ResultSet rs;
        JSONObject res=new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(g_sql);
            rs = pst.executeQuery();
            rs.next();String status=rs.getString(2);
            rs.next();String location=rs.getString(2);
            g_sql="show variables like 'log_output';";
            pst=conn.prepareStatement(g_sql);
            rs=pst.executeQuery();
            rs.next();String storage=rs.getString(2);
            res.put("status",status);
            res.put("location",location);
            res.put("storage",storage);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }
    public JSONObject searchSLog(Connection conn){
        String s_sql="SHOW VARIABLES LIKE 'slow%'";
        ResultSet rs;
        JSONObject jsonObject=new JSONObject();
        try {
            PreparedStatement pst = conn.prepareStatement(s_sql);
            rs = pst.executeQuery();
            rs.next();String long_time=rs.getString(2);
            rs.next();String status=rs.getString(2);
            rs.next();
            jsonObject.put("time",long_time);
            jsonObject.put("status",status);
            jsonObject.put("location",rs.getString(2));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String searchELog(Connection conn) {
        String err_sql = "SHOW VARIABLES LIKE 'log_error'";
        ResultSet rs;
        try {
            PreparedStatement pst=conn.prepareStatement(err_sql);
            rs=pst.executeQuery();
            rs.next();
            return rs.getString(2);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return " ";
    }
}
