package Optimization.ParameterOpt.View;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class CaseCustomize {

    public void frameDriver(Connection conn){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();

        panel.setLayout(new GridLayout(0,1,0,0));

        String[] paraName={"join_buffer_size","read_buffer_size","sort_buffer_size","key_buffer_size"};
        JTextArea[] jTextArea=new JTextArea[4];

        panel.add(new JLabel("若只需要使用一个测试语句，填写第一个参数语句提交即可"));

        for(int i=0;i<4;++i){
            panel.add(new JLabel(paraName[i]));
            jTextArea[i]=new JTextArea();
            panel.add(jTextArea[i]);
        }

        JButton confirm=new JButton("提交");

        confirm.addActionListener(event->{
            String[] sql=new String[4];
            for(int i=0;i<4;++i){
                sql[i]=jTextArea[i].getText();
            }
            ParaEvaluation paraEvaluation=new ParaEvaluation();
            paraEvaluation.autoTest(conn,sql);
        });

        JCheckBox ifSave=new JCheckBox();
        ifSave.setText("是否保存当前用例语句，以便下次快捷使用");

        panel.add(ifSave);
        panel.add(confirm);

        jFrame.add(panel);
        jFrame.setSize(400,650);
        jFrame.setTitle("评估结果");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
