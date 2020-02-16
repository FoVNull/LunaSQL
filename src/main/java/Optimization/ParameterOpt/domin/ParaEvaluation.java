package Optimization.ParameterOpt.domin;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParaEvaluation {

    public void autoTest(Connection conn){

        String[] sql={"SELECT * FROM information_schema.`COLUMNS` c JOIN information_schema.`TABLES` t ON c.TABLE_NAME=t.TABLE_NAME",
                "SELECT * FROM information_schema.`COLUMNS`"
        };

        TestThread testThread=new TestThread();
        MainThread mThread=new MainThread(conn,sql);
        mThread.start();
        //showRes(testThread.initTest(sql,conn));
    }

    public void showRes(LinkedHashMap<Integer, int[]> value){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,3,1,0));
        String[] info={"join_buffer_size","read_buffer_size"};
        int[] input=new int[4];//记录本次结果
        for(int i=0;i<2;++i){
            panel.add(new JLabel(info[i]));
            int[] temp=value.get(i);
            int minus=temp[0]-temp[1];
            input[i]=temp[1];

            JLabel jLabel=new JLabel();
            if(minus>0) {
                jLabel.setText("(-"+minus+")");
                jLabel.setForeground(Color.GREEN);
            }else if(minus<0) {
                jLabel.setText("(+" + minus + ")");
                jLabel.setForeground(Color.red);
            }else{
                jLabel.setText("(" + minus + ")");
                jLabel.setForeground(Color.orange);
            }
            panel.add(new JLabel(temp[1]+""));
            panel.add(jLabel);
        }

        EvaluationIO eio=new EvaluationIO();
        eio.writeLast(input);

        jFrame.add(panel);
        jFrame.setSize(500,350);
        jFrame.setTitle("评估结果");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
