package Starter;

import DBConn.MysqlConn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;

import DBConn.SaveInfo;

public class ConnViewer{
    JFrame frame1=new ConnViewerFrame();
    public void driver(){
        EventQueue.invokeLater(()->{
            frame1.setTitle("连接数据库");
            frame1.setResizable(false);
            frame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame1.setLocationRelativeTo(null);
            //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame1.setVisible(true);
        });
    }
}
class ConnViewerFrame extends JFrame {
    private static final int DEFAULT_WIDTH=600;
    private static final int DEFAULT_HEIGHT=700;

    private JPanel infoPanel=new JPanel();

    private JLabel urlText=new JLabel();
    private JLabel nameText=new JLabel();
    private JLabel passwordText=new JLabel();
    private JLabel connNameText=new JLabel();

    private static JTextField connName=new JTextField();
    private static JTextField url=new JTextField();
    private static JTextField name=new JTextField();
    private static JPasswordField password=new JPasswordField();

    private JCheckBox checkBox1 = new JCheckBox("是否保存连接");

    private JButton test=new JButton();
    public static JButton confirm=new JButton();
    private JButton concel=new JButton();

    public static ResultSet rs=null;

    public ConnViewerFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        infoPanel.setOpaque(true);
        infoPanel.setLayout(null);
        infoPanel.setAlignmentY(150);
        infoPanel.setBounds(100, 0, 600, 700);
        add(infoPanel);

        connNameText.setBounds(100,50,110,50);
        connNameText.setText("自定义连接名称：");
        infoPanel.add(connNameText);

        connName.setBounds(200,60,300,30);
        infoPanel.add(connName);

        urlText.setBounds(100, 150, 100, 50);
        urlText.setText("数据库地址：");
        infoPanel.add(urlText);

        url.setBounds(200, 160, 300, 30);
        infoPanel.add(url);


        nameText.setBounds(100, 250, 100, 50);
        nameText.setText("用户名：");
        infoPanel.add(nameText);

        name.setBounds(200, 260, 300, 30);
        infoPanel.add(name);

        passwordText.setBounds(100, 350, 100, 50);
        passwordText.setText("密码：");
        infoPanel.add(passwordText);

        password.setBounds(200, 360, 300, 30);
        infoPanel.add(password);

        checkBox1.setBounds(150,400,200,30);
        infoPanel.add(checkBox1);


        test.setBounds(100, 500, 100, 30);
        test.setText("测试连接");
        confirm.setBounds(240, 500, 60, 30);
        confirm.setText("连接");
        concel.setBounds(340, 500, 60, 30);
        concel.setText("取消");

        infoPanel.add(test);
        infoPanel.add(confirm);
        infoPanel.add(concel);

        concel.addActionListener(event -> {
            this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
        });
        test.addActionListener(event->{
            String title="连接测试";
            try {
                StringBuilder psw=new StringBuilder();
                for(int i=0;i<password.getPassword().length;i++) {
                    psw.append(password.getPassword()[i]);
                }
                MysqlConn mysqlConn = new MysqlConn();
                Connection tempConn;
                tempConn=mysqlConn.conn(url.getText(), name.getText(),psw.toString());
                JOptionPane.showMessageDialog(null, "连接成功", title, JOptionPane.PLAIN_MESSAGE);
                tempConn.close();
            }
            catch (Exception e){
                if(e.getMessage().contains("Access denied for user")){
                    JOptionPane.showMessageDialog(null, "用户名或密码错误",title,JOptionPane.ERROR_MESSAGE);
                    //System.out.print(sw.toString());
                }
                else{
                    JOptionPane.showMessageDialog(null, "地址错误", title, JOptionPane.ERROR_MESSAGE);
                    System.out.println(e.toString());
                }
            }
        });
        confirm.addActionListener(event->{
            String title="错误信息";
            if(connName.getText().equals("")||url.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "未定义连接名称或地址为空", title, JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    StringBuilder psw = new StringBuilder();
                    for (int i = 0; i < password.getPassword().length; i++) {
                        psw.append(password.getPassword()[i]);
                    }
                    MysqlConn mysqlConn = new MysqlConn();
                    Connection conn = mysqlConn.conn(url.getText(), name.getText(), psw.toString());
                    rs = mysqlConn.showDB(conn);
                    ViewFrame.getRs(rs);
                    ViewFrame.getConnName(connName.getText());
                    SaveInfo saveInfo=new SaveInfo();
                    ViewFrame.getFirstConn(conn);
                    if(checkBox1.isSelected()){
                        if(!saveInfo.saveLogin(url.getText(),name.getText(),psw.toString(),connName.getText(),"mysql")){
                            JOptionPane.showMessageDialog(null, "已存在同名连接", " ", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            ViewFrame.listButton.doClick();
                            this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
                        }
                    }
                    else {
                        ViewFrame.listButton.doClick();
                        this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING) );
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains("Access denied for user")) {                    
                        JOptionPane.showMessageDialog(null, "用户名或密码错误", " ", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "地址错误", " ", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
