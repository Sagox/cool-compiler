package cool;

import java.io.PrintWriter;
import java.util.*;

public class CoolConstants extends ArgumentInfo {
    public String constValue;
    CoolConstants(String valTemp, TypeUtils t) {
        super(valTemp, t);
        constValue = valTemp;
    }
}