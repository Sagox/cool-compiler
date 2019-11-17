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

    enum TypeID {
        EMPTY, VOID, INT1, INT1PTR, INT1DOUBLEPTR, INT8, INT8PTR, INT8DOUBLEPTR, INT32, INT32PTR, INT32DOUBLEPTR, VARARG, OBJ, OBJPTR, OBJDOUBLEPTR, CLASS
    }

    TypeID gt;
    int pointerDepth = 0;
    TypeUtils(TypeUtils.TypeID t) {
        gt = t;
    }

    TypeUtils(TypeUtils.TypeID t, String cname, int pd) {
        gt = t;
        name = cname;
        pointerDepth = pd;
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
}
