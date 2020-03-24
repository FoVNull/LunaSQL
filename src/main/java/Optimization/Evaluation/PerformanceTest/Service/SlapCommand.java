package Optimization.Evaluation.PerformanceTest.Service;

import Optimization.SqlOptimize.domin.CmdExcute;
import org.json.JSONObject;

public class SlapCommand {
    public String generateCommand(JSONObject option,JSONObject db){
        StringBuilder cmd=new StringBuilder();
        cmd.append("mysqlslap");

        String[] url=db.getString("url").split(":");
        cmd.append(" -h").append(url[0]);
        if(url.length>1) cmd.append(" -P").append(url[1]);
        cmd.append(" -u").append(db.getString("userName"));
        cmd.append(" -p").append(db.getString("psw"));
        cmd.append(" --create-schema=").append(db.getString("schema"));


        String query=option.getString("query");
        if((boolean)option.get("ifAuto")) cmd.append(" -a");
        else if(!query.equals("")) cmd.append(" --query=\"").append(query).append("\"");

        String c=option.getString("concurrency");
        if(!c.equals("")) cmd.append(" --concurrency=").append(c);
        String q=option.getString("qNum");
        if(!q.equals("")) cmd.append(" --number-of-queries=").append(q);
        String i=option.getString("iterations");
        if(!i.equals("")) cmd.append(" --iterations=").append(i);
        String e=option.getString("engine");
        if(!e.equals("")) cmd.append(" --engine=").append(e);

        if((boolean)option.get("ifDebug")) cmd.append(" --debug-info");
        if((boolean)option.get("ifPrint")) cmd.append(" --only-print");

        CmdExcute ce=new CmdExcute();
        return ce.excuteCmd(cmd.toString(),"slap");

    }
}
