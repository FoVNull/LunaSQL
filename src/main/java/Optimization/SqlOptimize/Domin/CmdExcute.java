package Optimization.SqlOptimize.domin;

import Optimization.Evaluation.PerformanceTest.domin.SlapLog;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CmdExcute {
    public String excuteCmd(String cmd,String type){
        String res;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res="\r\n"+"=".repeat(20)+df.format(new Date())+"\r\n";//使用字符串拼接而非stringbuilder是因为字符编码问题
        try{
            Process p = Runtime.getRuntime().exec("cmd.exe /c "+cmd);
            //win↑  linux↓
            //Process p = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line="";
            while ((line = br.readLine()) != null) {
               res+=line + "\n";
            }
            br.close();
            if(type.equals("soar")) {
                SoarLog sl = new SoarLog();
                sl.writeLog(res, cmd);
            }else{
                SlapLog sl=new SlapLog();
                sl.writeLog(res,cmd);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }
}
