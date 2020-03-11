package Optimization.ParameterOpt.View;

import Optimization.ParameterOpt.domin.SettingEdit;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.*;

public class ParameterView {

    public void paraDriver(Connection conn){
        SettingEdit se=new SettingEdit();
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,2,1,0));

        HashMap<String,String> para=se.paraQuery(conn);

        /*
        *前两个参数特殊处理
        *处理完删除map中的值
        */
        panel.add(new JLabel("table_open_cache(MB)"));
        JTextField table_cache=new JTextField();
        table_cache.setText(para.get("table_open_cache"));
        para.remove("table_open_cache");
        panel.add(table_cache);
        JLabel jLabel1=new JLabel("<html>缓冲区大小，当opened_tables不断增加且open_tables接近table_cache时，可以考虑适当调高该值,范围[1,524288]</html>");
        jLabel1.setForeground(Color.GRAY);
        panel.add(jLabel1);

        panel.add(new JLabel("Open_tables:"+para.get("Open_tables")+" / "+"Opened_tables:"+para.get("Opened_tables")));
        para.remove("Open_tables");para.remove("Opened_tables");

        panel.add(new JLabel("innodb_flush_log_at_trx_commit"));
        JComboBox fType=new JComboBox(new String[]{"0","1","2"});
        String value=para.get("innodb_flush_log_at_trx_commit");
        para.remove("innodb_flush_log_at_trx_commit");
        switch (value){
            case "0":fType.setSelectedIndex(0);break;
            case "1":fType.setSelectedIndex(1);break;
            case "2":fType.setSelectedIndex(2);break;
        }
        panel.add(fType);
        JLabel jLabel2=new JLabel("<html>建议值为安全性最高的1,值为0时效率最高安全性最低，值为2时平均</html>");
        jLabel2.setForeground(Color.GRAY);
        panel.add(jLabel2);
        panel.add(new JLabel());



        JTextField[] sizePara=new JTextField[8];
        JLabel[] sizeName=new JLabel[8];
        String[] info={
                "<html>行锁的死锁判断时间限制(s)</html>",
                "<html>若更新操作峰值或负载较大，可以提高该值(Byte)</html>",
                "<html>索引缓冲区大小，所有数据库共享(Byte)</html>",
                "<html>全连接缓冲区大小，影响多表连接速度</html>",
                "<html>全表扫描缓冲区大小</html>",
                "<html>排序缓冲区大小，影响ORDER BY和GROUP BY速度</html>",
                "<html>日志缓存区大小</html>",
                "<html>日志同步频率，为0表示日志文件写满再与磁盘同步</html>"
        };
        int p=0;
        for(Map.Entry<String,String> entry:para.entrySet()){
            sizeName[p]=new JLabel(entry.getKey());
            panel.add(sizeName[p]);
            sizePara[p]=new JTextField(entry.getValue());
            panel.add(sizePara[p]);
            JLabel tempLabel=new JLabel(info[p]);
            tempLabel.setForeground(Color.GRAY);
            panel.add(new JLabel());
            panel.add(tempLabel);
            ++p;
        }


        JButton ini=new JButton("打开配置文件");
        ini.addActionListener(event->{
            se.openIni(conn);
        });

        para.clear();
        JButton commit=new JButton("提交修改");
        commit.addActionListener(event->{
            para.put("table_open_cache",table_cache.getText());
            para.put("innodb_flush_log_at_trx_commit",fType.getSelectedIndex()+"");
            for(int i=0;i<8;++i){
                para.put(sizeName[i].getText(),sizePara[i].getText());
            }
            se.editPara(conn,para);
            jFrame.dispose();
            paraDriver(conn);
        });
        panel.add(commit);
        panel.add(new JLabel());

        panel.add(new JLabel("<html>参数更改后需重启数据库服务，更多参数可在配置文件中修改：</html>"));
        panel.add(ini);

        jFrame.add(panel);
        jFrame.setSize(700,750);
        jFrame.setTitle("部分优化参数");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
