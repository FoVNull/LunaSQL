package Optimization.SqlOverwrite.Enums;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public enum Keywords {
    EQUAL("="),UNEQUAL("!="),GREATER(">"),LESS("<"),GREATER_E(">="),LESS_E("<=");

    private String name;

    Keywords(String name){
        this.name=name;
    }
    public String getName(){return name;}
    public String getUpName(){return name.toUpperCase();}
    public String getLowName(){return name.toLowerCase();}


    public static Keywords getOpposite(Keywords symbol){
        Keywords res;

        res=switch (symbol){
            case EQUAL -> UNEQUAL;
            case UNEQUAL -> EQUAL;
            case GREATER -> LESS;
            case LESS -> GREATER;
            case GREATER_E -> LESS_E;
            case LESS_E -> GREATER_E;
            default -> throw new IllegalStateException("Unexpected value: " + symbol.getName());
        };

        return res;
    }
}
