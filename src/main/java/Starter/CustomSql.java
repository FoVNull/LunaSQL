package Starter;

import MysqlOperation.domin.Customize;
import Starter.ViewFrame;


import javax.swing.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomSql{
    private JFrame frame=new JFrame();
    private JPanel panel;
    private JTextArea sql;
    private JButton excute;
    public void customSqlDriver(Connection conn){
        panel=new JPanel();
        sql=new JTextArea(5,40);
        sql.setTabSize(4);
        sql.setLineWrap(true);        //激活自动换行功能
        sql.setWrapStyleWord(true); // 激活断行不断字功能
        excute=new JButton("执行");
        panel.add(sql);
        panel.add(excute);
        frame.add(panel);


        excute.addActionListener(event->{
            Customize customize=new Customize();
            try {
                ResultSet temp = customize.excuteSql(conn, sql.getText());
                ViewFrame.rs = temp;
                if (temp != null) ViewFrame.cusListener.doClick();
                frame.dispose();
            }
            catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });


        frame.setSize(450,170);
        frame.setTitle("自定义SQL");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
