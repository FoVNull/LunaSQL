package MysqlOperation.View;


import MysqlOperation.domin.Insert;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;

public class InsertInfo {
    public JFrame insertFrame=new JFrame();
    public JPanel insertInfo=new JPanel();
    public JLabel[] columnName;
    public JTextField[] value;
    public JButton submit=new JButton("提交");
    private JScrollPane scrollPane=new JScrollPane();
    JSONArray insertArray=new JSONArray();
    public void insert(ResultSet rs, Connection conn, String table, String dbName){
        try {
            rs.last();
            int columnCount=rs.getRow();
            rs.beforeFirst();
            columnName=new JLabel[columnCount];
            value=new JTextField[columnCount];
            insertInfo.setLayout(new GridLayout(0,2,10,0));
            submit.setSize(80,50);
            String[] values=new String[columnCount];
            String[] column=new String[columnCount];
            int i=0;
            while(rs.next()){
                columnName[i]=new JLabel();
                value[i]=new JTextField();
                columnName[i].setSize(30,20);
                value[i].setSize(200,30);
                columnName[i].setText(rs.getString(1));
                insertInfo.add(columnName[i]);
                insertInfo.add(value[i]);
                i++;
            }
            insertInfo.add(submit);
            submit.addActionListener(event->{
                int n=0;
                try {
                    JSONObject insertObject = new JSONObject();
                    rs.beforeFirst();
                    while (rs.next()) {
                        insertObject.put(rs.getString(1), value[n].getText());
                        column[n]=rs.getString(1);
                        n++;
                    }
                    insertArray.put(insertObject);
                    int a=0;
                    rs.beforeFirst();
                    while (rs.next()) {
                        values[a]=insertArray.getJSONObject(0).getString(rs.getString(1));
                        a++;
                    }
                    Insert insert=new Insert();
                    insert.insert(conn,table,dbName,column,values);
                    insertFrame.dispatchEvent(new WindowEvent(insertFrame,WindowEvent.WINDOW_CLOSING));
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            scrollPane.setViewportView(insertInfo);
            insertFrame.add(scrollPane);
            insertFrame.setSize(600,60*columnCount+50);
            insertFrame.setTitle("插入记录");
            insertFrame.setResizable(false);
            insertFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            insertFrame.setLocationRelativeTo(null);
            insertFrame.setVisible(true);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
