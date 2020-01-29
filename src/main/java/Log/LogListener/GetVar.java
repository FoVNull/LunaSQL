package Log.LogListener;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetVar {
    public String getVarRoot(String root, Connection conn){
        root = root.split("/var/")[1];
        char[] temp = root.toCharArray();
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < temp.length; ++i) {
            if (temp[i] == '/') temp[i] = '\\';
            stb.append(temp[i]);
        }
        root = stb.toString();
        String var="";
        try {
            String sql = "SHOW VARIABLES LIKE 'character_sets_dir'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.next();
            var = rs.getString(2);
            var = var.split("share")[0];
        }catch (SQLException e){
            JOptionPane.showMessageDialog(
                    null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return var+root;
    }
}
