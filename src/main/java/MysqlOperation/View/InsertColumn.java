package MysqlOperation.View;

import MysqlOperation.domin.Define;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

public class InsertColumn {
    public void driver(Connection conn, String tableName, String dbName){
        JFrame frame=new JFrame();
        JPanel panel=new JPanel();
        JLabel[] text=new JLabel[3];
        JTextField columnName=new JTextField();
        JTextField columnLength=new JTextField();
        JButton confirm=new JButton("提交更改");
        String[] typeName={"INT","TINYINT","FLOAT","DOUBLE","CHAR","VARCHAR","TINYTEXT","TEXT","LONGTEXT","TINYBLOB","BLOB",
                "DATE","TIME","YEAR","DATETIME","TIMESTAMP"};
        JComboBox columnType=new JComboBox(typeName);
        panel.setLayout(new GridLayout(0,3,0,0));
        text[0]=new JLabel("字段名");
        text[1]=new JLabel("字段类型");
        text[2]=new JLabel("长度");
        panel.add(text[0]);panel.add(text[1]);panel.add(text[2]);
        panel.add(columnName);panel.add(columnType);panel.add(columnLength);
        panel.add(confirm);
        confirm.addActionListener(event->{
            String sql="ALTER TABLE "+dbName+"."+tableName+" add "+columnName.getText()+" "+columnType.getSelectedItem().toString()+"("+columnLength.getText()+")";
            Define define=new Define();
            try {
                define.insertColumn(conn,sql);
                frame.dispose();
                TableStructure.insertTrriger.doClick();
            }
            catch (Exception ex){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                if(sw.toString().contains("Duplicate")){
                    JOptionPane.showMessageDialog(null,"字段名不能重复","错误信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.setSize(600,120);
        frame.setTitle("新建字段");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
