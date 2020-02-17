package Optimization.ParameterOpt.Service;


import Optimization.ParameterOpt.domin.EvaluationIO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class TestThread{
    int i;

    public LinkedHashMap<Integer,String[]> initTest(String[] sql,Connection conn){
        LinkedHashMap<Integer,String[]> res=new LinkedHashMap<>();
        for(int j=0;j<sql.length;++j){
            String[] temp=run(conn,sql[j],j);
            res.put(j,temp);
        }
        return res;
    }

    public String[] run(Connection conn,String sql,int type){
        String[] res=new String[3];
        JProgressBar jpb=new JProgressBar(0,100);
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        JButton cancel=new JButton("终止");
        panel.add(new JLabel("请稍后..."));
        cancel.addActionListener(event->{
            i=200;jFrame.dispose();
        });
        panel.add(jpb);
        panel.add(cancel);
        jFrame.add(panel);
        jFrame.setSize(275,100);
        int typex=type+1;
        jFrame.setTitle("参数 "+typex+"/4");
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        int n=0;
        for(i=0;i<100;++i) {
            jpb.setValue(i);
            try {
                long x = System.currentTimeMillis();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.executeQuery();
                long y = System.currentTimeMillis();
                n+=y-x;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        i=0;
        n/=100;
        res[1]=n+"";
        EvaluationIO eio=new EvaluationIO();
        String[] t=eio.readLast();
        res[0]=t[type];
        res[2]=t[4];
        jFrame.dispose();
        return res;
    }
}
