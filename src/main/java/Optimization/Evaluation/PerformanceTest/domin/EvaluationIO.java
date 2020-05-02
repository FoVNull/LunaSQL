package Optimization.Evaluation.PerformanceTest.domin;

import Starter.Location;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class EvaluationIO {
//    public static void main(String...args){
//        EvaluationIO eio=new EvaluationIO();
//    }
    public String[] readLast(String cusName){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/").append(cusName);
        File floder = new File(path.toString());
        floder.mkdir();
        path= Location.Path.getPath();
        path.append("classes/LunaLOG/").append(cusName).append("/Evaluation.csv");
        File file = new File(path.toString());
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

    public void writeLast(int[] input,String cusName){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/").append(cusName).append("/Evaluation.csv");
        File file = new File(path.toString());
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

    public void checkHistory(String cusName){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/").append(cusName).append("/Evaluation.csv");
        try {
            Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\notepad.exe " +
                    path.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setDefault(String sql){
        System.out.println(sql);
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/defaultcase.txt");
        File file = new File(path.toString());
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            Scanner sc=new Scanner(file);
            String str="";
            while(sc.hasNext()){
                str+=sc.nextLine()+"\r\n";
            }
            String[] last=str.split("\r\n");
            last[0]=sql;
            StringBuilder input=new StringBuilder();
            for(String s:last) input.append(s).append("\r\n");
            Writer writer=new FileWriter(file);
            writer.append(input.toString());
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String readDefault() {
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/defaultcase.txt");
        File file = new File(path.toString());
        String res="";
        try {
            if (!file.exists()) {
                file.createNewFile();
                Writer writer=new FileWriter(file);
                writer.append("SELECT * FROM information_schema.`COLUMNS` c JOIN information_schema.`TABLES` t ON c.TABLE_NAME=t.TABLE_NAME ORDER BY c.ORDINAL_POSITION"+"\r\n");
                writer.append("NAN"+"\r\n");writer.append("NAN"+"\r\n");writer.append("NAN"+"\r\n");writer.append("NAN"+"\r\n");
                writer.close();
            }
            Scanner sc=new Scanner(file);
            res=sc.nextLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }

    public String[] readUserCase() {
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/defaultcase.txt");
        File file = new File(path.toString());
        String[] res=new String[4];
        try {
            Scanner sc = new Scanner(file);
            String str = "";
            while (sc.hasNext()) {
                str += sc.nextLine() + "\r\n";
            }
            String[] last = str.split("\r\n");
            if(last.length>1){
                for(int i=1;i<5;++i) res[i-1]=last[i];
            }else{
                for(int i=0;i<4;++i) res[i]="NAN";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }

    public void saveUserCase(String[] sql){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/defaultcase.txt");
        File file = new File(path.toString());
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder str = new StringBuilder();
            String s="";
            while ((s = br.readLine())!=null) {
                str.append(s).append("\r\n");
            }br.close();
            String[] last = str.toString().split("\r\n");
            StringBuilder input= new StringBuilder(last[0] + "\r\n");
            for(int i=0;i<4;++i) input.append(sql[i]).append("\r\n");

            Writer writer=new FileWriter(file);
            writer.append(input.toString());
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
