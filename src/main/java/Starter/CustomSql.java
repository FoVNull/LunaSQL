package Starter;

import MysqlOperation.Entity.CusRes;
import MysqlOperation.Entity.SqlKeywords;
import MysqlOperation.domin.Customize;
import MysqlOperation.domin.Insert;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
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
    private JTextPane sql;
    private JButton excute;
    private JScrollPane console;
    private JTextPane feedback;
    private JScrollPane sqlPanel;
    public void customSqlDriver(Connection conn){
        panel=new JPanel();
        console=new JScrollPane();
        feedback=new JTextPane();
        feedback.setEditable(false);
        sqlPanel=new JScrollPane();
        sql=new JTextPane();

        Style style=sql.addStyle("BLUE",null);
        StyleConstants.setForeground(style,Color.BLUE);
        StyleConstants.setBold(style, true);

        sql.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                StyledDocument doc=(StyledDocument) sql.getDocument();
                int flag=0;
                for (int i = 0; i < sql.getDocument().getLength(); i++) {
                    try {
                        String nowChar=sql.getDocument().getText(i,1);
                        if(nowChar.equals(" ")||nowChar.equals("\n")
                                ||nowChar.equals("(")||nowChar.equals(";")) {
                            String s = sql.getDocument().getText(flag, i - flag);
                            for (SqlKeywords skw : SqlKeywords.values()) {
                                if (skw.toString().equals(s) || skw.getLowWord(skw).equals(s)) {
                                    int finalFlag = flag;
                                    int finalI = i;
                                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            doc.setCharacterAttributes(finalFlag, finalI - finalFlag, sql.getStyle("BLUE"), true);
                                        }
                                    });
                                }
                            }
                            flag = i + 1;
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });


        panel.setBounds(0,0,650,350);
        panel.setLayout(null);
        console.setBounds(0,350,650,350);
        feedback.setBounds(10,10,600,300);
        sqlPanel.setBounds(10,50,600,270);


        sql.setBounds(0,0,600,270);
        sqlPanel.setViewportView(sql);

        excute=new JButton("执行");
        excute.setBounds(20,10,80,25);
        panel.add(excute);
        JLabel label=new JLabel("可以选定部分sql语句执行");
        label.setBounds(140,10,200,25);
        panel.add(label);

        panel.add(sqlPanel);


        console.setViewportView(feedback);
        frame.add(console);
        frame.add(panel);
        feedback.setContentType("text/html");


        excute.addActionListener(event->{
            String selectedText="";
            if(sql.getSelectedText()==null) selectedText=sql.getText();
            else selectedText=sql.getSelectedText();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String title="=".repeat(20)+df.format(new Date());
            Customize customize=new Customize();
                long x = System.currentTimeMillis();
                CusRes cr = customize.excuteSql(conn, selectedText);
                long y = System.currentTimeMillis();
                if(cr.getCusResType()==0) {
                    ViewFrame.rs = cr.getCusResRs();
                    ViewFrame.cusListener.doClick();

                    String text = title + "<br>Action:" + selectedText + "<br>" + "Duration：" + (y - x) + "(ms)<br>";

                    HTMLDocument doc=(HTMLDocument) feedback.getStyledDocument();
                    try {
                        doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    String text="<font color='#FF3366'>"+title+"</font><br>"+"Action:"+selectedText+
                            "<br><font color='#FF3366'>Action Failed!</font><br>"+cr.getCusResEx().getMessage()+"<br>";
                    HTMLDocument doc=(HTMLDocument) feedback.getStyledDocument();
                    try {
                        doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
        });


        frame.setSize(650,700);
        frame.setTitle("自定义SQL");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
