package Optimization.SqlOptimize.Domin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CmdExcute {
    public String excuteCmd(String cmd){
        StringBuilder res=new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.append("\r\n"+"=".repeat(20)+df.format(new Date())+"\r\n");
        try{
            Process p = Runtime.getRuntime().exec("cmd.exe /c "+cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line="";
            while ((line = br.readLine()) != null) {
               res.append(line + "\n");
            }
            br.close();
            SoarLog sl=new SoarLog();
            sl.writeLog(res.toString(),cmd);
        }catch (IOException e){
            e.printStackTrace();
        }
        return res.toString();
    }
}
