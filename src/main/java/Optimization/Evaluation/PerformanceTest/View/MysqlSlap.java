package Optimization.Evaluation.PerformanceTest.View;

import MysqlOperation.domin.Script;
import Optimization.Evaluation.PerformanceTest.Service.SlapCommand;
import Optimization.Evaluation.PerformanceTest.domin.SlapLog;
import Optimization.SqlOptimize.View.SetDataBase;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class MysqlSlap {
    public static JSONObject defaultDB=null;
    public static JButton dbListen=new JButton();
    public void consoleDriver(){
        SlapCommand sc=new SlapCommand();
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(null);
        JPanel funcPanel=new JPanel();
        funcPanel.setLayout(null);

        JLabel text1=new JLabel("测试语句输入(关键字请使用小写)");
        JLabel dbInfo=new JLabel("测试库:");
        JButton dbSetting=new JButton("配置测试库");
        JButton submit=new JButton("执行");
        JButton log=new JButton("日志");
        JButton load=new JButton("导入sql文件");
        text1.setBounds(10,10,200,50);
        dbInfo.setBounds(200,10,200,50);
        dbSetting.setBounds(400,25,100,25);
        submit.setBounds(520,25,80,25);
        log.setBounds(620,25,70,25);
        load.setBounds(620,75,100,25);

        JTextArea sql=new JTextArea();
        sql.setBounds(10,60,600,100);
        sql.setLineWrap(true);        //激活自动换行功能
        sql.setWrapStyleWord(true); // 激活断行不断字功能

        dbListen.addActionListener(event->{
            dbInfo.setText("测试库："+defaultDB.getString("url")+"/"+defaultDB.getString("schema")
                    +"@"+defaultDB.getString("userName"));
        });
        dbSetting.addActionListener(event->{
            SetDataBase sdb=new SetDataBase();
            sdb.driver("slap");
        });
        log.addActionListener(event->{
            SlapLog sl=new SlapLog();
            sl.readLog();
        });
        load.addActionListener(event->{
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
                sql.setText(inputPath.toString());
            }
        });

        JPanel toolBar=new JPanel();
        toolBar.setLayout(new GridLayout(0,2,0,0));
        toolBar.setBounds(0,20,730,200);

        //mysqlslap选项
        JCheckBox autoGen=new JCheckBox("自动生成测试脚本(如果要测试已有模式请勿勾选！)");
        toolBar.add(autoGen);toolBar.add(new JLabel("注意，自动测试会自动创建您填写的模式，结束后会删除该模式！"));

        String[] sqlType={"mixed","read","key","write","update"};
        JComboBox<String> type=new JComboBox(sqlType);
        type.setSelectedItem(0);
        toolBar.add(new JLabel("自定义用例类型(默认为mixed)"));
        toolBar.add(type);

        JTextField concurrency=new JTextField();
        toolBar.add(new JLabel("并发数量(如有多个用逗号隔开，默认为1)"));
        toolBar.add(concurrency);

        JTextField queryNum=new JTextField();
        toolBar.add(new JLabel("查询总数(查询总数/并发数=并发中每个线程执行的次数)"));
        toolBar.add(queryNum);

        JTextField iterations=new JTextField();
        toolBar.add(new JLabel("全部测试执行次数(默认为1)"));
        toolBar.add(iterations);

        JTextField engine =new JTextField();
        toolBar.add(new JLabel("测试引擎(如有多个用逗号隔开)"));
        toolBar.add(engine);

        JCheckBox debug=new JCheckBox("打印内存和CPU的相关信息");
        debug.setVisible(false);//debug功能暂时无法使用
        toolBar.add(debug);

        JCheckBox onlyPrint=new JCheckBox("打印需要执行的测试语句，不实际执行");
        toolBar.add(onlyPrint);

        JScrollPane scrollRes=new JScrollPane();
        scrollRes.setBounds(0,350,740,370);
        JTextArea result=new JTextArea();
        result.setEditable(false);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        scrollRes.setViewportView(result);

        submit.addActionListener(event->{
            if(defaultDB!=null&&!(!autoGen.isSelected()&&sql.getText().equals(""))) {
                JSONObject object = new JSONObject();
                object.put("ifAuto", autoGen.isSelected());
                object.put("type", type.getSelectedItem());
                object.put("concurrency", concurrency.getText());
                object.put("qNum", queryNum.getText());
                object.put("iterations", iterations.getText());
                object.put("engine", engine.getText());
                object.put("ifDebug", debug.isSelected());
                object.put("ifPrint", onlyPrint.isSelected());
                object.put("query",sql.getText());
                String res=sc.generateCommand(object, defaultDB);
                result.append(res);
            }else{
                JOptionPane.showMessageDialog(null,
                        "<html>请先配置数据库，并且输入测试语句或选择自动脚本</html>",
                        "提示",JOptionPane.WARNING_MESSAGE);
            }
        });


        panel.add(text1);
        panel.add(dbInfo);
        panel.add(dbSetting);
        panel.add(submit);
        panel.add(sql);
        panel.add(log);
        panel.add(load);
        funcPanel.add(toolBar);

        jFrame.add(panel);jFrame.add(funcPanel);
        jFrame.add(scrollRes);
        panel.setBounds(0,0,750,100);
        funcPanel.setBounds(0,100,740,250);

        jFrame.setLayout(null);
        jFrame.setSize(750,770);
        jFrame.setTitle("mysqlslap操作界面");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
