package Optimization.ParameterOpt.domin;

import java.sql.Connection;
import java.util.LinkedHashMap;

public class MainThread extends Thread {
    Connection conn;
    String[] sql;

    public MainThread(Connection conn,String[] sql){
        this.conn=conn;this.sql=sql;
    }

    @Override
    public void run(){
        TestThread testThread=new TestThread();
        LinkedHashMap res=testThread.initTest(sql,conn);
        ParaEvaluation paraEvaluation=new ParaEvaluation();
        paraEvaluation.showRes(res);
    }
}
