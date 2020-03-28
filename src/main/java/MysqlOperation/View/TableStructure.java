package MysqlOperation.View;

import MysqlOperation.domin.Define;
import MysqlOperation.domin.Delete;
import MysqlOperation.domin.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableStructure extends JRadioButton {
    private JFrame frame = new JFrame();
    private JPanel structurePanel=new JPanel();
    ButtonGroup bg = new ButtonGroup();
    private JButton confirm=new JButton("提交更改");
    private JButton clear=new JButton("清除选择");
    private JButton add=new JButton("新建字段");
    public JSONArray info=new JSONArray();
    public JSONObject infoObject=new JSONObject();
    public static JButton insertTrriger=new JButton();

    public void driver(ResultSet rs, Connection conn, String tableName, String dbName)throws SQLException {
        structurePanel.setLayout(new GridLayout(0,9,0,0));
        rs.last();
        int columnCount=rs.getRow();
        JButton[] delete=new JButton[columnCount];
        rs.beforeFirst();

        JTextField[] columnName=new JTextField[columnCount];
        JRadioButton[] pk=new JRadioButton[columnCount];
        JCheckBox[] ifNull=new JCheckBox[columnCount];
        JComboBox[] columnType=new JComboBox[columnCount];
        JTextField[] columnLength=new JTextField[columnCount];

        JButton[] indexEx=new JButton[columnCount];
        JTextField[] indexName=new JTextField[columnCount];
        JCheckBox[] ifNonUniq=new JCheckBox[columnCount];

        JLabel colNameLabel=new JLabel("字段");
        JLabel pkLabel=new JLabel("PK");
        JLabel ifNullLabel=new JLabel("默认是否为空");
        JLabel type=new JLabel("字段类型");
        JLabel length=new JLabel("字段长度");
        structurePanel.add(colNameLabel);
        structurePanel.add(type);
        structurePanel.add(length);
        structurePanel.add(pkLabel);
        structurePanel.add(ifNullLabel);
        structurePanel.add(new JLabel(""));
        structurePanel.add(new JLabel("索引名"));
        structurePanel.add(new JLabel("是否为唯一索引"));
        structurePanel.add(new JLabel(""));

        Query query=new Query();
        JSONArray indexs=query.showIndexs(conn,dbName,tableName);

        String[] pkName=query.queryPK(conn,dbName,tableName);
        String[] typeName={"INT","TINYINT","FLOAT","DOUBLE","CHAR","VARCHAR","TINYTEXT","TEXT","LONGTEXT","TINYBLOB","BLOB",
                "DATE","TIME","YEAR","DATETIME","TIMESTAMP"};
        JSONArray jsonArray=query.queryType(conn,dbName,tableName);
        for(int i=0;i<columnCount;i++){
            delete[i]=new JButton("删除字段");
            rs.next();
            columnName[i]=new JTextField(rs.getString(1));
            columnName[i].setEditable(false);
            columnType[i]=new JComboBox(typeName);
            columnLength[i]=new JTextField();
            ifNull[i]=new JCheckBox();
            for(int j=0;j<jsonArray.length();j++) {
                if (jsonArray.getJSONObject(j).getString("name").equals(rs.getString(1))){
                    columnType[i].setSelectedItem(jsonArray.getJSONObject(j).getString("type").toUpperCase());
                    columnLength[i].setText(jsonArray.getJSONObject(j).getString("length"));
                    if(jsonArray.getJSONObject(j).getString("nullable").equals("YES")){
                        ifNull[i].setSelected(true);
                    }
                }
            }
            pk[i]=new JRadioButton();
            bg.add(pk[i]);
            if(pkName.length!=0){
                for(int a=0;a<pkName.length;a++){
                    if(pkName[a].equals(rs.getString(1))){
                        pk[i].setSelected(true);
                    }
                }
            }
            structurePanel.add(columnName[i]);
            structurePanel.add(columnType[i]);
            structurePanel.add(columnLength[i]);
            structurePanel.add(pk[i]);
            structurePanel.add(ifNull[i]);
            structurePanel.add(delete[i]);
            int x=i;
            delete[i].addActionListener(event->{
                String deleteSql="ALTER TABLE "+dbName+"."+tableName+" DROP COLUMN "+columnName[x].getText();
                Delete delete1=new Delete();
                try {
                    delete1.delete(deleteSql, conn);
                    frame.dispose();
                    TableStructure tableStructure=new TableStructure();
                    Query query1=new Query();
                    ResultSet tempRs = query1.queryColumns(conn, dbName, tableName);
                    tableStructure.driver(tempRs,conn,tableName,dbName);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            });

            indexEx[i]=new JButton("添加索引");
            ifNonUniq[i]=new JCheckBox();
            indexName[i]=new JTextField("无");
            for(int j=0;j<indexs.length();++j) {
                if(indexs.getJSONObject(j).getString("columnName").equals(rs.getString(1))){
                    indexEx[i].setText("删除索引");
                    if(indexs.getJSONObject(j).getString("nonUnique").equals("0")) ifNonUniq[i].setSelected(true);
                    ifNonUniq[i].setEnabled(false);
                    indexName[i].setText(indexs.getJSONObject(j).getString("indexName"));
                    indexName[i].setEditable(false);
                    break;
                }
            }
            structurePanel.add(indexName[i]);
            structurePanel.add(ifNonUniq[i]);
            structurePanel.add(indexEx[i]);

            int y=i;
            indexEx[i].addActionListener(event->{
                Define define=new Define();
                if(indexEx[y].getText().equals("添加索引")){
                    String sql="CREATE ";
                    if(ifNonUniq[y].isSelected()) sql+="UNIQUE ";
                    sql+="INDEX "+indexName[y].getText()+" ON "+dbName+"."+tableName+"("+columnName[y].getText()+")";
                    define.changeIndex(conn,sql);
                }else{
                    String sql="DROP INDEX "+indexName[y].getText()+" ON "+dbName+"."+tableName;
                    define.changeIndex(conn,sql);
                }
                try {
                    frame.dispose();
                    TableStructure tableStructure = new TableStructure();
                    Query query1 = new Query();
                    ResultSet tempRs = query1.queryColumns(conn, dbName, tableName);
                    tableStructure.driver(tempRs, conn, tableName, dbName);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            });
        }
        structurePanel.add(confirm);
        structurePanel.add(clear);
        structurePanel.add(add);
        clear.addActionListener(event->{
            int res=JOptionPane.showConfirmDialog(null,
                    "无主键可能存在安全风险是否继续？","提示",JOptionPane.YES_NO_OPTION);
            if(res!=1){
                bg.clearSelection();
            }
        });
        confirm.addActionListener(event->{
            for(int i=0;i<columnCount;i++) {
                infoObject.put("name", columnName[i].getText());
                infoObject.put("type", columnType[i].getSelectedItem());
                infoObject.put("length", columnLength[i].getText());
                if(pk[i].isSelected()) {
                    infoObject.put("pk", "true");
                }
                else {
                    infoObject.put("pk", "false");
                }
                if(ifNull[i].isSelected()) {
                    infoObject.put("nullable", "true");
                }
                else {
                    infoObject.put("nullable", "false");
                }
                info.put(infoObject);
                infoObject = new JSONObject();
            }
            Define define=new Define();
            try {
                define.alertStructure(info,conn,tableName,dbName);
                frame.setVisible(false);
            }
            catch (Exception ex){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                System.out.print(sw.toString());
            }
        });
        add.addActionListener(event->{
            InsertColumn insertColumn=new InsertColumn();
            insertColumn.driver(conn,tableName,dbName);
            frame.dispose();
        });
        insertTrriger.addActionListener(e->{
            Query query1 = new Query();
            try {
                frame.dispose();
                TableStructure tableStructure=new TableStructure();
                ResultSet tempRs = query1.queryColumns(conn, dbName, tableName);
                tableStructure.driver(tempRs, conn, tableName, dbName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.add(structurePanel);
        frame.setSize(900,60+columnCount*50);
        frame.setTitle("编辑"+tableName+"结构");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
