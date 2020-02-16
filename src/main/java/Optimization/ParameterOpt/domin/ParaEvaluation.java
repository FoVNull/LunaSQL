package Optimization.ParameterOpt.domin;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.LinkedHashMap;

public class ParaEvaluation {

    public void autoTest(Connection conn){

        String[] sql={"SELECT * FROM information_schema.`COLUMNS` c JOIN information_schema.`TABLES` t ON c.TABLE_NAME=t.TABLE_NAME",
                "SELECT * FROM information_schema.`COLUMNS`"
        };

        MainThread mThread=new MainThread(conn,sql);
        mThread.start();
    }

    public void showRes(LinkedHashMap<Integer, String[]> value){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,4,1,0));
        panel.add(new JLabel("参数名称"));
        panel.add(new JLabel("用例测试时间(ms)"));
        panel.add(new JLabel("较上次测试变化(ms)"));
        panel.add(new JLabel("上次测试时间"));

        String[] info={"join_buffer_size","read_buffer_size"};
        int[] input=new int[4];//记录本次结果
        for(int i=0;i<2;++i){
            panel.add(new JLabel(info[i]));
            String[] temp=value.get(i);
            int minus=Integer.parseInt(temp[0])-Integer.parseInt(temp[1]);
            input[i]=Integer.parseInt(temp[1]);

            JLabel jLabel=new JLabel();
            if(minus>0) {
                jLabel.setText("(-"+minus+")");
                jLabel.setForeground(Color.GREEN);
            }else if(minus<0) {
                jLabel.setText("(+" + Math.abs(minus) + ")");
                jLabel.setForeground(Color.red);
            }else{
                jLabel.setText("(" + minus + ")");
                jLabel.setForeground(Color.orange);
            }
            panel.add(new JLabel(temp[1]+""));
            panel.add(jLabel);
            panel.add(new JLabel(temp[2]));
        }

        EvaluationIO eio=new EvaluationIO();
        eio.writeLast(input);

        jFrame.add(panel);
        jFrame.setSize(600,250);
        jFrame.setTitle("评估结果");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
