package Optimization.Evaluation.PerformanceTest.View;

import Optimization.Evaluation.PerformanceTest.Service.MainThread;
import Optimization.Evaluation.PerformanceTest.domin.EvaluationIO;
import Optimization.Evaluation.ParameterOpt.domin.SettingEdit;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ParaEvaluation {

    public void autoTest(Connection conn,String[] sql,int testType){

        MainThread mThread=new MainThread(conn,sql,testType);
        mThread.start();
    }

    public void showRes(HashMap<Integer, String[]> value,Connection conn){
        for(int i=0;i<4;++i){
            if(!value.containsKey(i)) value.put(i,new String[]{"0","0","null"});
        }

        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,4,1,0));
        panel.add(new JLabel("若使用的是默认模式："));
        panel.add(new JLabel("所有值相同"));panel.add(new JLabel());panel.add(new JLabel());
        panel.add(new JLabel("参数名称"));
        panel.add(new JLabel("用例测试时间(ms)"));
        panel.add(new JLabel("较上次测试变化(ms)"));
        panel.add(new JLabel("上次测试时间"));

        int[] input=new int[8];//记录本次结果

        SettingEdit se=new SettingEdit();
        Map<String,String> paraValue=se.paraQuery(conn);
        String[] paraName={"join_buffer_size","read_buffer_size","sort_buffer_size","key_buffer_size"};

        for(int i=0;i<4;++i){
            panel.add(new JLabel(paraName[i]));
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

            input[i+4]=Integer.parseInt(paraValue.get(paraName[i]))/1024;

        }


        EvaluationIO eio=new EvaluationIO();
        eio.writeLast(input);

        panel.add(new JLabel("<html>参数值可在历史记录中查询</html>"));
        panel.add(new JLabel());

        JButton open=new JButton("查看历史记录");
        open.addActionListener(event->{
            eio.checkHistory();
        });

        panel.add(open);

        jFrame.add(panel);
        jFrame.setSize(600,250);
        jFrame.setTitle("评估结果");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}
