package MysqlOperation.domin;

import org.json.JSONArray;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    public void insert(Connection conn, String table, String dbName, String[] columnName, String[] value){
        int i = 0;
        int columnCount=columnName.length;
        String sql="insert into " + dbName + "." + table+"(";
        for(i=0;i<columnCount-1;i++){
            sql+=columnName[i]+",";
        }
        sql+=columnName[columnCount-1]+")";
        sql+=" values(";
        for(int n=0;n<columnCount-1;n++) {
            sql+= "?,";
        }
        sql+="?);";
        //System.out.println(sql);
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            for(int j=0;j<columnCount;j++) {
                if(value[j].equals("")){
                    value[j]=null;
                }
                pstmt.setString(j+1, value[j]);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            if(sw.toString().contains("cannot be null")) {
                String str=sw.toString().split("'")[1];
                JOptionPane.showMessageDialog(null,str+"字段不能为空","错误信息",JOptionPane.ERROR_MESSAGE);
            }
            if(sw.toString().contains("PRIMARY")){
                String str=sw.toString().split("'")[1];
                JOptionPane.showMessageDialog(null,"重复输入了主键值"+str,"错误信息",JOptionPane.ERROR_MESSAGE);
            }
            if(sw.toString().contains("Incorrect")&&sw.toString().contains("value")){
                String str=sw.toString().split("'")[3];
                JOptionPane.showMessageDialog(null,"字段"+str+"类型错误","错误信息",JOptionPane.ERROR_MESSAGE);
            }
            //e.printStackTrace();
        }

    }
}
