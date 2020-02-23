package Optimization.ParameterOpt.View;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiCases {
    public void driver(Connection conn){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();

        JTextArea sql=new JTextArea();
        JButton commit=new JButton("提交");
        JButton script=new JButton("使用sql脚本");

        panel.setLayout(new GridLayout(0,1,0,0));

        panel.add(new JLabel("sql语句直接使用分号分隔"));
        panel.add(sql);

        commit.addActionListener(event->{
            List<Character> temp=new ArrayList<>();

            char[] chars=sql.getText().toCharArray();
            for(char c:chars) temp.add(c);

            temp=temp.stream().filter(x->x!=10).collect(Collectors.toList());

            StringBuilder sb=new StringBuilder();
            for(char c:temp) sb.append(c);

            String[] input=sb.toString().split(";");

            ParaEvaluation paraEvaluation=new ParaEvaluation();
            paraEvaluation.autoTest(conn,input,2);
            jFrame.dispose();
        });

        script.addActionListener(event->{

        });

        panel.add(commit);
        panel.add(script);

        jFrame.add(panel);
        jFrame.setSize(500,400);
        jFrame.setTitle("多语句/sql脚本");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
