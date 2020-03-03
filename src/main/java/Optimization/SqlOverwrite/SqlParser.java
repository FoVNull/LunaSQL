package Optimization.SqlOverwrite;

import Optimization.SqlOverwrite.Enums.Keywords;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.util.*;


public class SqlParser {
    public static void main(String...args){
        SqlParser sqlParser=new SqlParser();
        try {
            //String sql = "SELECT * FROM TABLE1 WHERE NOT x=(SELECT y FROM t2 WHERE y=3)";
            String sql="SELECT * FROM TABLE1 WHERE NOT (x>=(SELECT y FROM t2 WHERE NOT (y=(SELECT z FROM t3 WHERE z=3))))";
            //String sql="SELECT * FROM TABLE1";
            System.out.print(sqlParser.replaceNot(sql));
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
        Stack<String> stack=new Stack<>();
        for(int i=0;i<strExp.length;++i) {
            if(!stack.isEmpty()) System.out.print(stack.peek()+"/");
            if(strExp[i].equals("NOT")||strExp[i].equals("not")) {
                stack.push(strExp[i]);
            }else{
                Keywords kw = null;
                for (Keywords k : Keywords.values()) {
                    if (k.getName().equals(strExp[i])) {
                        kw = k;break;
                    }
                }
                if(kw!=null && !stack.isEmpty()) {
                    strExp[i] = Keywords.getOpposite(kw).getName();//运算符取反值
                    stack.pop();
                }
                res.append(strExp[i]+" ");
            }
        }
        System.out.print("\n");
        return res.toString();
    }
}
