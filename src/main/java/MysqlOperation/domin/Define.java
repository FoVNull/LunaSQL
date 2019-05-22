package MysqlOperation.domin;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import org.json.JSONArray;

import java.sql.SQLException;

public class Define {
    public void alertStructure(JSONArray json, Connection conn,String tableName,String dbName)throws SQLException {
        int columnCount=json.length();
        String[] columnName=new String[columnCount];
        String[] columnType=new String[columnCount];
        String[] length=new String[columnCount];
        Boolean[] pk=new Boolean[columnCount];
        Boolean[] nullable=new Boolean[columnCount];
        for(int i=0;i<columnCount;i++) {
            columnName[i] = json.getJSONObject(i).getString("name");
            columnType[i]=json.getJSONObject(i).getString("type");
            length[i]=json.getJSONObject(i).getString("length");
            if(json.getJSONObject(i).getString("pk").equals("true")){
                pk[i]=true;
            }
            else {
                pk[i]=false;
            }
            if(json.getJSONObject(i).getString("nullable").equals("true")){
                nullable[i]=true;
            }
            else {
                nullable[i]=false;
            }
        }
        String sql;
        PreparedStatement pst;
        for(int n=0;n<columnCount;n++) {
            if(nullable[n]) {
                sql = "ALTER TABLE " + dbName+"."+tableName + " MODIFY " + columnName[n] + " " + columnType[n] + "(" + length[n] + ") DEFAULT NULL; ";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
            else {
                sql = "ALTER TABLE " + dbName+"."+tableName + " MODIFY " + columnName[n] + " " + columnType[n] + "(" + length[n] + ") NOT NULL;";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
            if(pk[n]){
                sql="ALTER TABLE "+ dbName+"."+tableName +" DROP PRIMARY KEY";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
                sql="ALTER TABLE "+ dbName+"."+tableName +" ADD PRIMARY KEY ("+columnName[n]+")";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
        }
    }
    public void insertCoulum(Connection conn,String tableName,String dbName,String sql) throws SQLException{
        PreparedStatement pst;
        pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();
    }
}