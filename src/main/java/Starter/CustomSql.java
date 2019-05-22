package Starter;

import MysqlOperation.domin.Customize;
import Starter.ViewFrame;
import com.mysql.jdbc.Connection;

import javax.swing.*;

public class CustomSql{
    private JFrame frame=new JFrame();
    private JPanel panel;
    private JTextArea sql;
    private JButton excute;
    public void customSqlDriver(Connection conn){
        panel=new JPanel();
        sql=new JTextArea(25,58);
        sql.setTabSize(4);
        excute=new JButton("执行");
        panel.add(sql);
        panel.add(excute);
        frame.add(panel);

        excute.addActionListener(event->{
            Customize customize=new Customize();
            try {
                ViewFrame.rs= customize.excuteSql(conn, sql.getText());
                ViewFrame.cusListener.doClick();
                frame.dispose();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });


        frame.setSize(700,500);
        frame.setTitle("自定义SQL");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
