package Optimization.SqlOptimize.domin;

import Starter.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SoarLog {
    public void writeLog(String text,String cmd) throws IOException {
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG");
        File file = new File(path.toString());
        File file1 = new File(path.toString() +"/optimizeLog.log");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            file1.createNewFile();
        }
        StringBuilder nonPwdCmd = new StringBuilder();
        if(cmd.contains("test-dsn=")) {
            String[] temp=cmd.split("test-dsn=");
            nonPwdCmd.append(temp[0]).append("test-dsn=");
            char[] chars = temp[1].toCharArray();
            boolean flag = false;boolean first=true;
            for (int i = 0; i < chars.length; ++i) {
                if (chars[i] == '@') flag = false;
                if (flag) nonPwdCmd.append("*");
                else nonPwdCmd.append(chars[i]);
                if (chars[i] == ':'&& first) {flag = true; first=false;}
            }
        }else nonPwdCmd.append(cmd);
        Writer out = new FileWriter(file1, true);
        out.write(text + "\r\n" + "command:"+  nonPwdCmd.toString());
        out.close();
    }
    public void readLog(){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/optimizeLog.log");
        try {
            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + path.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
