package Optimization.SqlOptimize.Domin;

import Optimization.SqlOptimize.Service.SoarOperation;
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
        Writer out = new FileWriter(file1, true);
        out.write(text + "\r\n" + "command:"+ cmd);
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
