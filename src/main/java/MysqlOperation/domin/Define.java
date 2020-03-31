package MysqlOperation.domin;

import DBConn.ConnMange;
import org.json.JSONArray;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Define {
    public void alertStructure(JSONArray json, Connection conn, String tableName, String dbName)throws SQLException {
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
            if(length[n].equals(""))
            if(nullable[n]) {
                if(!length[n].equals(""))
                    sql = "ALTER TABLE " + dbName+"."+tableName + " MODIFY `" + columnName[n] + "` " + columnType[n] + "(" + length[n] + ") DEFAULT NULL; ";
                else{
                    sql = "ALTER TABLE " + dbName+"."+tableName + " MODIFY `" + columnName[n] + "` " + columnType[n] + " DEFAULT NULL; ";
                }
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
            else {
                sql = "ALTER TABLE " + dbName+"."+tableName + " MODIFY `" + columnName[n] + "` " + columnType[n] + "(" + length[n] + ") NOT NULL;";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
            if(pk[n]) {
                try{
                    sql = "ALTER TABLE " + dbName + "." + tableName + " DROP PRIMARY KEY";
                    pst = (PreparedStatement) conn.prepareStatement(sql);
                    pst.executeUpdate();
                }catch (SQLException e){

                }
                sql="ALTER TABLE "+ dbName+"."+tableName +" ADD PRIMARY KEY (`"+columnName[n]+"`)";
                pst = (PreparedStatement) conn.prepareStatement(sql);
                pst.executeUpdate();
            }
        }
    }
    public void insertColumn(Connection conn, String sql) throws SQLException{
        PreparedStatement pst;
        pst=(PreparedStatement)conn.prepareStatement(sql);
        pst.executeUpdate();
    }

    public void changeIndex(Connection conn,String sql){
        try{
            PreparedStatement pst;
            pst=conn.prepareStatement(sql);
            pst.executeUpdate();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),
                    "Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void createTable(Connection conn){

    }
}
