package MysqlOperation.Service;

import MysqlOperation.domin.Define;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;

public class CreateService {
    public boolean buildSQL(JSONArray columns, Connection conn){
        ArrayList<String> pkList=new ArrayList<>();
        ArrayList<String> uniqList=new ArrayList<>();
        String dbName=columns.getString(0);
        String tableName=columns.getString(1);

        StringBuilder sql=new StringBuilder();
        sql.append("CREATE TABLE ").append(dbName).append(".").append(tableName).append("(");

        int len=columns.length();
        for(int i=2;i<len;++i){
            JSONObject object=columns.getJSONObject(i);
            String name=object.getString("name");
            String type=object.getString("type");
            sql.append(name).append(" ").append(type);
            if(object.getBoolean("ifPK")) pkList.add(name);
            if(object.getBoolean("ifUniq")) uniqList.add(name);
            if(object.getBoolean("ifNull")||object.getBoolean("ifPK")) sql.append(" NOT NULL,");
            else sql.append(",");
        }
        int pkLen=pkList.size();
        if(pkLen>0) {
            sql.append("PRIMARY KEY(");
            for (int i = 0; i < pkLen - 1; ++i) {
                sql.append(pkList.get(i)).append(",");
            }
            sql.append(pkList.get(pkLen - 1)).append(")");
        }

        int uniLen=uniqList.size();
        if(uniLen>0) {
            sql.append("UNIQUE INDEX ");
            for (int i = 0; i < uniLen - 1; ++i) {
                String temp = uniqList.get(i);
                sql.append("unique_").append(temp).append("(")
                        .append(temp).append(" ASC) VISIBLE,");
            }
            sql.append("unique_").append(uniqList.get(uniLen - 1)).append("(")
                    .append(uniqList.get(uniLen - 1)).append(" ASC) VISIBLE");
        }


        sql.append(")");

        Define define=new Define();
        return define.createTable(conn,sql.toString());
    }
}
