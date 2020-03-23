package Optimization.SqlOptimize.View;

import MysqlOperation.domin.Script;
import Optimization.SqlOptimize.domin.SoarLog;
import Optimization.SqlOptimize.Service.SoarOperation;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class SoarConsole {
    public static JSONObject defaultDB=null;
    public static JButton dbListen=new JButton();
    public void consoleDriver(){
        SoarOperation sop=new SoarOperation();
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(null);
        JPanel funcPanel=new JPanel();
        funcPanel.setLayout(null);

        JLabel text1=new JLabel("sql语句输入");
        JLabel dbInfo=new JLabel("测试库:");
        JButton dbSetting=new JButton("配置测试库");
        JCheckBox useDB=new JCheckBox();
        useDB.setText("在测试库中运行评估和重写");
        text1.setBounds(10,10,100,50);
        dbInfo.setBounds(200,10,200,50);
        dbSetting.setBounds(400,25,100,25);
        useDB.setBounds(500,25,180,25);

        JTextArea sql=new JTextArea();
        sql.setBounds(10,60,600,200);
        sql.setLineWrap(true);        //激活自动换行功能
        sql.setWrapStyleWord(true); // 激活断行不断字功能
        JButton script=new JButton("导入sql脚本");

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

        JPanel toolBar=new JPanel();
        toolBar.setLayout(new GridLayout(0,3,0,0));
        toolBar.setBounds(0,20,750,100);
        String[] toolName={"评估语句","sql重写","sql美化","合并多条ALTER","优化日志"};
        JButton[] buttons=new JButton[toolName.length];

        toolBar.add(script);

        buttons[0]=new JButton(toolName[0]);
        toolBar.add(buttons[0]);
        for(int i=1;i<toolName.length;++i){
            buttons[i]=new JButton(toolName[i]);
            toolBar.add(buttons[i]);
        }

        JScrollPane scrollRes=new JScrollPane();
        scrollRes.setBounds(0,350,740,370);
        JTextArea result=new JTextArea();
        result.setEditable(false);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        scrollRes.setViewportView(result);

        buttons[0].addActionListener(event->{
            if(useDB.isSelected()){
                if(defaultDB==null){
                    JOptionPane.showMessageDialog(null,"请先选择测试库",
                            "提示",JOptionPane.WARNING_MESSAGE);
                    dbSetting.doClick();
                }else{
                    String res=sop.evaluateSQLWithDB(sql.getText(),defaultDB);
                    result.append(res);
                }
            }else {
                String res = sop.evaluateSQL(sql.getText());
                result.append(res);
            }
        });
        dbListen.addActionListener(event->{
            dbInfo.setText("测试库："+defaultDB.getString("url")+"/"+defaultDB.getString("schema")
                    +"@"+defaultDB.getString("userName"));
        });
        dbSetting.addActionListener(event->{
            SetDataBase sdb=new SetDataBase();
            sdb.driver("soar");
        });
        buttons[1].addActionListener(event->{
            if(useDB.isSelected()) {
                if (defaultDB == null) {
                    JOptionPane.showMessageDialog(null, "请先选择测试库",
                            "提示", JOptionPane.WARNING_MESSAGE);
                    dbSetting.doClick();
                } else {
                    String res = sop.rewriteSQLWithDB(sql.getText(), defaultDB);
                    result.append(res);
                }
            }else {
                String res = sop.rewriteSQL(sql.getText());
                result.append(res);
            }
        });
        buttons[2].addActionListener(event->{
            String res=sop.prettySQL(sql.getText());
            result.append(res);
        });
        buttons[3].addActionListener(event->{
            String res=sop.mergeAlter(sql.getText());
            result.append(res);
        });
        buttons[4].addActionListener(event->{
            SoarLog sl=new SoarLog();
            sl.readLog();
        });

        panel.add(text1);
        panel.add(dbInfo);
        panel.add(dbSetting);
        panel.add(useDB);
        panel.add(sql);
        funcPanel.add(toolBar);

        jFrame.add(panel);jFrame.add(funcPanel);
        jFrame.add(scrollRes);
        panel.setBounds(0,0,750,200);
        funcPanel.setBounds(0,200,740,150);

        jFrame.setLayout(null);
        jFrame.setSize(750,770);
        jFrame.setTitle("sql优化");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
