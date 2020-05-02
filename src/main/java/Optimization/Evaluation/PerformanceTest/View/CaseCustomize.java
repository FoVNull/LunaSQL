package Optimization.Evaluation.PerformanceTest.View;

import Optimization.Evaluation.PerformanceTest.domin.EvaluationIO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class CaseCustomize {

    public void frameDriver(Connection conn,String cusName){
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();

        panel.setLayout(new GridLayout(0,1,0,0));

        String[] paraName={"  join_buffer_size","  read_buffer_size","  sort_buffer_size","  key_buffer_size"};
        JTextArea[] jTextArea=new JTextArea[4];

        panel.add(new JLabel("  语句可以为空，格式为\"语句@执行次数\"(默认100)"));

        EvaluationIO eios=new EvaluationIO();
        String[] userCase=eios.readUserCase();

        for(int i=0;i<4;++i){
            panel.add(new JLabel(paraName[i]));
            jTextArea[i]=new JTextArea();
            jTextArea[i].setLineWrap(true);        //激活自动换行功能
            jTextArea[i].setWrapStyleWord(true); // 激活断行不断字功能
            if(!userCase[i].equals("NAN")) jTextArea[i].setText(userCase[i]);
            panel.add(jTextArea[i]);
        }

        JButton confirm=new JButton("提交");
        JCheckBox ifSave=new JCheckBox();
        ifSave.setText("是否保存当前用例语句，以便下次快捷使用");
        ifSave.setSelected(true);


        confirm.addActionListener(event->{
            int flag=0;
            String[] sql=new String[4];
            for(int i=0;i<4;++i){
                sql[i]=jTextArea[i].getText();
                if(sql[i].equals("")) {++flag; sql[i]="NAN";};
            }
            if(flag==4){
                JOptionPane.showMessageDialog(null,"请至少输入1条语句",
                        "语句为空",JOptionPane.ERROR_MESSAGE);
            }else {
                ParaEvaluation paraEvaluation = new ParaEvaluation();
                paraEvaluation.autoTest(conn, sql,1,cusName);

                if(ifSave.isSelected()){
                    EvaluationIO eio=new EvaluationIO();
                    eio.saveUserCase(sql);
                }

                jFrame.dispose();

            }

        });

        panel.add(ifSave);
        panel.add(confirm);

        JButton setDefault=new JButton("确认默认语句并提交运行");
        JTextArea defaultSQL=new JTextArea();

        setDefault.addActionListener(event->{
            ParaEvaluation paraEvaluation = new ParaEvaluation();
            paraEvaluation.autoTest(conn, new String[]{defaultSQL.getText()},0,cusName);
            EvaluationIO eio=new EvaluationIO();
            eio.setDefault(defaultSQL.getText());
            jFrame.dispose();
        });
        panel.add(new JLabel("  修改默认用例↓"));
        panel.add(defaultSQL);panel.add(setDefault);

        jFrame.add(panel);
        jFrame.setSize(700,600);
        jFrame.setTitle("自定义用例");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
