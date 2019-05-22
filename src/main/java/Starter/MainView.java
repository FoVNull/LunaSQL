package Starter;

import DBConn.MysqlConn;
import DBConn.ReadInfo;
import MysqlOperation.View.TableStructure;
import MysqlOperation.domin.Delete;
import MysqlOperation.View.InsertInfo;
import MysqlOperation.domin.Query;
import MysqlOperation.domin.Update;
import com.mysql.jdbc.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MainView {
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            JFrame frame=new ViewFrame();
            frame.setTitle("Luna-SQL");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        MenuItem exit=new MenuItem("Exit");
        PopupMenu trayMenu=new PopupMenu();
        trayMenu.add(exit);
        exit.addActionListener(event->{
            System.exit(0);
        });
        Image trayImg=Toolkit.getDefaultToolkit().getImage("resources/img/leaf2.gif");
        SystemTray tray = SystemTray.getSystemTray();
        if(SystemTray.isSupported()){
            TrayIcon trayIcon = new TrayIcon(trayImg,"Luna-SQL", trayMenu);
            trayIcon.setImageAutoSize(true);
            try{
                tray.add(trayIcon);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
class  ViewFrame extends JFrame{
    private JLabel mainView=new JLabel();
    private JLabel dbList=new JLabel();
    private JToolBar toolBar=new JToolBar();
    private JButton[] confirmTool=new JButton[100];
    private JButton[] cancelTool=new JButton[100];
    private JButton[] deleteTool=new JButton[100];

    private JPanel mainPanel=new JPanel();
    private JPanel listPanel=new JPanel();
    private JScrollPane listScroll=new JScrollPane();
    private JSplitPane allSplitPane=new JSplitPane();
    private static final int DEFAULT_WIDTH=1200;
    private static final int DEFAULT_HEIGHT=800;

    public static JButton listButton=new JButton();
    public static JButton cusListener=new JButton();

    private DefaultMutableTreeNode root;
    private JTree tree;
    private DefaultMutableTreeNode connNode;
    private DefaultMutableTreeNode dbNode;
    private DefaultMutableTreeNode tableNode;
    private DefaultMutableTreeNode columnNode;
    private DefaultMutableTreeNode[] itemNode=new DefaultMutableTreeNode[10];
    private JPopupMenu connNodeMunu=new JPopupMenu();
    String connectingType;
    String connectingName;
    String connectingPsw;
    String connectingUrl;
    Connection[] connecting=new Connection[1000];//记录所有connection
    int connectingCount=0;
    public static Connection firstConn;//新建的连接

    private JSONArray updateInfo=new JSONArray();
    private JSONObject updateObject=new JSONObject();
    String[] pk=new String[100];
    String[] updateSQL=new String[1000];
    int[] sqlCount=new int[10000];
    JTable[] tempTable=new JTable[100];
    String deleteSql=new String();

    private DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
    ImageIcon leafIcon=new ImageIcon();
    ImageIcon leafIcon2=new ImageIcon();
    ImageIcon openIcon;
    ImageIcon closeIcon;

    private JTable[] table=new JTable[100];
    private JTabbedPane tabbedPane=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
    private int nowTabIndex=-1;

    private ConnViewer connViewer=new ConnViewer();
    public static ResultSet rs;
    public static String connName=new String();


    public ViewFrame(){
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);

        String imgUrl="resources/img";
//        ImageIcon consoleBg=new ImageIcon(imgUrl+"/6.jpg");
//        consoleBg.setImage(consoleBg.getImage().getScaledInstance(1400,300,Image.SCALE_DEFAULT));
//        console.setIcon(consoleBg);
//        console.setBounds(0,0,1400,300);

        ImageIcon listBg=new ImageIcon(imgUrl+"/3.jpg");
        listBg.setImage(listBg.getImage().getScaledInstance(520,1100,Image.SCALE_DEFAULT));
        dbList.setIcon(listBg);
        dbList.setBounds(0,0,520,1100);
        dbList.setOpaque(false);


        ImageIcon mainBg=new ImageIcon(imgUrl+"/4.jpg");
        mainBg.setImage(mainBg.getImage().getScaledInstance(880,615,Image.SCALE_DEFAULT));
        mainView.setIcon(mainBg);
        mainView.setBounds(280,100,1425,768);


        mainPanel.setBounds(523,0,1425,1200);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        listPanel.setLayout(null);
        listPanel.setBounds(0,0,520,1100);
        listPanel.setOpaque(false);


        listScroll.setBounds(0,0,520,1025);
        listScroll.setOpaque(false);
        listScroll.getViewport().setOpaque(false);
        listScroll.setBorder(null);


//        Font tablefont=new Font(null);
//        table.setFont(tablefont.deriveFont(Font.PLAIN,15));
        tabbedPane.setBounds(0,0,1380,1000);
        tabbedPane.setOpaque(false);
        tabbedPane.setBorder(null);
        tabbedPane.addChangeListener(event->{
            tabbedPane.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.isMetaDown()) {
                        //System.out.println(tabbedPane.getSelectedIndex());
                        showTabMenu(e.getX(),e.getY(),tabbedPane.getSelectedIndex());
                    }
                }
            });
        });
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateButton();
                deleteButton();
            }
        });
        mainPanel.add(mainView);
//        confirmTool.setSize(80,40);
//        cancelTool.setSize(80,40);
        toolBar.setBounds(0,985,200,40);
//        cancelTool.setEnabled(false);
//        confirmTool.setEnabled(false);
        toolBar.setFloatable(false);
//        toolBar.add(confirmTool);
//        toolBar.add(cancelTool);
        mainPanel.add(toolBar);


        leafIcon=new ImageIcon("resources/img/leaf.jpg");
        leafIcon.setImage(leafIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        leafIcon2=new ImageIcon("resources/img/5.jpg");
        leafIcon2.setImage(leafIcon2.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        openIcon=new ImageIcon("resources/img/open.png");
        openIcon.setImage(openIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        closeIcon=new ImageIcon("resources/img/close.jpg");
        closeIcon.setImage(closeIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        render.setLeafIcon(leafIcon);
        render.setOpenIcon(openIcon);
        render.setClosedIcon(closeIcon);
        Font font=new Font(null);
        render.setFont(font.deriveFont(Font.PLAIN,18));


        root=new DefaultMutableTreeNode("数据库连接列表");
        for(int n=0;n<ReadInfo.readInfo().length();n++) {
            connNode=new DefaultMutableTreeNode(ReadInfo.readInfo().getJSONObject(n).getString("connName"));
            root.add(connNode);
        }
        //tree.updateUI();
        tree = new JTree(root);
        tree.setOpaque(false);
        tree.setCellRenderer(render);


        tree.addTreeSelectionListener(e->{
            TreePath path = e.getPath();
//            TreePath[] paths = e.getPaths();
//            TreePath newLeadPath = e.getNewLeadSelectionPath();
//            TreePath oldLeadPath = e.getOldLeadSelectionPath();
            tree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int nodeLevel=0;
                    for(char str:path.toString().toCharArray()){
                        if(str==','){
                            nodeLevel++;
                        }
                    }
                    if(e.isMetaDown()) {
                        switch (nodeLevel) {
                            case 1:showConnMenu(e.getX(), e.getY(), path);
                            break;
                            case 2:openDB(e.getX(),e.getY(),path);
                            break;
                            case 3:
                            break;
                            case 4:openTable(e.getX(),e.getY(),path);
                            break;
                        }
                    }
                }
            });
        });
        listScroll.setViewportView(tree);
        listPanel.add(dbList);//bgimg

        allSplitPane.setBounds(0,0,1910,980);
        allSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        allSplitPane.setLeftComponent(listScroll);
        allSplitPane.setRightComponent(tabbedPane);
        allSplitPane.setDividerLocation(0.275);
        allSplitPane.setDividerSize(5);
        allSplitPane.setOpaque(false);
        add(allSplitPane);
        add(mainPanel);
        add(listPanel);



        JMenuBar menuBar=new JMenuBar();
        setJMenuBar(menuBar);

        JMenu dbType=new JMenu("数据库连接");
        JMenu optimize=new JMenu("优化");
        JMenu sql=new JMenu("SQL操作");
//        JMenu view=new JMenu("视图操作");
//        JMenu func=new JMenu("函数");
//        JMenu backup=new JMenu("备份");
        JMenu log=new JMenu("日志");
        JMenu running=new JMenu("运行分析");
        JMenu about=new JMenu("关于");
        menuBar.add(dbType);
        menuBar.add(optimize);
        //menuBar.add(view);
        menuBar.add(sql);
        //menuBar.add(func);
        //menuBar.add(backup);
        menuBar.add(log);
        menuBar.add(running);
        menuBar.add(about);

        JMenuItem localLog=new JMenuItem("查看MySQL日志");
        JMenuItem lunaLog=new JMenuItem("查看Luna-SQL日志");
        log.add(localLog);log.add(localLog);

        JMenuItem optimizeConsole=new JMenuItem("优化控制台");
        JMenuItem optListenPath=new JMenuItem("设置日志读取路径");
        JMenuItem optimizeHistory=new JMenuItem("优化建议历史");
        optimize.add(optimizeConsole);optimize.add(optListenPath);optimize.add(optimizeHistory);

        //JMenuItem oracle=new JMenuItem("Oracle");
        JMenuItem mysql=new JMenuItem("Mysql");
        //dbType.add(oracle);
        dbType.add(mysql);

        JMenuItem newQuery =new JMenuItem("自定义sql");
        JMenuItem openQuery =new JMenuItem("打开sql文件");
        //JMenuItem newLunaSql=new JMenuItem("新建Luna-SQL查询");
        sql.add(newQuery);
        sql.add(openQuery);
        //sql.add(newLunaSql);

        JMenuItem news=new JMenuItem("关于");
        JMenuItem help=new JMenuItem("帮助");
        about.add(help);
        about.add(news);

        newQuery.addActionListener(event->{
            CustomSql customSql=new CustomSql();
            customSql.customSqlDriver(connecting[connectingCount]);
        });
        cusListener.addActionListener(event->{
            nowTabIndex+=1;
            try {
                ResultSetMetaData rsmd1 = rs.getMetaData();
                int columnCount = rsmd1.getColumnCount();
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();
                Object[] title = new Object[columnCount];
                Object[][] info = new Object[rowCount][columnCount];
                for(int i=1;i<=columnCount;i++) {
                    title[i-1]=rsmd1.getColumnName(i);
                }
                int n=0;
                while (rs.next()) {
                    for (int i = 1; i <=columnCount; i++) {
                        info[n][i-1] = rs.getString(i);
                    }
                    n++;
                }
                table[nowTabIndex]=new JTable(info,title);
                table[nowTabIndex].setEnabled(false);
                table[nowTabIndex].setRowHeight(20);
                System.out.print(table[nowTabIndex].getRowCount());
                JTableHeader tableHeader = table[nowTabIndex].getTableHeader();
                tableHeader.setResizingAllowed(true);
                tableHeader.setReorderingAllowed(true);
                table[nowTabIndex].setOpaque(false);
                JScrollPane scrollPane=new JScrollPane();
                scrollPane.setViewportView(table[nowTabIndex]);
                tabbedPane.addTab("自定义查询",leafIcon2,scrollPane);

                confirmTool[nowTabIndex]=new JButton("提交修改");
                cancelTool[nowTabIndex]=new JButton("取消");
                deleteTool[nowTabIndex]=new JButton("删除选定记录");

                tabbedPane.updateUI();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        });


        news.addActionListener(event->{
            JOptionPane.showMessageDialog(
                    null,
                    "本软件仅供学习交流使用"+"\n"+"Email:JKL4131@126.com",
                    "制作者信息",
                    JOptionPane.PLAIN_MESSAGE
            );
        });

        mysql.addActionListener(event->{
            connViewer.driver();
        });
        //对新建连接页面的监听--------------------------------
        listButton.addActionListener(event->{
            connNode=new DefaultMutableTreeNode(connName);
            try {
                while (rs.next()) {
                    dbNode=new DefaultMutableTreeNode(rs.getString("Database"));
                    connNode.add(dbNode);
                }
                connNode.setUserObject(connNode.getUserObject()+"--正在运行");
                root.add(connNode);
                connectingCount=connNode.getParent().getIndex(connNode);
                connecting[connectingCount]=firstConn;
                tree.updateUI();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public static void getRs(ResultSet rs){
        ViewFrame.rs=rs;
    }
    public static ResultSet setRs(){
        return rs;
    }
    public static void getConnName(String connName){
        ViewFrame.connName=connName;
    }
    public static void getFirstConn(Connection firstConn){
        ViewFrame.firstConn=firstConn;
    }
    public void updateButton(){
        for(int i=0;i<tabbedPane.getTabCount();i++) {
            if(i!=tabbedPane.getSelectedIndex()){
                confirmTool[i].setVisible(false);
                cancelTool[i].setVisible(false);
                tabbedPane.updateUI();
            }
            else{
                confirmTool[tabbedPane.getSelectedIndex()].setVisible(true);
                cancelTool[tabbedPane.getSelectedIndex()].setVisible(true);
                tabbedPane.updateUI();
            }
        }
    }
    public void deleteButton(){
        for(int i=0;i<tabbedPane.getTabCount();i++) {
            if(i!=tabbedPane.getSelectedIndex()){
                deleteTool[i].setVisible(false);
                tabbedPane.updateUI();
            }
            else{
                deleteTool[tabbedPane.getSelectedIndex()].setVisible(true);
                tabbedPane.updateUI();
            }
        }
    }
    private void showConnMenu(int x,int y,TreePath path){
        JMenuItem openConn=new JMenuItem("打开连接");
        JMenuItem closeConn=new JMenuItem("关闭连接");
        connNodeMunu=new JPopupMenu();
        connNodeMunu.add(openConn);
        connNodeMunu.add(closeConn);
        //打开连接-----------------------------------
        openConn.addActionListener(event->{
            //closeConn.doClick();
            if(path.toString().contains("--正在运行")){
                JOptionPane.showMessageDialog(null, "已连接","",JOptionPane.WARNING_MESSAGE);
            }
            else{
                DefaultMutableTreeNode temp;
                temp=(DefaultMutableTreeNode)path.getPathComponent(1);
                for(int i=0;i<ReadInfo.readInfo().length();i++) {
                    if(ReadInfo.readInfo().getJSONObject(i).getString("connName").equals(temp.getUserObject())){
                        connectingName=ReadInfo.readInfo().getJSONObject(i).getString("userName");
                        connectingPsw=ReadInfo.readInfo().getJSONObject(i).getString("psw");
                        connectingUrl=ReadInfo.readInfo().getJSONObject(i).getString("url");
                        connectingType=ReadInfo.readInfo().getJSONObject(i).getString("type");
                    }
                }
                switch (connectingType) {
                    case "mysql": {
                        MysqlConn mysqlConn = new MysqlConn();
                        connectingCount = temp.getParent().getIndex(temp);
                        try {
                            connecting[connectingCount] = mysqlConn.conn(connectingUrl, connectingName, connectingPsw);
                            rs = mysqlConn.showDB(connecting[connectingCount]);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                try {
                    while (rs.next()) {
                        dbNode=new DefaultMutableTreeNode(rs.getString("Database"));
                        temp.add(dbNode);
                    }
                    temp.setUserObject(temp.getUserObject()+"--正在运行");
                    //root.add(connNode);
                    tree.updateUI();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //关闭连接---------------------------------
        closeConn.addActionListener(event->{
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(1);
            connectingCount=temp.getParent().getIndex(temp);
            String str=temp.getUserObject().toString().split("-")[0];
            if(temp.toString().contains("--正在运行")) {
                try {
                    int testTemp=0;
                    for(int i=0;i<ReadInfo.readInfo().length();i++) {
                        if (ReadInfo.readInfo().getJSONObject(i).getString("connName").equals(str)) {
                            testTemp=1;break;
                        }
                    }
                    if (testTemp==0){
                        root.remove(temp);
                        for(int i=connectingCount;i<connecting.length;i++){
                            connecting[i]=connecting[i+1];
                        }
                        tree.updateUI();
                    }
                    else {
                        MysqlConn mysqlConn = new MysqlConn();
                        rs = mysqlConn.showDB(connecting[connectingCount]);
                        while (rs.next()) {
                            temp.remove(0);
                        }
                        temp.setUserObject(str);
                        tree.updateUI();
                        connecting[connectingCount].close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        connNodeMunu.show(tree,x,y);
    }
    private void openDB(int x,int y,TreePath path){
        JMenuItem openDBConn=new JMenuItem("打开模式");
        JMenuItem closeDBConn=new JMenuItem("关闭模式");
        JMenuItem creatTabel=new JMenuItem("创建新表");
        connNodeMunu=new JPopupMenu();
        connNodeMunu.add(openDBConn);
        connNodeMunu.add(closeDBConn);
        connNodeMunu.add(creatTabel);

        openDBConn.addActionListener(event->{
            int itemNodeCount=0;
            switch (itemNodeCount){
                case 0:{
                    itemNode[0]=new DefaultMutableTreeNode();
                    itemNode[0].setUserObject("表");
                    itemNodeCount++;
                }
                case 1:{
                    itemNode[1]=new DefaultMutableTreeNode();
                    itemNode[1].setUserObject("视图");
                    itemNodeCount++;
                }
                case 2:{
                    itemNode[2]=new DefaultMutableTreeNode();
                    itemNode[2].setUserObject("函数");
                    itemNodeCount++;
                }
            }
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(2);
            if(!temp.isLeaf()){
                JOptionPane.showMessageDialog(null, "已经打开","",JOptionPane.WARNING_MESSAGE);
            }
            else {
                DefaultMutableTreeNode tempConn = (DefaultMutableTreeNode) path.getPathComponent(1);
                connectingCount = tempConn.getParent().getIndex(tempConn);
                MysqlConn mysqlConn = new MysqlConn();
                try {
                    rs = mysqlConn.openDB(connecting[connectingCount], temp.getUserObject().toString());
                    while (rs.next()) {
                        tableNode = new DefaultMutableTreeNode(rs.getString(1));
                        itemNode[0].add(tableNode);
                    }
                    for (int i = 0; i < 3; i++) {
                        temp.add(itemNode[i]);
                    }
                    temp.setUserObject(temp.getUserObject());
                    tree.updateUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        closeDBConn.addActionListener(event->{
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(2);
            //temp.setUserObject(temp.getUserObject().toString().split("-")[0]);
            for(int i=0;i<3;i++){
                temp.remove(0);
            }
            tree.updateUI();
        });
        connNodeMunu.show(tree,x,y);
    }
    private void openTable(int x,int y,TreePath path){
        JMenuItem query200=new JMenuItem("查看记录");
        JMenuItem insertValue=new JMenuItem("插入记录");
        //JMenuItem insertColumn=new JMenuItem("插入字段");
        JMenuItem tableStructure=new JMenuItem("表结构操作");
        JMenuItem deleteTable=new JMenuItem("删除选定表格");
        connNodeMunu=new JPopupMenu();
        //connNodeMunu.add(insertColumn);
        connNodeMunu.add(query200);
        connNodeMunu.add(insertValue);
        connNodeMunu.add(tableStructure);
        connNodeMunu.add(deleteTable);


        tableStructure.addActionListener(event->{
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(4);
            DefaultMutableTreeNode tempConn = (DefaultMutableTreeNode) path.getPathComponent(1);
            DefaultMutableTreeNode dbName = (DefaultMutableTreeNode) path.getPathComponent(2);
            connectingCount = tempConn.getParent().getIndex(tempConn);
            Query query=new Query();
            try {
                rs=query.queryColumns(connecting[connectingCount], dbName.getUserObject().toString(), temp.getUserObject().toString());
                TableStructure tableStructure1=new TableStructure();
                tableStructure1.driver(rs,connecting[connectingCount],temp.getUserObject().toString(),dbName.getUserObject().toString());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });

        query200.addActionListener(event->{
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(4);
            DefaultMutableTreeNode tempConn = (DefaultMutableTreeNode) path.getPathComponent(1);
            DefaultMutableTreeNode dbName = (DefaultMutableTreeNode) path.getPathComponent(2);
            connectingCount = tempConn.getParent().getIndex(tempConn);
            String dbNameText=dbName.getUserObject().toString();
            String tableText=temp.getUserObject().toString();
            nowTabIndex+=1;
            JScrollPane scrollPane=queryTable(connectingCount,dbNameText,tableText,nowTabIndex);
            tabbedPane.addTab("查询-"+connectingCount+"-"+dbName.getUserObject().toString()+"-"+temp.getUserObject().toString(),leafIcon2,scrollPane);
            //不同的tab分配不同的update,delete监听
            confirmTool[nowTabIndex]=new JButton("提交修改");
            cancelTool[nowTabIndex]=new JButton("取消");
            deleteTool[nowTabIndex]=new JButton("删除选定记录");
            confirmTool[nowTabIndex].setEnabled(false);
            cancelTool[nowTabIndex].setEnabled(false);
            deleteTool[nowTabIndex].setEnabled(false);
            toolBar.add(confirmTool[nowTabIndex]);
            toolBar.add(cancelTool[nowTabIndex]);
            toolBar.add(deleteTool[nowTabIndex]);
            tabbedPane.setSelectedIndex(nowTabIndex);
            updateButton();
            deleteButton();

            tabbedPane.updateUI();
        });
        insertValue.addActionListener(event->{
            DefaultMutableTreeNode temp;
            temp=(DefaultMutableTreeNode)path.getPathComponent(4);
            DefaultMutableTreeNode tempConn = (DefaultMutableTreeNode) path.getPathComponent(1);
            DefaultMutableTreeNode dbName = (DefaultMutableTreeNode) path.getPathComponent(2);
            connectingCount = tempConn.getParent().getIndex(tempConn);
            Query query=new Query();
            try {
                rs=query.queryColumns(connecting[connectingCount], dbName.getUserObject().toString(), temp.getUserObject().toString());
                InsertInfo insertInfo=new InsertInfo();
                insertInfo.insert(rs,connecting[connectingCount],temp.getUserObject().toString(),dbName.getUserObject().toString());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });
        connNodeMunu.show(tree,x,y);
    }
    private JScrollPane queryTable(int connectingCount,String dbName,String temp,int tabIndex){
        Query query = new Query();
        JScrollPane scrollPane=new JScrollPane();
        try{
            rs=query.query(connecting[connectingCount],dbName,temp);
            ResultSetMetaData rsmd1 = rs.getMetaData();
            int columnCount = rsmd1.getColumnCount();
            rs.last();
            int rowCount=rs.getRow();
            rs.beforeFirst();
            Object[] title=new Object[columnCount];
            Object[][] info=new Object[rowCount][columnCount];
            Object[] title1=new Object[columnCount];
            Object[][] info1=new Object[rowCount][columnCount];
            for(int i=1;i<=columnCount;i++) {
                title[i-1]=rsmd1.getColumnName(i);
                title1[i-1]=rsmd1.getColumnName(i);
            }
            int n=0;
            while (rs.next()) {
                for (int i = 1; i <=columnCount; i++) {
                    info[n][i-1] = rs.getString(i);
                    info1[n][i-1] = rs.getString(i);
                }
                n++;
            }
            table[tabIndex]=new JTable(info,title);
            tempTable[tabIndex]=new JTable(info1,title1);
            table[tabIndex].getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if(e.getType()==TableModelEvent.UPDATE){
                        //System.out.println(table[tabIndex].getValueAt(e.getLastRow(),e.getColumn()).toString());
                        confirmTool[tabIndex].setEnabled(true);
                        cancelTool[tabIndex].setEnabled(true);
                        sqlCount[tabIndex]++;
                        Query query1=new Query();
                        try {
                            int pkCount=query1.queryPK(connecting[connectingCount], dbName, temp).length;
                            String[] pkName=query1.queryPK(connecting[connectingCount], dbName, temp);
                            if(pkCount!=0) {
                                updateSQL[sqlCount[tabIndex]] = "UPDATE " + dbName + "." + temp + " SET " + table[tabIndex].getColumnName(e.getColumn()) + "='" +
                                        table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()).toString() + "' WHERE ";
                                updateSQL[sqlCount[tabIndex]] += pkName[0] + "='" + tempTable[tabIndex].getValueAt(e.getLastRow(), 0) + "'";
                                for (int i = 1; i < pkCount; i++) {
                                    updateSQL[sqlCount[tabIndex]] += "AND " + pkName[i] + "='" + tempTable[tabIndex].getValueAt(e.getLastRow(), i) + "'";
                                }
                                //System.out.println(updateSQL[tabIndex][sqlCount[tabIndex]]);
                                tempTable[tabIndex].setValueAt(table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()), e.getLastRow(), e.getColumn());
                            }
                            else{
                                int result=JOptionPane.showConfirmDialog(null,"此表未设置主键，建议您设置主键，继续可能会引发更新错误，是否继续？",
                                        "未设置主键!",JOptionPane.YES_NO_OPTION);
                                if(result!=1){
                                    updateSQL[sqlCount[tabIndex]]= "UPDATE "+dbName+"."+temp+" SET "+table[tabIndex].getColumnName(e.getColumn()) +"='"+
                                            table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()).toString() + "' WHERE ";
                                    updateSQL[sqlCount[tabIndex]] +=table[tabIndex].getColumnName(0)+"='"+tempTable[tabIndex].getValueAt(e.getLastRow(),0)+"'";
                                    for(int i=1;i<table[tabIndex].getColumnCount();i++){
                                        updateSQL[sqlCount[tabIndex]] +="AND "+table[tabIndex].getColumnName(i)+"='"+tempTable[tabIndex].getValueAt(e.getLastRow(),i)+"'";
                                    }
                                    tempTable[tabIndex].setValueAt(table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()), e.getLastRow(), e.getColumn());
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        confirmTool[tabIndex].addActionListener(event->{
                            Update update=new Update();
                            try {
                                update.update(updateSQL, connecting[connectingCount]);
                                updateSQL=new String[1000];
                                tabRefresh(tabIndex);
                            }
                            catch (Exception ex){
                                StringWriter sw = new StringWriter();
                                PrintWriter pw = new PrintWriter(sw);
                                ex.printStackTrace(pw);
                                if(sw.toString().contains("Duplicate")){
                                    JOptionPane.showMessageDialog(null,"主键值不唯一","错误",JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        });
                        cancelTool[tabIndex].addActionListener(event->{
                            tabRefresh(tabIndex);
                            updateSQL=new String[1000];
                            confirmTool[tabIndex].setEnabled(false);
                            cancelTool[tabIndex].setEnabled(false);
                        });
                    }
                }
            });
            table[tabIndex].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Query query1=new Query();
                    deleteSql="DELETE FROM "+dbName+"."+temp+" WHERE ";
                    deleteTool[tabIndex].setEnabled(true);
                    deleteTool[tabIndex].addActionListener(event->{
                        try{
                            int pkCount=query1.queryPK(connecting[connectingCount], dbName, temp).length;
                            String[] pkName=query1.queryPK(connecting[connectingCount], dbName, temp);
                            if(pkCount!=0){
                                deleteSql+=pkName[0]+"='"+table[tabIndex].getValueAt(table[tabIndex].getSelectedRow(),0)+"'";
                                for(int i=1;i<pkCount;i++) {
                                    deleteSql += " AND "+pkName[i]+"="+table[tabIndex].getValueAt(table[tabIndex].getSelectedRow(),i)+"'";
                                }
                            }
                            else{
                                int result=JOptionPane.showConfirmDialog(null,"此表未设置主键，建议您设置主键，继续可能会引发删除错误，是否继续？",
                                        "未设置主键!",JOptionPane.YES_NO_OPTION);
                                if(result!=1) {
                                    deleteSql +=table[tabIndex].getColumnName(0)+"='"+table[tabIndex].getValueAt(table[tabIndex].getSelectedRow(),0)+"'";
                                    for(int i=1;i<table[tabIndex].getColumnCount();i++){
                                        deleteSql +="AND "+table[tabIndex].getColumnName(i)+"='"+tempTable[tabIndex].getValueAt(table[tabIndex].getSelectedRow(),i)+"'";
                                    }
                                }
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        Delete delete1=new Delete();
                        try {
                            delete1.delete(deleteSql,connecting[connectingCount]);
                            tabRefresh(tabIndex);
                            deleteTool[tabIndex].setEnabled(false);
                        }
                        catch (Exception ex){
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            ex.printStackTrace(pw);
                        }
                    });
                }
            });
            table[tabIndex].setRowHeight(20);
            JTableHeader tableHeader = table[tabIndex].getTableHeader();
            tableHeader.setResizingAllowed(true);
            tableHeader.setReorderingAllowed(true);
            table[tabIndex].setOpaque(false);
            scrollPane.setViewportView(table[tabIndex]);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return scrollPane;
    }
    private void showTabMenu(int x,int y,int index){
        JMenuItem closeTab=new JMenuItem("关闭标签");
        JMenuItem closeAllTab=new JMenuItem("关闭所有标签");
        JMenuItem refresh=new JMenuItem("刷新页面");
        JPopupMenu tabMenu=new JPopupMenu();
        tabMenu.add(closeTab);
        tabMenu.add(closeAllTab);
        tabMenu.add(refresh);

        closeTab.addActionListener(event->{
            tabbedPane.remove(index);
            nowTabIndex-=1;
        });
        closeAllTab.addActionListener(event->{
            tabbedPane.removeAll();
            nowTabIndex=0;
        });
        refresh.addActionListener(event->{
            tabRefresh(index);
        });
        tabbedPane.updateUI();
        tabMenu.show(tabbedPane,x,y);
    }
    public void tabRefresh(int index){
        if(tabbedPane.getTitleAt(index).contains("-")) {
            String[] str = tabbedPane.getTitleAt(index).split("-");
            String title = tabbedPane.getTitleAt(index);
            JScrollPane scrollPane = queryTable(Integer.parseInt(str[1]), str[2], str[3], index);
            tabbedPane.remove(index);
            tabbedPane.insertTab(title, leafIcon2, scrollPane, null, index);
            tabbedPane.setSelectedIndex(index);
            Query query = new Query();
            try {
                rs = query.query(connecting[connectingCount], str[2], str[3]);
                ResultSetMetaData rsmd1 = rs.getMetaData();
                int columnCount = rsmd1.getColumnCount();
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();
                Object[] title1 = new Object[columnCount];
                Object[][] info = new Object[rowCount][columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    title1[i - 1] = rsmd1.getColumnName(i);
                }
                int n = 0;
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        info[n][i - 1] = rs.getString(i);
                    }
                    n++;
                }
                tempTable[index] = new JTable(info, title1);
                updateSQL = new String[1000];
                confirmTool[index].setEnabled(false);
                cancelTool[index].setEnabled(false);
                deleteTool[index].setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}