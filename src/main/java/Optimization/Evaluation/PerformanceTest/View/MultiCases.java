package Optimization.Evaluation.PerformanceTest.View;

import MysqlOperation.domin.Script;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiCases {
    public void driver(Connection conn){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();

        JTextArea sql=new JTextArea();
        sql.setLineWrap(true);        //激活自动换行功能
        sql.setWrapStyleWord(true); // 激活断行不断字功能
        JButton commit=new JButton("提交");
        JButton script=new JButton("导入sql脚本");

        panel.setLayout(null);

        JLabel info=new JLabel("sql语句之间使用分号间隔");

        panel.setBounds(0,0,500,400);
        info.setBounds(0,5,200,40);
        sql.setBounds(10,60,400,250);
        script.setBounds(420,270,100,30);
        commit.setBounds(230,320,60,30);

        panel.add(info);
        panel.add(sql);

        commit.addActionListener(event->{
            if(sql.getText().equals("")){
                JOptionPane.showMessageDialog(null,"内容不能为空",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }else {
                List<Character> temp = new ArrayList<>();

                char[] chars = sql.getText().toCharArray();
                for (char c : chars) temp.add(c);

                temp = temp.stream().filter(x -> x != 10).collect(Collectors.toList());

                StringBuilder sb = new StringBuilder();
                for (char c : temp) sb.append(c);

                String[] input = sb.toString().split(";");

                ParaEvaluation paraEvaluation = new ParaEvaluation();
                paraEvaluation.autoTest(conn, input, 2);
                jFrame.dispose();
            }
        });

        script.addActionListener(event->{
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter fft=new FileFilter(){
                @Override
                public boolean accept(File file){
                    return file.getName().endsWith("sql");
                }

                @Override
                public String getDescription(){
                    return "需要sql文件(*.sql)";
                }
            };

            jfc.setFileFilter(fft);
            jfc.showOpenDialog(null);
            File inputPath=jfc.getSelectedFile();
            if(inputPath!=null) {
                Script scriptR = new Script();
                String scriptText=scriptR.readScript(inputPath);
                sql.setText(scriptText);
            }
        });

        panel.add(commit);
        panel.add(script);

        jFrame.add(panel);
        jFrame.setSize(550,400);
        jFrame.setTitle("多语句/sql脚本");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
