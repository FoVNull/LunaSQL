package Starter;

import MysqlOperation.Entity.CusRes;
import MysqlOperation.domin.Customize;
import MysqlOperation.domin.Insert;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CustomSql{
    private JFrame frame=new JFrame();
    private JPanel panel;
    private JTextArea sql;
    private JButton excute;
    private JScrollPane console;
    private JTextPane feedback;
    public void customSqlDriver(Connection conn){
        panel=new JPanel();
        console=new JScrollPane();
        feedback=new JTextPane();
        feedback.setEditable(false);

        panel.setBounds(0,0,550,350);
        console.setBounds(0,350,550,350);
        feedback.setBounds(10,10,500,300);

        sql=new JTextArea(20,45);
        sql.setTabSize(4);
        sql.setLineWrap(true);        //激活自动换行功能
        sql.setWrapStyleWord(true); // 激活断行不断字功能
        excute=new JButton("执行");

        panel.add(sql);
        panel.add(excute);
        console.setViewportView(feedback);
        frame.add(console);
        frame.add(panel);
        feedback.setContentType("text/html");


        excute.addActionListener(event->{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String title="=".repeat(20)+df.format(new Date());
            Customize customize=new Customize();
                long x = System.currentTimeMillis();
                CusRes cr = customize.excuteSql(conn, sql.getText());
                long y = System.currentTimeMillis();
                if(cr.getCusResType()==0) {
                    ViewFrame.rs = cr.getCusResRs();
                    ViewFrame.cusListener.doClick();

                    String text = title + "<br>Action:" + sql.getText() + "<br>" + "Duration：" + (y - x) + "(ms)<br>";

                    HTMLDocument doc=(HTMLDocument) feedback.getStyledDocument();
                    try {
                        doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    String text="<font color='#FF3366'>"+title+"</font><br>"+"Action:"+sql.getText()+
                            "<br><font color='#FF3366'>Action Failed!</font><br>"+cr.getCusResEx().getMessage()+"<br>";
                    HTMLDocument doc=(HTMLDocument) feedback.getStyledDocument();
                    try {
                        doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
        });


        frame.setSize(550,700);
        frame.setTitle("自定义SQL");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
