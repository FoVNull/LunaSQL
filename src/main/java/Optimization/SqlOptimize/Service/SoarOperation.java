package Optimization.SqlOptimize.Service;

import Optimization.SqlOptimize.domin.CmdExcute;
import org.json.JSONObject;

public class SoarOperation {
    CmdExcute ce=new CmdExcute();
    StringBuilder local=new StringBuilder();
    String type="soar";
    String cusName;

    public SoarOperation(String cusName){
        String[] path= this.getClass().getClassLoader().getResource("soar.exe").getPath()
                .replaceAll("%20"," ").split("/");
        for(int i=1;i<path.length-2;++i){
            local.append(path[i]+"\\");
        }local.append("classes\\");

        this.cusName=cusName;
    }

    public String mergePath(JSONObject object){
        StringBuilder dbInfo=new StringBuilder();
        dbInfo.append(object.getString("userName"));
        dbInfo.append(":");
        dbInfo.append(object.getString("psw"));
        dbInfo.append("@");
        dbInfo.append(object.getString("url"));
        dbInfo.append("/");
        dbInfo.append(object.getString("schema"));
        return dbInfo.toString();
    }

    public String evaluateSQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\"";
        return ce.excuteCmd(cmd,type,cusName);
    }
    public String evaluateSQLWithDB(String sql, JSONObject object){
        String dbInfo=mergePath(object);
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -test-dsn=\""+dbInfo+"\" -allow-online-as-test";
        return ce.excuteCmd(cmd,type,cusName);
    }

    public String prettySQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type=pretty";
        return ce.excuteCmd(cmd,type,cusName);
    }
    public String mergeAlter(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type rewrite -rewrite-rules mergealter";
        return ce.excuteCmd(cmd,type,cusName);
    }
    public String rewriteSQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type rewrite ";
        return ce.excuteCmd(cmd,type,cusName);
    }
    public String rewriteSQLWithDB(String sql, JSONObject object){
        String dbInfo=mergePath(object);
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type rewrite -test-dsn=\""+dbInfo+"\" -allow-online-as-test";
        return ce.excuteCmd(cmd,type,cusName);
    }

}
