package Optimization.SqlOptimize.Service;

import Optimization.SqlOptimize.Domin.CmdExcute;
import org.json.JSONObject;

public class SoarOperation {
    CmdExcute ce=new CmdExcute();
    StringBuilder local=new StringBuilder();


    public SoarOperation(){
        String[] path= SoarOperation.class.getResource("/soar.exe").getPath()
                .replaceAll("%20"," ").split("/");
        for(int i=1;i<path.length-1;++i){
            local.append(path[i]+"\\");
        }
    }

    public String evaluateSQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\"";
        return ce.excuteCmd(cmd);
    }
    public String evaluateSQLWithDB(String sql, JSONObject object){
        StringBuilder dbInfo=new StringBuilder();
        dbInfo.append(object.getString("userName"));
        dbInfo.append(":");
        dbInfo.append(object.getString("psw"));
        dbInfo.append("@");
        dbInfo.append(object.getString("url"));
        dbInfo.append("/");
        dbInfo.append(object.getString("schema"));
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -test-dsn=\""+dbInfo.toString()+"\"";
        System.out.print(cmd);
        return ce.excuteCmd(cmd);
    }

    public String prettySQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type=pretty";
        return ce.excuteCmd(cmd);
    }
    public String mergeAlter(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type rewrite -rewrite-rules mergealter";
        return ce.excuteCmd(cmd);
    }
    public String rewriteSQL(String sql){
        String cmd="echo "+sql+" | \""+local.toString()+"soar\" -report-type rewrite ";
        return ce.excuteCmd(cmd);
    }
}
