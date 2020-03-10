package MysqlOperation.domin;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
    public ResultSet queryColumns(Connection conn, String dbName, String tableName)throws SQLException {
        String sql="select column_name from information_schema.columns where table_schema='"+dbName+"' and table_name='"+tableName+"';";
        ResultSet rs;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs=pst.executeQuery();
        return rs;
    }
    public ResultSet query(Connection conn,String dbName,String tableName) throws SQLException{
        String sql="SELECT * FROM "+dbName+"."+tableName;
        ResultSet rs;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs=pst.executeQuery();
        return rs;
    }
    public String[] queryPK(Connection conn,String dbName,String tableName) throws SQLException{
        String sql="SELECT column_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE table_name='"+tableName+
                     "'AND CONSTRAINT_SCHEMA='"+dbName+" 'AND CONSTRAINT_NAME='PRIMARY'";
        ResultSet rs;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs=pst.executeQuery();
        rs.last();
        String[] pk=new String[rs.getRow()];
        int i=0;
        rs.beforeFirst();
        while(rs.next()){
            pk[i]=rs.getString(1);
            i++;
        }
        return pk;
    }
    public JSONArray queryType(Connection conn, String dbName, String tableName) throws SQLException{
        JSONArray jsonArray=new JSONArray();
        String sql="SELECT COLUMN_TYPE,IS_NULLABLE,COLUMN_NAME FROM information_schema.columns WHERE table_schema='"+dbName+"'AND table_name = '"+tableName+"'";
        ResultSet rs;
        PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
        rs=pst.executeQuery();
        while (rs.next()){
            JSONObject jsonObject=new JSONObject();
            String type=rs.getString(1);
            String length="";
            if(rs.getString(1).contains("(")){
               type= rs.getString(1).split("\\(")[0];
               length= rs.getString(1).split("\\(")[1].split("\\)")[0];
            }
            jsonObject.put("type",type);
            jsonObject.put("length",length);
            jsonObject.put("nullable",rs.getString(2));
            jsonObject.put("name",rs.getString(3));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
    public JSONArray showIndexs(Connection conn,String dbName,String tableName){
        JSONArray indexs=new JSONArray();

        String sql="show index from "+dbName+"."+tableName;
        ResultSet rs=null;
        try{
            PreparedStatement pst=(PreparedStatement)conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while (rs.next()){
                JSONObject object=new JSONObject();
                object.put("indexName",rs.getString("Key_name"));
                object.put("columnName",rs.getString("Column_name"));
                object.put("nonUnique",rs.getString("Non_unique"));
                indexs.put(object);
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),
                "Error",JOptionPane.ERROR_MESSAGE);
        }

        return indexs;
    }
}
