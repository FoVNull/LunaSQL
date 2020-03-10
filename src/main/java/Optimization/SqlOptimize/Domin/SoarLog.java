package Optimization.SqlOptimize.Domin;

import Optimization.SqlOptimize.Service.SoarOperation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SoarLog {
    public void writeLog(String text,String cmd) throws IOException {
        File file = new File("C:" + File.separator + "LunaSQL");
        File file1 = new File("C:"+ File.separator + "LunaSQL"+ File.separator +"optimizeLog.log");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            file1.createNewFile();
        }
        Writer out = new FileWriter(file1, true);
        out.write(text + "\r\n" + "命令:"+ cmd);
        out.close();
    }
    public void readLog(){
        String root="C:" + File.separator + "LunaSQL" + File.separator + "optimizeLog.log";
        try {
            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + root);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
