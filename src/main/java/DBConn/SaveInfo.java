package DBConn;


import Starter.Location;

import java.io.*;

public class SaveInfo {
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
        int test=0;
        for(int i=0;i<ReadInfo.readInfo().length();i++) {
            if(ReadInfo.readInfo().getJSONObject(i).getString("connName").equals(connName)){
                test=1;break;
            }
        }
        if(test==0) {
            Writer out = new FileWriter(file1, true);
            out.write(connName + "," + url + "," + userName + "," + psw + "," + dbType + "\r\n");
            out.close();
            return true;
        }
        else {
            return false;
        }
    }
}
