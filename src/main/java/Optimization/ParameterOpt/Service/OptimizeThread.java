package Optimization.ParameterOpt.Service;

import Optimization.ParameterOpt.domin.EvaluationIO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OptimizeThread extends Thread {
    int i;int flag=0;
    Connection conn; String sql ;int type;

    public OptimizeThread(Connection conn,String sql,int type){
        this.conn=conn;this.sql=sql;this.type=type;
    }

    @Override
    public void run(){
        flag=0;
        JProgressBar jpb=new JProgressBar(0,100);
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        JButton cancel=new JButton("终止");
        panel.add(new JLabel("请稍后..."));
        cancel.addActionListener(event->{
            i=200;flag=1;MainThread.stopFlag=true;
            jFrame.dispose();
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
            if(MainThread.stopFlag) jFrame.dispose();
            jpb.setValue(i);
            try {
                long x = System.currentTimeMillis();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.executeQuery();
                long y = System.currentTimeMillis();
                n+=y-x;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                break;
            }
        }
        i=0;
        n/=100;

        jFrame.dispose();
        MainThread.sumOfMulti+=n;
    }
}
