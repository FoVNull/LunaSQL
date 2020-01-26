package Log.LogListener;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogReader {
    public ResultSet getLog(int logType,Connection conn){
        String sql="";
        switch (logType){
            case 0:sql="SHOW VARIABLES LIKE 'general_log%'";
            case 1: ;
        }
        ResultSet rs;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            if(!rs.getString(2).equals("ON")) {
                JOptionPane.showConfirmDialog(
                        null,"未开启general_log是否开启？","日志未启动",JOptionPane.WARNING_MESSAGE
                );
            }else{
                rs.next();String track=rs.getString(2);
                sql="show variables like 'log_output'";
                pst=conn.prepareStatement(sql);rs=pst.executeQuery();
                rs.next();
                if(rs.getString(2).contains("TABLE")){
                    sql="SELECT * FROM mysql.general_log";
                    pst=conn.prepareStatement(sql);rs=pst.executeQuery();
                    return rs;
                }else{
                    try {
                        Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe "+track);
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(
                                null,ioe.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                        ioe.printStackTrace();
                    }
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
}
