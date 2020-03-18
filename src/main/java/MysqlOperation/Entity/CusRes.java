package MysqlOperation.Entity;

import java.sql.ResultSet;

public class CusRes {
    private static Integer type;
    private static ResultSet rs;
    private static Exception ex;

    public Exception getCusResEx() {
        return ex;
    }

    public Integer getCusResType() {
        return type;
    }

    public ResultSet getCusResRs() {
        return rs;
    }

    public void setCusRes(Integer type,ResultSet rs,Exception ex) {
        CusRes.ex = ex;
        CusRes.type = type;
        CusRes.rs = rs;
    }
}
