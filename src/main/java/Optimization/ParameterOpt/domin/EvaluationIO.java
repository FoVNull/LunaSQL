package Optimization.ParameterOpt.domin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EvaluationIO {
    public String[] readLast(){
        File file = new File("C:" + File.separator + "LunaSQL" + File.separator + "Evaluation.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
                Writer out = new FileWriter(file);
                out.write("0,0,0,0,default");
                out.close();
                return new String[]{"0","0","0","0","default"};
            }else{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String s = "";
                while ((s = br.readLine()) != null) {
                    br.close();
                    return s.split(",");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void writeLast(int[] input){
        File file = new File("C:" + File.separator + "LunaSQL" + File.separator + "Evaluation.txt");
        try {
            Writer out = new FileWriter(file);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            out.write(input[0] + "," + input[1] + "," + input[2] + "," + input[3]+","+ df.format(new Date()));
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
