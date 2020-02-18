package Optimization.ParameterOpt.domin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class EvaluationIO {
//    public static void main(String...args){
//        EvaluationIO eio=new EvaluationIO();
//        eio.readLast();
//    }
    public String[] readLast(){
        File file = new File("C:" + File.separator + "LunaSQL" + File.separator + "Evaluation.csv");
        try {
            if (!file.exists()) {
                file.createNewFile();
                Writer out = new FileWriter(file,true);
                out.append("join_buffer(ms),read_buffer(ms),sort_buffer(ms),key_buffer(ms),Date," +
                           "join_buffer(KB),read_buffer(KB),sort_buffer(KB),key_buffer(KB)"+"\r\n");
                out.append("0,0,0,0,default,0,0,0,0"+"\r\n");
                out.close();
                return new String[]{"0","0","0","0","default","0","0","0","0"};
            }else{
                Scanner sc=new Scanner(file);
                String s = "";
                while (sc.hasNext()){
                    s=sc.nextLine();
                }
                return s.split(",");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void writeLast(int[] input){
        File file = new File("C:" + File.separator + "LunaSQL" + File.separator + "Evaluation.csv");
        try {
            Writer out = new FileWriter(file,true);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out.append(input[0] + "," + input[1] + "," + input[2] + "," + input[3]+","+ df.format(new Date())+","+
                          input[4] + "," + input[5] + "," + input[6] + "," + input[7]+"\r\n");
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void checkHistory(){
        try {
            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " +
                    "C:\\LunaSQL\\Evaluation.csv");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
