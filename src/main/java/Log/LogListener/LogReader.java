package Log.LogListener;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogReader {
    public ResultSet getGLog(Connection conn){
        String sql="SHOW VARIABLES LIKE 'general_log%'";
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
    public void getSLog(Connection conn){
        String sql="SHOW VARIABLES LIKE 'slow%'";
        try{
            PreparedStatement pst=conn.prepareStatement(sql);
            pst.executeQuery();
            ResultSet rs;
            rs = pst.executeQuery();
            rs.next();
            rs.next();String status=rs.getString(2);
            rs.next();String root=rs.getString(2);
            if(status.equals("OFF")){
                JOptionPane.showConfirmDialog(
                        null,"未开启slow_log是否开启？","日志未启动",JOptionPane.WARNING_MESSAGE
                );
            }else{
                try {
                    Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + root);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
