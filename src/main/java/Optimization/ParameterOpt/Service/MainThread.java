package Optimization.ParameterOpt.Service;

import Optimization.ParameterOpt.View.ParaEvaluation;

import java.sql.Connection;
import java.util.LinkedHashMap;

public class MainThread extends Thread {
    Connection conn;
    String[] sql;
    int testType;

    public MainThread(Connection conn,String[] sql,int testType){
        this.conn=conn;this.sql=sql;this.testType=testType;
    }

    @Override
    public void run(){
        TestThread testThread=new TestThread();
        LinkedHashMap<Integer,String[]> res=testThread.initTest(sql,conn,testType);
        if(res!=null) {
            ParaEvaluation paraEvaluation = new ParaEvaluation();
            paraEvaluation.showRes(res, conn);
        }
    }
}
