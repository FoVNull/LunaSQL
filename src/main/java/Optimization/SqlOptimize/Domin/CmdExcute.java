package Optimization.SqlOptimize.domin;

import Optimization.Evaluation.PerformanceTest.domin.SlapLog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CmdExcute {
    public String excuteCmd(String cmd,String type){
        StringBuilder res=new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.append("\r\n").append("=".repeat(20)).append(df.format(new Date())).append("\r\n");
        try{
            Process p = Runtime.getRuntime().exec("cmd.exe /c "+cmd);
            //win↑  linux↓
            //Process p = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),StandardCharsets.UTF_8));
            String line="";
            while ((line = br.readLine()) != null) {
                res.append(line).append("\n");
            }
            br.close();
            if(type.equals("soar")) {
                SoarLog sl = new SoarLog();
                sl.writeLog(res.toString(), cmd);
            }else{
                SlapLog sl=new SlapLog();
                sl.writeLog(res.toString(),cmd);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res.toString();
    }
}
