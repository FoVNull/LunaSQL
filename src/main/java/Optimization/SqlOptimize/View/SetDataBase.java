package Optimization.SqlOptimize.View;

import DBConn.ReadInfo;
import Optimization.Evaluation.PerformanceTest.View.MysqlSlap;
import Optimization.SqlOptimize.View.SoarConsole;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class SetDataBase {

    public void driver(String type){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        JPanel cusPanel=new JPanel();

        panel.setLayout(new GridLayout(0,5,0,0));
        panel.add(new JLabel("名称"));
        panel.add(new JLabel(":端口"));
        panel.add(new JLabel("用户名"));
        panel.add(new JLabel("模式(数据库)"));
        panel.add(new JLabel());

        JSONArray dbList= ReadInfo.readInfo();
        int len=dbList.length();
        JRadioButton[] buttons=new JRadioButton[dbList.length()+1];
        JTextField[] schemaName=new JTextField[dbList.length()];
        ButtonGroup group = new ButtonGroup();
        for(int i=0;i<len;++i) {
            String connN=dbList.getJSONObject(i).getString("connName");
            String url=dbList.getJSONObject(i).getString("url");
            String username=dbList.getJSONObject(i).getString("userName");
            schemaName[i]=new JTextField();

            panel.add(new JLabel(connN));panel.add(new JLabel(url));panel.add(new JLabel(username));
            panel.add(schemaName[i]);
            buttons[i]=new JRadioButton();
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }
        panel.setBounds(0,0,600,len*50);

        cusPanel.add(new JLabel("自定义测试库"));
        buttons[len]=new JRadioButton();
        group.add(buttons[len]);
        cusPanel.add(buttons[len]);cusPanel.add(new JLabel());cusPanel.add(new JLabel());
        cusPanel.add(new JLabel());cusPanel.add(new JLabel());

        cusPanel.add(new JLabel("地址:端口"));
        cusPanel.add(new JLabel("用户名："));
        cusPanel.add(new JLabel("密码："));
        cusPanel.add(new JLabel("模式(数据库)："));
        cusPanel.add(new JLabel());cusPanel.add(new JLabel());

        JTextField[] cusInfo=new JTextField[4];
        for(int i=0;i<cusInfo.length;++i) {
            cusInfo[i]=new JTextField();
            cusPanel.add(cusInfo[i]);
        }
        cusPanel.add(new JLabel());
        JButton cusButton=new JButton("确认");

        cusButton.addActionListener(event->{
            JSONObject object=null;
            if(buttons[len].isSelected()){
                object=new JSONObject();
                object.put("url", cusInfo[0].getText());
                object.put("userName", cusInfo[1].getText());
                object.put("psw", cusInfo[2].getText());
                object.put("schema", cusInfo[3].getText());
                SoarConsole.defaultDB=object;
                SoarConsole.dbListen.doClick();
                jFrame.dispose();
            }else {
                for (int i = 0; i < len; ++i) {
                    if (buttons[i].isSelected() && !schemaName[i].getText().equals("")) {
                        object = dbList.getJSONObject(i);
                        object.put("schema", schemaName[i].getText());
                        if(type.equals("soar")) {
                            SoarConsole.defaultDB = object;
                            SoarConsole.dbListen.doClick();
                        }else{
                            MysqlSlap.defaultDB = object;
                            MysqlSlap.dbListen.doClick();
                        }
                        jFrame.dispose();break;
                    }
                }
            }
            if(object==null){
                JOptionPane.showMessageDialog(null,"模式为空或未选择数据库"
                        ,"Error",JOptionPane.ERROR_MESSAGE);
            }else{object=null;}
        });

        cusPanel.add(cusButton);
        cusPanel.setBounds(0,len*50+25,600,100);
        cusPanel.setLayout(new GridLayout(3,0,0,0));

        jFrame.add(panel);
        jFrame.add(cusPanel);
        jFrame.setLayout(null);
        jFrame.setSize(650,len*50+200);
        jFrame.setTitle("选择测试库");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
