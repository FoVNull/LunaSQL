package Optimization.ParameterOpt.domin;


import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class TestThread{
    int i;

    public LinkedHashMap<Integer,int[]> initTest(String[] sql,Connection conn){
        LinkedHashMap<Integer,int[]> res=new LinkedHashMap<>();
        for(int j=0;j<sql.length;++j){
            int[] temp=run(conn,sql[j],j);
            res.put(j,temp);
        }
        return res;
    }

    public int[] run(Connection conn,String sql,int type){
        int[] res=new int[2];
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
        for(i=0;i<100;++i) {
            jpb.setValue(i);
            try {
                long x = System.currentTimeMillis();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.executeQuery();
                long y = System.currentTimeMillis();
                res[1]+=y-x;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        i=0;
        res[1]=res[1]/100;
        EvaluationIO eio=new EvaluationIO();
        String[] t=eio.readLast();
        res[0]=Integer.parseInt(t[type]);
        jFrame.dispose();
        return res;
    }
}
