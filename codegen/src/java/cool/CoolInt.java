package cool;

import java.util.*;
import java.io.PrintWriter;

public class CoolInt extends CoolConstants {
    public int intValParam;
    CoolInt(int i) {
        super(String.valueOf(i), new TypeUtils(TypeUtils.Typegt.INT32));
        intValParam = i;
    }
}
