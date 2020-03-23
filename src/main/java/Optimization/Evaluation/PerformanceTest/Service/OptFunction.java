package Optimization.Evaluation.PerformanceTest.Service;


import Optimization.Evaluation.ParameterOpt.domin.EvaluationIO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class OptFunction {
    int i;int flag=0;

    public LinkedHashMap<Integer,String[]> initTest(String[] sql,Connection conn,int testType){
        LinkedHashMap<Integer,String[]> res=new LinkedHashMap<>();
        for(int j=0;j<sql.length;++j){
            if(!sql[j].equals("NAN")) {
                String[] temp = run(conn, sql[j], j);
                if(flag==1) {res.clear();return res;}
                res.put(j, temp);
            }
        }
        if(testType==0){
            for(int i=1;i<4;++i) res.put(i,res.get(0));
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
            i=200;flag=1;
        });
        panel.add(jpb);
        panel.add(cancel);
        jFrame.add(panel);
        jFrame.setSize(275,100);
        int typex=type+1;
        jFrame.setTitle("用例 "+typex);
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
                JOptionPane.showMessageDialog(null,e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
                flag=1;
                break;
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
