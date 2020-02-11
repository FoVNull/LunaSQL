package Starter;

import Log.LogListener.SettingEdit;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ParameterView {

    public void paraDriver(Connection conn){
        SettingEdit se=new SettingEdit();
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,2,1,0));

        JButton ini=new JButton("打开配置文件");
        ini.addActionListener(event->{
            se.openIni(conn);
        });

        panel.add(ini);

        jFrame.add(panel);
        jFrame.setSize(500,450);
        jFrame.setTitle("日志控制台");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
