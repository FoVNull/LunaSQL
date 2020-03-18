package MysqlOperation.domin;


import MysqlOperation.Entity.CusRes;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.HashMap;

public class Customize {

    public CusRes excuteSql(Connection conn, String sql){
        CusRes cr=new CusRes();
        ResultSet rs=null;
        try {
            PreparedStatement pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            cr.setCusRes(0,rs,null);
        }
        catch (SQLException ex) {
            StringWriter sw=new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            if(sw.toString().contains("with executeQuery")){
                try {
                    PreparedStatement pst=conn.prepareStatement(sql);
                    pst.executeUpdate();
                    cr.setCusRes(0,null,null);
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(null,e.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);
                    cr.setCusRes(2,null,e);
                }
            }
            else{
                JOptionPane.showMessageDialog(null,ex.getMessage(),
                        "Error",JOptionPane.ERROR_MESSAGE);
                cr.setCusRes(2,null,ex);
            }
        }
        return cr;
    }
}
