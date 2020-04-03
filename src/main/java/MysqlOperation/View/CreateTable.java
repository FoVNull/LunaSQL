package MysqlOperation.View;

import MysqlOperation.Entity.NewColumn;
import MysqlOperation.Service.CreateService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

public class CreateTable {
    public void driver(Connection conn,String dbName){
        JFrame frame=new JFrame();
        JPanel columnInfo=new JPanel();
        JScrollPane columnPane=new JScrollPane();
        JPanel tableInfo=new JPanel();
        JPanel toolPanel=new JPanel();

        JLabel text1=new JLabel("表名：");
        text1.setBounds(100,0,50,25);
        tableInfo.add(text1);

        JTextField tableName=new JTextField("new_table");
        tableName.setBounds(150,0,200,25);
        tableInfo.add(tableName);

        JLabel text2=new JLabel("模式："+dbName);
        text2.setBounds(400,0,200,25);
        tableInfo.add(text2);

        JLabel text3=new JLabel("列名"+" ".repeat(20)+"数据类型"+" ".repeat(17)+"主键"+
                " ".repeat(24)+"禁止为空"+" ".repeat(24)+""+"唯一索引");
        text3.setBounds(100,50,650,40);
        tableInfo.add(text3);


        tableInfo.setLayout(null);
        tableInfo.setBounds(0,20,650,80);


        ArrayList<NewColumn> columnList=new ArrayList<>();
        NewColumn first = new NewColumn();
        columnList.add(first);
        first.ifPK.addActionListener(event->{
            if (first.ifPK.isSelected()) {
                first.ifNull.setSelected(true);
            }
        });
        for(Component o:first.getComponent()) columnInfo.add(o);

        columnPane.setBounds(50,100,600,40);
        columnInfo.setLayout(new GridLayout(0,5,0,0));
        columnPane.setViewportView(columnInfo);
        columnInfo.updateUI();
        columnPane.updateUI();

        JButton addColumn=new JButton("增加列");
        JButton apply=new JButton("提交");

        toolPanel.setBounds(50,500,600,100);
        toolPanel.setLayout(null);
        apply.setBounds(50,20,100,30);
        addColumn.setBounds(500,0,80,30);
        toolPanel.add(apply);toolPanel.add(addColumn);

        addColumn.addActionListener(event->{
            NewColumn newColumn = new NewColumn();
            columnList.add(newColumn);
            newColumn.ifPK.addActionListener(e-> {
                if (newColumn.ifPK.isSelected()){
                    newColumn.ifNull.setSelected(true);
                }
            });
            for(Component o:newColumn.getComponent()) columnInfo.add(o);

            int len=Math.min(columnList.size()*40, 400);
            columnPane.setBounds(50,100,600,len);
            columnInfo.updateUI();
        });

        apply.addActionListener(event->{
            JSONArray columns=new JSONArray();
            columns.put(dbName);
            columns.put(tableName.getText());
            for(NewColumn nc:columnList){
                if(!nc.name.getText().equals("")) {
                    JSONObject object = new JSONObject();
                    object.put("name", nc.name.getText());
                    object.put("type",nc.type.getSelectedItem());
                    object.put("ifPK",nc.ifPK.isSelected());
                    object.put("ifNull",nc.ifNull.isSelected());
                    object.put("ifUniq",nc.ifUniq.isSelected());
                    columns.put(object);
                }
            }
            CreateService cs=new CreateService();
            if(cs.buildSQL(columns,conn)) frame.dispose();
        });

        frame.setLayout(null);
        frame.add(columnPane);
        frame.add(tableInfo);
        frame.add(toolPanel);
        frame.setSize(700,650);
        frame.setTitle("新建表");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
