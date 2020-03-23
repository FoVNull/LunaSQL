package Optimization.Evaluation.PerformanceTest.Service;

import Optimization.Evaluation.PerformanceTest.View.ParaEvaluation;
import Optimization.Evaluation.ParameterOpt.domin.EvaluationIO;

import java.sql.Connection;

import java.util.HashMap;
import java.util.ArrayList;

public class MainThread extends Thread {
    Connection conn;
    String[] sql;
    int testType;

    public static HashMap<Integer,String[]> map;
    public static int sumOfMulti;
    public static boolean stopFlag;

    public MainThread(Connection conn,String[] sql,int testType){
        this.conn=conn;this.sql=sql;this.testType=testType;
    }

    @Override
    public void run(){

        if(testType!=2) {
            OptFunction optFunction = new OptFunction();
            HashMap<Integer, String[]> res = optFunction.initTest(sql, conn, testType);
            if(!res.isEmpty()) {
                ParaEvaluation paraEvaluation = new ParaEvaluation();
                paraEvaluation.showRes(res, conn);
            }
        }else {
            multiSql();
            if(!stopFlag) {
                ParaEvaluation paraEvaluation = new ParaEvaluation();
                paraEvaluation.showRes(map, conn);
            }
        }
    }

    public void multiSql(){
        map=new HashMap<>();
        sumOfMulti=0;
        stopFlag=false;

        ArrayList<Thread> queue=new ArrayList<>();

        for(int j=0;j<sql.length;++j) {
            OptimizeThread opt=new OptimizeThread(conn,sql[j],j);
            queue.add(opt);
            opt.start();
        }
        try{
            for(Thread t:queue){
                t.join();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        int avg=sumOfMulti/sql.length;

        EvaluationIO eio=new EvaluationIO();
        String[] t=eio.readLast();

        for(int i=0;i<4;++i) {
            String[] temp={t[i],avg+"",t[4]};
            map.put(i,temp);
        }
    }
}
