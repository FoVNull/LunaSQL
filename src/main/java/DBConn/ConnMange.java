package DBConn;

import Optimization.Evaluation.PerformanceTest.View.MysqlSlap;
import Optimization.SqlOptimize.View.SoarConsole;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class ConnMange {
    ReadInfo ri=new ReadInfo();
    public void driver(){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        JPanel cusPanel=new JPanel();

        panel.setLayout(new GridLayout(0,4,0,0));
        panel.add(new JLabel("名称"));
        panel.add(new JLabel(":端口"));
        panel.add(new JLabel("用户名"));
        panel.add(new JLabel());

        JSONArray dbList= ri.readInfo();
        int len=dbList.length();
        JRadioButton[] buttons=new JRadioButton[dbList.length()+1];
        ButtonGroup group = new ButtonGroup();
        for(int i=0;i<len;++i) {
            String connN=dbList.getJSONObject(i).getString("connName");
            String url=dbList.getJSONObject(i).getString("url");
            String username=dbList.getJSONObject(i).getString("userName");

            panel.add(new JLabel(connN));panel.add(new JLabel(url));panel.add(new JLabel(username));
            buttons[i]=new JRadioButton();
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }
        panel.setBounds(0,0,500,len*50);

        JButton cusButton=new JButton("删除");
        cusPanel.add(new JLabel());cusPanel.add(new JLabel());cusPanel.add(new JLabel());

        cusButton.addActionListener(event->{
            for (int i = 0; i < len; ++i) {
                if (buttons[i].isSelected()) {
                    SaveInfo si=new SaveInfo();
                    si.deleteConn(i);
                    break;
                }
            }
            jFrame.dispose();
        });

        cusPanel.add(cusButton);
        cusButton.setBounds(10,0,100,25);
        cusPanel.setBounds(0,len*50+20,500,80);
        cusPanel.setLayout(null);

        jFrame.add(panel);
        jFrame.add(cusPanel);
        jFrame.setLayout(null);
        jFrame.setSize(550,len*50+100);
        jFrame.setTitle("管理连接");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
