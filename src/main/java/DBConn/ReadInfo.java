package DBConn;

import Starter.Location;
import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadInfo {
    public static JSONArray readInfo() {
        JSONObject urlObject = new JSONObject();
        JSONArray urlArray = new JSONArray();
        StringBuilder path= Location.Path.getPath();
        path.append("classes/LunaLOG/connInfo.txt");
        File file = new File(path.toString());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                String[] str = s.split(",");
                JSONObject object = new JSONObject();
                object.put("connName", str[0]);
                object.put("url", str[1]);
                object.put("userName", str[2]);
                object.put("psw", str[3]);
                object.put("type",str[4]);
                urlArray.put(object);
            }
            br.close();
            urlObject.put("url", urlArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlArray;
    }
}
