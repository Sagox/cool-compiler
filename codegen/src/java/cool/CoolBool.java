package cool;

import java.util.*;
import java.io.PrintWriter;

public class CoolBool extends CoolConstants {
    public boolean bValParam;
    CoolBool(boolean bTemp) {
        super("", new TypeUtils(TypeUtils.TypeID.INT1));
        bValParam = bTemp;
        if(bTemp)
            constValue = "true";
        else
            constValue = "false";
        name = constValue;
    }
}