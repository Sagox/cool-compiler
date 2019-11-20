package cool;

import java.util.*;
import java.io.PrintWriter;

/*
	Utility Class to help deal with eveything related to types, converting a type to the
	corresponding IR code, type conversion from cool->LLVM IR, cool -> Standard C functions, etc.
*/

public class TypeUtils {
	// All the types

    String name;

    enum Typegt {
        EMPTY, VOID, INT1, INT1PTR, INT1DOUBLEPTR, INT8, INT8PTR, INT8DOUBLEPTR, INT32, INT32PTR, INT32DOUBLEPTR, VARARG, OBJ, OBJPTR, OBJDOUBLEPTR, CLASS
    }

    Typegt gt;
    int pointerDepth = 0;
    TypeUtils(TypeUtils.Typegt t) {
        gt = t;
        name = "";
        pointerDepth = 0;
    }

    TypeUtils(TypeUtils.Typegt t, String cname, int pd) {
        gt = t;
        name = cname;
        pointerDepth = pd;
    }

    // ask ask
    TypeUtils(String cname, int pd) {
        gt = Typegt.OBJ;
        pointerDepth = pd;
        name = cname;
        if(pd == 1) {
            gt = Typegt.OBJPTR;
        }
    }

    static String getIRRep(TypeUtils t) {
        String ps = "*";
    	switch(t.gt) {
    		case EMPTY:
    			return "";
    		case VOID:
    			return "void";
    		case INT1:
    			return "i1";
    		case INT1PTR:
    			return "i1*";
    		case INT1DOUBLEPTR:
    			return "i1**";
    		case INT8:
    			return "i8";
    		case INT8PTR:
    			return "i8*";
    		case INT8DOUBLEPTR:
    			return "i8**";
    		case INT32:
    			return "i32";
    		case INT32PTR:
    			return "i32*";
    		case INT32DOUBLEPTR:
    			return "i32**";
    		case VARARG:
    			return "...";
            case CLASS:
                return "%" + t.name + ps.repeat(t.pointerDepth);
    	}
    	return "";
    }

    public TypeUtils getPtr() {
        Typegt retPtr = Typegt.EMPTY;
        if(gt == Typegt.INT1) {
            return (new TypeUtils(Typegt.INT1PTR));
        } else if(gt == Typegt.INT8) {
            return (new TypeUtils(Typegt.INT8PTR));
        } else if(gt == Typegt.INT32) {
            return (new TypeUtils(Typegt.INT32PTR));
        } else if(gt == Typegt.INT1PTR) {
            return (new TypeUtils(Typegt.INT1DOUBLEPTR));
        } else if(gt == Typegt.INT8PTR) {
            return (new TypeUtils(Typegt.INT8DOUBLEPTR));
        } else if(gt == Typegt.INT32PTR) {
            return (new TypeUtils(Typegt.INT32DOUBLEPTR));
        } else if(gt == Typegt.OBJ) {
            retPtr = Typegt.OBJPTR;
        } else if(gt == Typegt.OBJPTR) {
            retPtr = Typegt.OBJDOUBLEPTR;
        } else if(gt == Typegt.CLASS)
            retPtr = Typegt.CLASS;
        // remove first character (%)
        TypeUtils newType = new TypeUtils(name, 1);
        newType.gt = retPtr;
        return newType;
    }
}
