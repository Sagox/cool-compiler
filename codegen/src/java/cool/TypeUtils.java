package cool;

import java.util.*;
import java.io.PrintWriter;

/*
	Utility Class to help deal with eveything related to types, converting a type to the
	corresponding IR code, type conversion from cool->LLVM IR, cool -> Standard C functions, etc.
*/

public class TypeUtils {
	// All the types
    enum TypeID {
        EMPTY, VOID, INT1, INT1PTR, INT1DOUBLEPTR, INT8, INT8PTR, INT8DOUBLEPTR, INT32, INT32PTR, INT32DOUBLEPTR, VARARG, OBJ, OBJPTR, OBJDOUBLEPTR
    }

    static String getIRRep(TypeID t) {
    	switch(t) {
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
    	}
    	return "";
    }
}
