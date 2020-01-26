package Starter;

import Log.LogListener.LogStatus;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Connection;

public class LogConsoleView {
    private JFrame jFrame=new JFrame();
    private JPanel panel=new JPanel();
    LogStatus logStatus=new LogStatus();
    JLabel gStatus=new JLabel();
    JLabel gStorage=new JLabel();
    JLabel sStatus=new JLabel();

    public void logConsoleDriver(Connection conn){
        panel.setLayout(new GridLayout(0,1,0,0));
        panel.add(new JLabel("general_log状态"));
        JSONObject jsonObject =logStatus.searchGLog(conn);
        if(jsonObject.get("status").equals("ON")) gStatus.setText("状态：开启");
        else gStatus.setText("状态：关闭");
        gStorage.setText("存储方式："+jsonObject.getString("storage"));
        panel.add(gStatus);
        panel.add(gStorage);
        panel.add(new JLabel("日志路径："+jsonObject.getString("location")));
        panel.add(new JButton("更改状态"));

        panel.add(new JLabel("slow_log状态"));
        jsonObject=logStatus.searchSLog(conn);
        if(jsonObject.get("status").equals("ON")) sStatus.setText("状态：开启");
        else sStatus.setText("状态：关闭");
        panel.add(sStatus);
        panel.add(new JLabel("慢日志时间限制："+jsonObject.getString("time")+"s"));
        panel.add(new JLabel("日志路径："+jsonObject.getString("location")));
        panel.add(new JButton("更改状态"));

        String errLoc=logStatus.searchELog(conn);
        panel.add(new JLabel("错误日志路径："+errLoc));

        jFrame.add(panel);
        jFrame.setSize(500,400);
        jFrame.setTitle("日志控制台");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }

}
