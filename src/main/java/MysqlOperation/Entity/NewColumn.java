package MysqlOperation.Entity;

import javax.swing.*;
import java.awt.*;

public class NewColumn {
    public JTextField name;
    public JComboBox type;
    public JCheckBox ifPK;
    public JCheckBox ifNull;
    public JCheckBox ifUniq;

    public NewColumn(){
        String[] typeName={"INT","TINYINT","FLOAT","DOUBLE","CHAR","VARCHAR(45)","TINYTEXT","TEXT","LONGTEXT","TINYBLOB","BLOB",
                "DATE","TIME","YEAR","DATETIME","TIMESTAMP"};
        name=new JTextField();
        type=new JComboBox(typeName);
        ifPK=new JCheckBox();
        ifNull=new JCheckBox();
        ifUniq=new JCheckBox();
    }

    public Component[] getComponent(){
        Component[] objects={name,type,ifPK,ifNull,ifUniq};
        return objects;
    }
}
