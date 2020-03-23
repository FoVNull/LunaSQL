package Optimization.Evaluation.PerformanceTest.domin;

import Starter.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SlapLog {
    public void writeLog(String text,String cmd) throws IOException {
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG");
        File file = new File(path.toString());
        File file1 = new File(path.toString() +"/slap.log");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            file1.createNewFile();
        }

        StringBuilder nonPwdCmd=new StringBuilder();
        char[] chars=cmd.toCharArray();
        boolean flag=false;
        for(int i=0;i<chars.length-1;++i){
            if(flag){
                if(chars[i]!=' ') nonPwdCmd.append("*");
                else {flag=false;nonPwdCmd.append(chars[i]);}
            }else{
                nonPwdCmd.append(chars[i]);
            }
            StringBuilder temp=new StringBuilder();
            temp.append(chars[i]).append(chars[i+1]);
            if(temp.toString().equals("-p")) flag=true;
        }
        nonPwdCmd.append(chars[chars.length-1]);
        System.out.println(nonPwdCmd);
        Writer out = new FileWriter(file1, true);
        out.write(text + "\r\n" + "command:"+ nonPwdCmd.toString());
        out.close();
    }
    public void readLog(){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/slap.log");
        try {
            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " + path.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
