package Starter;

import Log.LogListener.SettingEdit;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ParameterView {

    public void paraDriver(Connection conn){
        SettingEdit se=new SettingEdit();
        JFrame jFrame=new JFrame();
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(0,2,1,0));

        String[] para=se.paraQuery(conn);

        panel.add(new JLabel("table_open_cache(MB):"));
        JTextField table_cache=new JTextField();
        table_cache.setText(para[0]);
        panel.add(table_cache);
        JLabel jLabel1=new JLabel("<html>缓冲区大小，当opened_tables不断增加且open_tables接近table_cache时，可以考虑适当调高该值,范围[1,524288]</html>");
        jLabel1.setForeground(Color.GRAY);
        panel.add(jLabel1);

        panel.add(new JLabel("Open_tables:"+para[1]+" / "+"Opened_tables:"+para[2]));

        panel.add(new JLabel("innodb_flush_log_at_trx_commit:"));
        JComboBox fType=new JComboBox(new String[]{"0","1","2"});
        switch (para[3]){
            case "TABLE":fType.setSelectedIndex(0);break;
            case "FILE":fType.setSelectedIndex(1);break;
            case "FILE,TABLE":fType.setSelectedIndex(2);break;
        }
        panel.add(fType);
        JLabel jLabel2=new JLabel("<html>建议值为安全性最高的1,值为0时效率最高安全性最低，值为2时平均</html>");
        jLabel2.setForeground(Color.GRAY);
        panel.add(jLabel2);
        panel.add(new JLabel());

        panel.add(new JLabel("innodb_lock_wait_timeout(s):"));
        JTextField lwt=new JTextField(para[4]);
        panel.add(lwt);
        JLabel jLabel3=new JLabel("<html>行锁的死锁判断时间限制</html>");
        jLabel3.setForeground(Color.GRAY);
        panel.add(jLabel3);
        panel.add(new JLabel());

        panel.add(new JLabel("innodb_log_buffer_size(B):"));
        JTextField log_buffer=new JTextField();
        log_buffer.setText(para[5]);
        panel.add(log_buffer);
        JLabel jLabel4=new JLabel("<html>若更新操作峰值或负载较大，可以提高该值</html>");
        jLabel4.setForeground(Color.GRAY);
        panel.add(jLabel4);
        panel.add(new JLabel());

        JButton ini=new JButton("打开配置文件");
        ini.addActionListener(event->{
            se.openIni(conn);
        });

        JButton commit=new JButton("提交修改");
        commit.addActionListener(event->{
            se.editPara(conn,table_cache.getText(),fType.getSelectedIndex()+"",lwt.getText(),log_buffer.getText());
            jFrame.dispose();
            paraDriver(conn);
        });
        panel.add(commit);
        panel.add(new JLabel());

        panel.add(new JLabel("<html>注意修改参数需要重启数据库服务，更多参数可以在配置文件中修改：</html>"));
        panel.add(new JLabel());
        panel.add(ini);

        jFrame.add(panel);
        jFrame.setSize(500,500);
        jFrame.setTitle("部分优化参数");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
