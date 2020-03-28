package Starter;

import DBConn.ConnMange;
import DBConn.MysqlConn;
import DBConn.ReadInfo;
import MysqlOperation.domin.Script;
import Optimization.Evaluation.PerformanceTest.View.MysqlSlap;
import Optimization.LogAnalyses.domin.LogReader;
import Optimization.LogAnalyses.View.LogConsoleView;
import Optimization.Evaluation.PerformanceTest.View.CaseCustomize;
import Optimization.Evaluation.PerformanceTest.View.MultiCases;
import Optimization.Evaluation.ParameterOpt.View.ParameterView;
import MysqlOperation.View.TableStructure;
import MysqlOperation.domin.Delete;
import MysqlOperation.View.InsertInfo;
import MysqlOperation.domin.Query;
import MysqlOperation.domin.Update;
import Optimization.Evaluation.PerformanceTest.View.ParaEvaluation;
import Optimization.Evaluation.ParameterOpt.domin.EvaluationIO;
import Optimization.SqlOptimize.View.SoarConsole;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.JTableHeader;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class MainView {
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            JFrame frame=new ViewFrame();
            frame.setTitle("Luna-SQL");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        MenuItem exit=new MenuItem("Exit");
        PopupMenu trayMenu=new PopupMenu();
        trayMenu.add(exit);
        exit.addActionListener(event->{
            System.exit(0);
        });
        StringBuilder urlBuilder=Location.Path.getPath();;
        urlBuilder.append("classes/img");
        String imgUrl=urlBuilder.toString();
        Image trayImg=Toolkit.getDefaultToolkit().getImage(imgUrl+"/leaf2.gif");
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
    ReadInfo ri=new ReadInfo();
    private JLabel mainView=new JLabel();
    private JLabel dbList=new JLabel();
    private JToolBar toolBar=new JToolBar();
    private JButton[] confirmTool=new JButton[20];
    private JButton[] cancelTool=new JButton[20];
    private JButton[] deleteTool=new JButton[20];

    private JPanel mainPanel=new JPanel();
    private JPanel listPanel=new JPanel();
    private JScrollPane listScroll=new JScrollPane();
    private JSplitPane allSplitPane=new JSplitPane();
    private static final int DEFAULT_WIDTH=1280;
    private static final int DEFAULT_HEIGHT=800;

    public static JButton listButton=new JButton();
    public static JButton cusListener=new JButton();

    private DefaultMutableTreeNode root;
    private JTree tree;
    private DefaultMutableTreeNode connNode;
    private DefaultMutableTreeNode dbNode;
    private DefaultMutableTreeNode tableNode;
    private DefaultMutableTreeNode[] itemNode=new DefaultMutableTreeNode[3];
    private JPopupMenu connNodeMunu=new JPopupMenu();
    String connectingType;
    String connectingName;
    String connectingPsw;
    String connectingUrl;
    Connection[] connecting=new Connection[100];//记录所有connection
    int connectingCount=0;
    public static Connection firstConn;//新建的连接

    String[] updateSQL=new String[20];
    int[] sqlCount=new int[20];
    JTable[] tempTable=new JTable[20];
    String deleteSql=new String();

    private DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
    ImageIcon leafIcon;
    ImageIcon leafIcon2;
    ImageIcon openIcon;
    ImageIcon closeIcon;

    private JTable[] table=new JTable[20];
    private JTabbedPane tabbedPane=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
    private int nowTabIndex=-1;

    private ConnViewer connViewer=new ConnViewer();
    public static ResultSet rs;
    public static String connName=new String();


    public ViewFrame(){
        //界面初始化
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);

        StringBuilder urlBuilder=Location.Path.getPath();;
        urlBuilder.append("classes/img");
        String imgUrl=urlBuilder.toString();

        setIconImage(Toolkit.getDefaultToolkit().getImage(imgUrl+"/LunaSQL-LOGO.png"));


        ImageIcon listBg=new ImageIcon(imgUrl+"/3.jpg");
        listBg.setImage(listBg.getImage().getScaledInstance(300,650,Image.SCALE_DEFAULT));
        dbList.setIcon(listBg);
        dbList.setBounds(0,0,280,800);
        dbList.setOpaque(false);


        ImageIcon mainBg=new ImageIcon(imgUrl+"/4.jpg");
        mainBg.setImage(mainBg.getImage().getScaledInstance(600,420,Image.SCALE_DEFAULT));
        mainView.setIcon(mainBg);
        mainView.setBounds(200,0,780,680);


        mainPanel.setBounds(280,50,780,800);
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        listPanel.setLayout(null);
        listPanel.setBounds(0,0,280,800);
        listPanel.setOpaque(false);


        listScroll.setBounds(0,0,280,800);
        listScroll.setOpaque(false);
        listScroll.getViewport().setOpaque(false);
        listScroll.setBorder(null);


//        Font tablefont=new Font(null);
//        table.setFont(tablefont.deriveFont(Font.PLAIN,15));
        tabbedPane.setBounds(0,0,780,600);
        tabbedPane.setOpaque(false);
        tabbedPane.setBorder(null);
        tabbedPane.addChangeListener(event->{
            tabbedPane.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.isMetaDown()) {
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
        toolBar.setBounds(0,660,200,30);
//        cancelTool.setEnabled(false);
//        confirmTool.setEnabled(false);
        toolBar.setFloatable(false);
//        toolBar.add(confirmTool);
//        toolBar.add(cancelTool);
        mainPanel.add(toolBar);


        leafIcon=new ImageIcon(imgUrl+"/integral.png");
        leafIcon.setImage(leafIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        leafIcon2=new ImageIcon(imgUrl+"/5.jpg");
        leafIcon2.setImage(leafIcon2.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        openIcon=new ImageIcon(imgUrl+"/open.png");
        openIcon.setImage(openIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        closeIcon=new ImageIcon(imgUrl+"/close.jpg");
        closeIcon.setImage(closeIcon.getImage().getScaledInstance(22,22,Image.SCALE_DEFAULT));
        render.setLeafIcon(leafIcon);
        render.setOpenIcon(openIcon);
        render.setClosedIcon(closeIcon);
        Font font=new Font(null);
        render.setFont(font.deriveFont(Font.PLAIN,18));


        root=new DefaultMutableTreeNode("数据库连接列表");
        for(int n=0;n<ri.readInfo().length();n++) {
            connNode=new DefaultMutableTreeNode(ri.readInfo().getJSONObject(n).getString("connName"));
            root.add(connNode);
        }
        //tree.updateUI();
        tree = new JTree(root);
        tree.setOpaque(false);
        tree.setCellRenderer(render);

        // 功能↓

        tree.addTreeSelectionListener(e->{
            TreePath path = e.getPath();
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

        allSplitPane.setBounds(0,0,1260,710);
        allSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        allSplitPane.setLeftComponent(listScroll);
        allSplitPane.setRightComponent(tabbedPane);
        allSplitPane.setDividerLocation(0.22);
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
        JMenu log=new JMenu("日志");
        JMenu about=new JMenu("关于");
        menuBar.add(dbType);

        menuBar.add(sql);
        menuBar.add(log);
        menuBar.add(optimize);
        menuBar.add(about);

        JMenuItem generalLog=new JMenuItem("查看常规日志");
        JMenuItem slowLog=new JMenuItem("查看慢日志");
        JMenuItem errLog=new JMenuItem("错误日志");

        log.add(generalLog);log.add(slowLog);log.add(errLog);
        JMenuItem logConsole=new JMenuItem("日志控制台");
        log.add(logConsole);

        JMenuItem parameterEdit=new JMenuItem("参数调整");
        JMenuItem autoPara=new JMenuItem("参数性能评估(LunaSQL测试)");
        JMenuItem slap=new JMenuItem("mysqlslap压力测试");
        JMenuItem overWrite=new JMenuItem("sql优化");
        optimize.add(parameterEdit);
        optimize.add(autoPara);
        optimize.add(slap);
        optimize.add(overWrite);

        JMenuItem mysql=new JMenuItem("Mysql");
        JMenuItem manageConn=new JMenuItem("管理连接");
        dbType.add(mysql);
        dbType.add(manageConn);

        JMenuItem newQuery =new JMenuItem("自定义sql");
        JMenuItem openQuery =new JMenuItem("打开sql文件");
        sql.add(newQuery);
        sql.add(openQuery);

        JMenuItem news=new JMenuItem("关于");
        JMenuItem help=new JMenuItem("帮助");
        about.add(help);
        about.add(news);

        newQuery.addActionListener(event->{
            CustomSql customSql=new CustomSql();
            customSql.customSqlDriver(connecting[connectingCount]);
        });
        manageConn.addActionListener(event->{
            ConnMange cm=new ConnMange();
            cm.driver();
        });
        //自定义sql监听-----
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
                JTableHeader tableHeader = table[nowTabIndex].getTableHeader();
                tableHeader.setResizingAllowed(true);
                tableHeader.setReorderingAllowed(true);
                table[nowTabIndex].setOpaque(false);
                table[nowTabIndex].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                JScrollPane scrollPane=new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setViewportView(table[nowTabIndex]);
                tabbedPane.addTab("自定义查询(只读)",leafIcon2,scrollPane);

                confirmTool[nowTabIndex]=new JButton("提交修改");
                cancelTool[nowTabIndex]=new JButton("取消");
                deleteTool[nowTabIndex]=new JButton("删除选定记录");

                tabbedPane.updateUI();
            }
            catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        openQuery.addActionListener(event->{
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter fft=new FileFilter(){
                @Override
                public boolean accept(File file){
                    return file.getName().endsWith("sql");
                }

                @Override
                public String getDescription(){
                    return "需要sql文件(*.sql)";
                }
            };

            jfc.setFileFilter(fft);
            jfc.showOpenDialog(null);
            File inputPath=jfc.getSelectedFile();
            if(inputPath!=null) {
                Script script=new Script();
                try {
                    List<ResultSet> scSql=script.multiEx(inputPath, connecting[connectingCount]);
                    for(ResultSet r:scSql){
                        JScrollPane logPane=queryLog(r,++nowTabIndex);
                        tabbedPane.addTab("自定义查询(只读)",leafIcon2,logPane);
                    }
                }catch (SQLException e){
                    JOptionPane.showMessageDialog(null,e.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        help.addActionListener(event->{
            JOptionPane.showMessageDialog(null,
                    "<html>所有的日志文件存储于LunaSQL/classes/LunaLOG文件夹中<br>" +
                            "sql优化使用的是soar，Credit：github.com/XiaoMi<br>" +
                            "您可以在安装文件夹中找到soar.exe并使用它，文档参见上述地址</html>",
                    "帮助",
                    JOptionPane.PLAIN_MESSAGE
            );
        });

        news.addActionListener(event->{
            JOptionPane.showMessageDialog(
                    null,
                    "<html>本软件仅供学习交流使用(For educational use only)，完全遵循GPL协议<br>Credit：hikki.top<br>@fovnull 2020</html>",
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
            catch (SQLException e){
                JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });


        logConsole.addActionListener(event->{
            LogConsoleView logConsoleView = new LogConsoleView();
            logConsoleView.logConsoleDriver(connecting[connectingCount]);
        });

        generalLog.addActionListener(event->{
            LogReader logReader=new LogReader();
            ResultSet logRs=null;
            try {
                logRs = logReader.getGLog(connecting[connectingCount]);
            }catch (NullPointerException e){
                JOptionPane.showMessageDialog(null,"未连接数据库！","Error",JOptionPane.ERROR_MESSAGE);
            }
            if(logRs!=null) {
                JScrollPane logPane = queryLog(logRs, ++nowTabIndex);
                tabbedPane.addTab("general_log", leafIcon2, logPane);
                tabbedPane.setSelectedIndex(nowTabIndex);
                tabbedPane.updateUI();
            }
        });

        slowLog.addActionListener(event->{
            LogReader logReader=new LogReader();
            logReader.getSLog(connecting[connectingCount]);
        });

        errLog.addActionListener(event->{
            LogReader logReader=new LogReader();
            logReader.getELog(connecting[connectingCount]);
        });

        parameterEdit.addActionListener(event->{
            ParameterView parameterView=new ParameterView();
            parameterView.paraDriver(connecting[connectingCount]);
        });

        EvaluationIO eios=new EvaluationIO();
        eios.readDefault();
        autoPara.addActionListener(event->{
//            if(connectingCount>0) JOptionPane.showMessageDialog(null,
//                    "当前开启了多个数据库连接，日志/优化模块只针对最新的数据库连接！",
//                    "注意",JOptionPane.WARNING_MESSAGE);


            int evaType=JOptionPane.showOptionDialog(null,"<html>请选择使用自定义语句进行评估或使用默认语句(您可以在自定义中更改默认语句)<br>您可以使用自定义方法针对不同的参数使用不同的用例测试，也可以不区分参数通过自定义脚本输入用例</html>",
                    "评估方式",JOptionPane.YES_NO_CANCEL_OPTION,1,null,new String[]{"默认","自定义","并发测试"},1);
            if (evaType == 0) {
                String[] inputSql = new String[1];
                EvaluationIO eio = new EvaluationIO();
                inputSql[0] = eio.readDefault();
                ParaEvaluation ao = new ParaEvaluation();
                ao.autoTest(connecting[connectingCount], inputSql, 0);
            } else if (evaType == 1) {
                CaseCustomize caseCustomize = new CaseCustomize();
                caseCustomize.frameDriver(connecting[connectingCount]);
            } else if(evaType==2) {
                MultiCases multiCases = new MultiCases();
                multiCases.driver(connecting[connectingCount]);
            }
        });

        slap.addActionListener(event->{
            MysqlSlap ms=new MysqlSlap();
            ms.consoleDriver();
        });

        overWrite.addActionListener(event->{
            SoarConsole soarConsole=new SoarConsole();
            soarConsole.consoleDriver();
        });
    }
    public static void getRs(ResultSet rs){
        ViewFrame.rs=rs;
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
                for(int i=0;i<ri.readInfo().length();i++) {
                    if(ri.readInfo().getJSONObject(i).getString("connName").equals(temp.getUserObject())){
                        connectingName=ri.readInfo().getJSONObject(i).getString("userName");
                        connectingPsw=ri.readInfo().getJSONObject(i).getString("psw");
                        connectingUrl=ri.readInfo().getJSONObject(i).getString("url");
                        connectingType=ri.readInfo().getJSONObject(i).getString("type");
                    }
                }
                MysqlConn mysqlConn = new MysqlConn();
                connectingCount = temp.getParent().getIndex(temp);
                try {
                    connecting[connectingCount] = mysqlConn.conn(connectingUrl, connectingName, connectingPsw);
                    rs = mysqlConn.showDB(connecting[connectingCount]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
                catch (SQLException e){
                    JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
                    boolean isTemp=true;
                    for(int i=0;i<ri.readInfo().length();i++) {
                        if (ri.readInfo().getJSONObject(i).getString("connName").equals(str)) {
                            isTemp=false;break;
                        }
                    }
                    if (isTemp){//不保存的临时链接
                        root.remove(temp);
                        for(int i=connectingCount;i<connecting.length;i++){
                            if(connecting[i]==null) break;
                            else connecting[i]=connecting[i+1];
                        }
                        tree.updateUI();
                    }
                    else {//记录的链接
                        MysqlConn mysqlConn = new MysqlConn();
                        rs = mysqlConn.showDB(connecting[connectingCount]);
                        while (rs.next()) {
                            temp.remove(0);
                        }
                        temp.setUserObject(str);
                        tree.updateUI();
                        connecting[connectingCount].close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
                    temp.add(itemNode[0]);
//                    for (int i = 0; i < 3; i++) {
//                        temp.add(itemNode[i]);
//                    }
                    temp.setUserObject(temp.getUserObject());
                    tree.updateUI();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
        JMenuItem tableStructure=new JMenuItem("表结构操作");
        JMenuItem deleteTable=new JMenuItem("删除选定表格");
        //JMenuItem indexs=new JMenuItem("索引管理");
        connNodeMunu=new JPopupMenu();
        connNodeMunu.add(query200);
        connNodeMunu.add(insertValue);
        connNodeMunu.add(tableStructure);
        connNodeMunu.add(deleteTable);
        //connNodeMunu.add(indexs);

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
            catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
            catch (SQLException ex){
                 JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
                        confirmTool[tabIndex].setEnabled(true);
                        cancelTool[tabIndex].setEnabled(true);
                        sqlCount[tabIndex]++;
                        Query query1=new Query();
                        try {
                            int pkCount=query1.queryPK(connecting[connectingCount], dbName, temp).length;
                            String[] pkName=query1.queryPK(connecting[connectingCount], dbName, temp);
                            if(pkCount!=0) {
                                updateSQL[sqlCount[tabIndex]] = "UPDATE " + dbName + "." + temp + " SET " + table[tabIndex].getColumnName(e.getColumn()) +
									"='" +table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()).toString() + "' WHERE ";
                                updateSQL[sqlCount[tabIndex]] += pkName[0] + "='" + tempTable[tabIndex].getValueAt(e.getLastRow(), 0) + "'";
                                for (int i = 1; i < pkCount;++i) {
                                    updateSQL[sqlCount[tabIndex]] += "AND " + pkName[i] + "='" + tempTable[tabIndex].getValueAt(e.getLastRow(), i) + "'";
                                }
                                tempTable[tabIndex].setValueAt(table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()), e.getLastRow(), e.getColumn());
                            }
                            else{
                                int result=JOptionPane.showConfirmDialog(null,"此表未设置主键，建议您设置主键，继续可能会引发更新错误，是否继续？",
                                        "未设置主键!",JOptionPane.YES_NO_OPTION);
                                if(result!=1){
                                    updateSQL[sqlCount[tabIndex]]= "UPDATE "+dbName+"."+temp+" SET "+table[tabIndex].getColumnName(e.getColumn()) +
									"='"+table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()).toString() + "' WHERE ";
                                    updateSQL[sqlCount[tabIndex]] +=table[tabIndex].getColumnName(0)+"='"+tempTable[tabIndex].getValueAt(e.getLastRow(),0)+"'";
                                    for(int i=1;i<table[tabIndex].getColumnCount();++i){
                                        updateSQL[sqlCount[tabIndex]] +="AND "+table[tabIndex].getColumnName(i)+
										"='"+tempTable[tabIndex].getValueAt(e.getLastRow(),i)+"'";
                                    }
                                    tempTable[tabIndex].setValueAt(table[tabIndex].getValueAt(e.getLastRow(), e.getColumn()), e.getLastRow(), e.getColumn());
                                }
                            }
                        }
                        catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                        }
                        confirmTool[tabIndex].addActionListener(event->{
                            Update update=new Update();
                            try {
                                update.update(updateSQL, connecting[connectingCount]);
                                updateSQL=new String[100];
                                tabRefresh(tabIndex);
                            }
                            catch (SQLException ex){
                                if(ex.getMessage().contains("Duplicate")){
                                    JOptionPane.showMessageDialog(null,"主键值不唯一","错误",JOptionPane.WARNING_MESSAGE);
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,ex.getMessage(),"错误",JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        });
                        cancelTool[tabIndex].addActionListener(event->{
                            tabRefresh(tabIndex);
                            updateSQL=new String[100];
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
                        catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                        }
                        Delete delete1=new Delete();
                        try {
                            delete1.delete(deleteSql,connecting[connectingCount]);
                            tabRefresh(tabIndex);
                            deleteTool[tabIndex].setEnabled(false);
                        }
                        catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            });
            table[tabIndex].setRowHeight(20);
            JTableHeader tableHeader = table[tabIndex].getTableHeader();
            tableHeader.setResizingAllowed(true);
            tableHeader.setReorderingAllowed(true);
            table[tabIndex].setOpaque(false);

            table[tabIndex].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            scrollPane.setViewportView(table[tabIndex]);
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
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
                updateSQL = new String[100];
                confirmTool[index].setEnabled(false);
                cancelTool[index].setEnabled(false);
                deleteTool[index].setEnabled(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private JScrollPane queryLog(ResultSet logRs,int tabIndex){
        JScrollPane logPne=new JScrollPane();
        try {
            ResultSetMetaData rsmd1 = logRs.getMetaData();
            int columnCount = rsmd1.getColumnCount();
            logRs.last();
            int rowCount = logRs.getRow();
            logRs.beforeFirst();
            Object[] title = new Object[columnCount];
            Object[][] info = new Object[rowCount][columnCount];
            for (int i = 1; i <= columnCount; i++) {
                title[i - 1] = rsmd1.getColumnName(i);
            }
            int n = 0;
            while (logRs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    info[n][i - 1] = logRs.getString(i);
                }
                n++;
            }
            table[tabIndex]=new JTable(info,title);
        }catch (SQLException e){
            e.printStackTrace();
        }
        table[tabIndex].setRowHeight(20);
        JTableHeader tableHeader = table[tabIndex].getTableHeader();
        tableHeader.setResizingAllowed(true);
        tableHeader.setReorderingAllowed(true);
        table[tabIndex].setOpaque(false);

        table[tabIndex].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        logPne.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        logPne.setViewportView(table[tabIndex]);

        confirmTool[nowTabIndex]=new JButton("提交修改");
        cancelTool[nowTabIndex]=new JButton("取消");
        deleteTool[nowTabIndex]=new JButton("删除选定记录");

        return logPne;
    }
}
