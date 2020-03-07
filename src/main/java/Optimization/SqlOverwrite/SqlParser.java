package Optimization.SqlOverwrite;

import Optimization.SqlOverwrite.Entity.JoinStruct;
import Optimization.SqlOverwrite.Enums.Keywords;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.*;


public class SqlParser {
    public static void main(String...args){
        SqlParser sqlParser=new SqlParser();
        try {
            //String sql = "SELECT * FROM TABLE1 WHERE NOT x=(SELECT y FROM t2 WHERE y=3)";
            String sql="SELECT * FROM TABLE1 WHERE NOT (x>=(SELECT y FROM t2 WHERE NOT (y=(SELECT z FROM t3 WHERE z=3))))";
            //String sql="SELECT * FROM TABLE1";
            sql="SELECT a.z FROM A a LEFT JOIN B b ON a.x=b.y RIGHT JOIN C c ON c.x=b.x WHERE b.z=1;";
            //System.out.print(sqlParser.replaceNot(sql));
            sqlParser.joinOptimize(sql);
        }catch (JSQLParserException e){
            e.printStackTrace();
        }
    }

    public String replaceNot(String sql) throws JSQLParserException {
        StringBuilder res=new StringBuilder();

        Statement stat = CCJSqlParserUtil.parse(sql);
        Select select = (Select) stat;
        SelectBody selectBody = select.getSelectBody();
        PlainSelect ps = (PlainSelect) selectBody;


        String[] strExp=ps.toString().split(" ");
        boolean ifNot=false;
        for(int i=0;i<strExp.length;++i) {
            if(strExp[i].equals("NOT")||strExp[i].equals("not")) {
                ifNot=true;
            }else{
                Keywords kw = null;
                for (Keywords k : Keywords.values()) {
                    if (k.getName().equals(strExp[i])) {
                        kw = k;break;
                    }
                }
                if(kw!=null && ifNot) {
                    strExp[i] = Keywords.getOpposite(kw).getName();//运算符取反值
                    ifNot=false;
                }
                res.append(strExp[i]+" ");
            }
        }
        return res.toString();
    }

    public String joinOptimize(String sql)throws JSQLParserException{
        StringBuilder res=new StringBuilder();

        try{//判断是否是含连接的sql语句
            Statement stat = CCJSqlParserUtil.parse(sql);
            Select select = (Select) stat;
            SelectBody selectBody = select.getSelectBody();
            PlainSelect ps = (PlainSelect) selectBody;

            String prefix=ps.toString().split("FROM")[0];
            res.append(prefix+"FROM ");

            FromItem fromItem=ps.getFromItem();
            List<Join> joinList=ps.getJoins();

            int size=joinList.size();
            for(int i=0;i<size;++i) {
                Join j = joinList.get(i);
                if (j.isRight()) {
                    if (i == 0) {
                        FromItem temp = j.getRightItem();
                        j.setRightItem(fromItem);
                        fromItem = temp;
                    }else{
                        FromItem temp=j.getRightItem();
                        Join lastJ=joinList.get(i-1);
                        j.setRightItem(lastJ.getRightItem());
                        lastJ.setRightItem(temp);
                    }
                    Expression onExp=j.getOnExpression();
                    String[] strOnExp=onExp.toString().split("=");

                    j.setRight(false);j.setLeft(true);
                    res.append(j.toString().split("ON")[0]+"ON"+strOnExp[1]+" = "+strOnExp[0]);
                }else{
                    res.append(j.toString()+" ");
                }
            }
            joinList.stream().forEach(System.out::println);

            try{
                String[] strs=ps.getWhere().toString().split(" ");
                Keywords kw = null;
                for(String s:strs) {
                    for (Keywords k : Keywords.values()) {
                        if (k.getName().equals(s)) {
                            kw = k;
                            break;
                        }
                    }
                    if(kw!=null) break;
                }

            }catch (NullPointerException ex){
                ex.printStackTrace();
            }

            //res.append();
        }catch (JSQLParserException e){
            return sql;
        }

        return res.toString();
    }

}
