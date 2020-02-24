package MysqlOperation.domin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Script {
    public String readScript(File path){
        StringBuilder res=new StringBuilder();
        try {
            Scanner sc = new Scanner(path);
            while (sc.hasNext()){
                res.append(sc.nextLine());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res.toString();
    }

    public List<ResultSet> multiEx(File path, Connection conn) throws SQLException {
        String text=readScript(path);

        String[] sql=text.split(";");

        List<ResultSet> rs=new ArrayList<>();

        Customize customize=new Customize();
        for(int i=0;i<sql.length;++i){
            rs.add(customize.excuteSql(conn,sql[i]));
        }
        return rs;
    }
}
