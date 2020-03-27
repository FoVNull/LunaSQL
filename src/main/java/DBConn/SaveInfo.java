package DBConn;


import Starter.Location;

import java.io.*;

public class SaveInfo {
    ReadInfo ri=new ReadInfo();
    public boolean saveLogin(String url,String userName,String psw,String connName,String dbType) throws IOException{
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG");
        File file = new File(path.toString());
        File file1 = new File(path.toString() +"/connInfo.txt");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            file1.createNewFile();
        }

        psw=confuse(psw,connName.length());

        int test=0;
        for(int i=0;i<ri.readInfo().length();i++) {
            if(ri.readInfo().getJSONObject(i).getString("connName").equals(connName)){
                test=1;break;
            }
        }
        if(test==0) {
            Writer out = new FileWriter(file1, true);
            out.write(connName + " " + url + " " + userName + " " + psw + " " + dbType + "\r\n");
            out.close();
            return true;
        }
        else {
            return false;
        }
    }

    public String confuse(String psw,int len){
        char[] chars=psw.toCharArray();
        StringBuilder stb=new StringBuilder();

        for(int i=0;i<chars.length;++i){
            chars[i]=(char)(chars[i]+len+i);
            stb.append(chars[i]);
        }

        return stb.toString();
    }

    public void deleteConn(int i){
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/connInfo.txt");
        File file = new File(path.toString());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;int count=0;
            String res="";
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                if(count!=i)  res+=s+"\r\n";
                ++count;
            }
            Writer out = new FileWriter(file);
            out.write(res);
            out.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
