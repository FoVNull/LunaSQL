package Optimization.Evaluation.ParameterOpt.domin;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SettingEdit {

    public HashMap<String,String> paraQuery(Connection conn){
        HashMap<String,String> res=new HashMap<>();
        String[] sql= {"show variables like 'table_open_cache'",
                "show status like 'Open_tables'",
                "show status like 'Opened_tables'",
                "show variables like 'innodb_flush_log_at_trx_commit'",
                "show variables like 'innodb_lock_wait_timeout'",
                "show variables like 'innodb_log_buffer_size'",
                "show variables like 'key_buffer_size'",
                "show variables like 'join_buffer_size'",
                "show variables like 'read_buffer_size'",
                "show variables like 'sort_buffer_size'",
                "show variables like 'binlog_cache_size'",
                "show variables like 'sync_binlog'"
        };
        for(int i=0;i<12;++i){
            try{
                PreparedStatement pst=conn.prepareStatement(sql[i]);
                ResultSet rs=pst.executeQuery();
                rs.next();
                res.put(rs.getString(1),rs.getString(2));
            }catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        return res;
    }

    public void editPara(Connection conn,Map<String,String> paras){
        String[] sql=new String[10];
        int p=0;
        for(Map.Entry<String,String> entry:paras.entrySet()){
            sql[p]="set global "+entry.getKey()+" = "+entry.getValue();
            ++p;
        }
        for(String s:sql) {
            try {
                PreparedStatement pst = conn.prepareStatement(s);
                pst.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void openIni(Connection conn){
        char[] ver={0};
        try {
            String sql = "select version()";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            ver=rs.getString(1).toCharArray();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        if(ver[0]>='8') {
            try {
                Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " +
                        "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\my.ini");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            String var = "";
            try {
                String sql = "SHOW VARIABLES LIKE 'character_sets_dir'";
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                rs.next();
                var = rs.getString(2);
                var = var.split("share")[0];
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            var += "/my.ini";
            try {
                Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + var);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
