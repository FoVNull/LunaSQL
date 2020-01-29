package Starter;

import Log.LogListener.LogStatus;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.Connection;

public class LogConsoleView {
    String gcStr="";String scStr="";

    public void logConsoleDriver(Connection conn){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        LogStatus logStatus=new LogStatus();
        JLabel gStatus=new JLabel("",JLabel.CENTER);
        JLabel gStorage=new JLabel("",JLabel.CENTER);
        JLabel sStatus=new JLabel("",JLabel.CENTER);
        JButton gButton=new JButton("查询日志开关");
        JButton gEditB=new JButton("保存修改");
        JButton sButton=new JButton("慢日志开关");
        JButton sEditB=new JButton("保存修改");

        String[] gsType={"TABLE","FILE","FILE,TABLE"};
        JComboBox<String> gType=new JComboBox(gsType);

        JTextField gRoot=new JTextField();
        JTextField sTime=new JTextField();
        JTextField sRoot=new JTextField();

        panel.setLayout(new GridLayout(0,2,1,0));

        panel.add(new JLabel("查询日志状态"));panel.add(new JLabel());

        JSONObject jsonObject =logStatus.searchGLog(conn);
        if(jsonObject.get("status").equals("ON")) {gStatus.setText("状态：开启");gcStr="OFF";}
        else {gStatus.setText("状态：关闭");gcStr="ON";}

        String type=jsonObject.getString("storage");
        gStorage.setText("存储方式："+type);

        panel.add(gStatus);panel.add(gButton);
        switch (type){
            case "TABLE":gType.setSelectedIndex(0);break;
            case "FILE":gType.setSelectedIndex(1);break;
            case "FILE,TABLE":gType.setSelectedIndex(2);break;
        }
        panel.add(gStorage);panel.add(gType);

        panel.add(new JLabel("日志路径（修改路径时使用‘/’）：",JLabel.CENTER));
        gRoot.setText(jsonObject.getString("location"));
        panel.add(gRoot);

        gButton.addActionListener(event->{
            logStatus.editGLog(conn,gcStr);
            jFrame.dispose();
            logConsoleDriver(conn);
        });
        gEditB.addActionListener(event->{
            logStatus.editGInfo(conn,gType.getSelectedItem().toString(),gRoot.getText());
            jFrame.dispose();
            logConsoleDriver(conn);
        });
        panel.add(new JLabel());panel.add(gEditB);


        panel.add(new JLabel("慢日志状态"));panel.add(new JLabel());
        jsonObject=logStatus.searchSLog(conn);
        if(jsonObject.get("status").equals("ON")) {sStatus.setText("状态：开启");scStr="OFF";}
        else {sStatus.setText("状态：关闭");scStr="ON";}
        panel.add(sStatus); panel.add(sButton);
        panel.add(new JLabel("慢日志时间限制(s)：",JLabel.CENTER));
        sTime.setText(jsonObject.getString("time"));
        panel.add(sTime);
        panel.add(new JLabel("日志路径（修改路径时使用‘/’）：",JLabel.CENTER));
        sRoot.setText(jsonObject.getString("location"));
        panel.add(sRoot);

        sEditB.addActionListener(event->{
            logStatus.editSInfo(conn,sTime.getText(),sRoot.getText());
            jFrame.dispose();
            logConsoleDriver(conn);
        });
        sButton.addActionListener(event->{
            logStatus.editSLog(conn,scStr);
            jFrame.dispose();
            logConsoleDriver(conn);
        });
        panel.add(new JLabel());panel.add(sEditB);
        panel.add(new JLabel());panel.add(new JLabel());

        String errLoc=logStatus.searchELog(conn);
        panel.add(new JLabel("错误日志路径(请在my.ini中修改)：",JLabel.CENTER));
        panel.add(new JScrollPane(new JLabel(errLoc)));

        jFrame.add(panel);
        jFrame.setSize(500,450);
        jFrame.setTitle("日志控制台");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
