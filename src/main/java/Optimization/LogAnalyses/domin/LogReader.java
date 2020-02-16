package Optimization.LogAnalyses.domin;

import Optimization.LogAnalyses.GetVar;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogReader {
    /*
    记事本打开文件为win下的处理方法
     */
    public ResultSet getGLog(Connection conn){
        String sql="SHOW VARIABLES LIKE 'general_log%'";
        ResultSet rs;
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
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
                    if(track.substring(0,4).equals("/var")){
                        GetVar getVar=new GetVar();
                        Runtime.getRuntime().exec(getVar.getVarRoot(track,conn));
                    }else {
                        Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + track);
                    }
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(
                            null,ioe.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    ioe.printStackTrace();
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
            rs.next();rs.next();rs.next();
            String root=rs.getString(2);
            try {
                if(root.substring(0,4).equals("/var")) {
                    GetVar getVar=new GetVar();
                    Runtime.getRuntime().exec(getVar.getVarRoot(root,conn));
                }else {
                    Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + root);
                }
            }catch(IOException e){
                JOptionPane.showMessageDialog(
                        null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void getELog(Connection conn){
        String sql="SHOW VARIABLES LIKE 'log_error'";
        try{
            PreparedStatement pst=conn.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            rs.next();String root=rs.getString(2);
            try {
                if(root.substring(0,4).equals("/var")) {
                    GetVar getVar=new GetVar();
                    Runtime.getRuntime().exec(getVar.getVarRoot(root,conn));
                }else {
                    Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + root);
                }
            }catch (IOException e){
                JOptionPane.showMessageDialog(
                        null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(
                    null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
