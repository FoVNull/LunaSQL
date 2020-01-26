package DBConn;

import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

public class ReadInfo {
//    public static void main(String[] args){
//        //System.out.println(readInfo().getJSONObject(0).getString("connName"));
//        System.out.println(readInfo());
//    }
    public static JSONArray readInfo() {
        JSONObject urlObject = new JSONObject();
        JSONArray urlArray = new JSONArray();
        File file = new File("C:" + File.separator + "LunaSQL" + File.separator + "connInfo.txt");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlArray;
    }
}
