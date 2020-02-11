package Log.LogListener;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingEdit {
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
