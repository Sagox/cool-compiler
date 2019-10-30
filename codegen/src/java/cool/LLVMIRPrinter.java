package cool;

import java.io.PrintWriter;
import java.util.*;

public class LLVMIRPrinter {
	PrintWriter out;

	LLVMIRPrinter(PrintWriter tofile) {
		out = tofile;
	}

	void printMetaData(AST.program program) {
		String filename = program.classes.get(0).filename;
		// assumed to be same as filename
		out.println("; ModuleID = \""+filename+"\"");
		out.println("source_filename = \""+filename+"\"");
		// layout is in the following format
		// little endian | name mangling | int size and alignment | float size and alignment
		// | native integer widths supported | stack allignment
		out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"");
		out.println("target triple = \"x86_64-unknown-linux-gnu\"");
	}

	void printDeclaration(ArrayList<TypeUtils.TypeID> args, TypeUtils.TypeID retType, String name) {
        out.print("declare " + TypeUtils.getIRRep(retType) + " @" + name + "( ");
        for(int i=0;i<args.size();i++) {
            if (i < args.size() - 1) {
                out.print(TypeUtils.getIRRep(args.get(i)) + ", ");
            } else {
                out.print(TypeUtils.getIRRep(args.get(i)));
            }
        }
        out.println(" )\n");
	}

	// Declarations of C functions which are used by default classes of cool
	void printRequiredCFunctionsDeclaration() {
		// String Methods

		// argument type list for string functions
		ArrayList<TypeUtils.TypeID> StringFunctionArguments = new ArrayList<TypeUtils.TypeID>();
		StringFunctionArguments.add(TypeUtils.TypeID.INT8PTR);
		StringFunctionArguments.add(TypeUtils.TypeID.INT8PTR);
 		// strcpy
		printDeclaration(StringFunctionArguments, TypeUtils.TypeID.INT8PTR, "strcpy"); 

		// strcmp
		printDeclaration(StringFunctionArguments, TypeUtils.TypeID.INT8PTR, "strcmp");
		StringFunctionArguments.add(TypeUtils.TypeID.INT32);

		// strncpy
		printDeclaration(StringFunctionArguments, TypeUtils.TypeID.INT8PTR, "strncpy");

		// strlen
		printDeclaration(new ArrayList<TypeUtils.TypeID>(Arrays.asList(TypeUtils.TypeID.INT8PTR)),
			TypeUtils.TypeID.INT8PTR, "strlen");

		ArrayList<TypeUtils.TypeID> IOFunctionArguments = new ArrayList<TypeUtils.TypeID>();		
		IOFunctionArguments.add(TypeUtils.TypeID.INT8PTR);
		IOFunctionArguments.add(TypeUtils.TypeID.VARARG);
		//printf
		printDeclaration(IOFunctionArguments, TypeUtils.TypeID.INT32, "printf");
		//scanf
		printDeclaration(IOFunctionArguments, TypeUtils.TypeID.INT32, "scanf");

		// malloc
		printDeclaration(new ArrayList<TypeUtils.TypeID>(Arrays.asList(TypeUtils.TypeID.INT32)),
			TypeUtils.TypeID.INT8PTR, "malloc");
		// exit
		printDeclaration(new ArrayList<TypeUtils.TypeID>(Arrays.asList(TypeUtils.TypeID.INT32)),
			TypeUtils.TypeID.VOID, "exit");

	}

    void generateDefUtil(TypeUtils.TypeID retType, String name, List<ArgumentInfo> args) {
        out.print("\ndefine " + TypeUtils.getIRRep(retType) + " @" + name + "( ");
        for(int i=0;i<args.size();i++) {
            if (i < args.size()) {
                out.print(TypeUtils.getIRRep(args.get(i).type) + " " + args.get(i).name + ", ");
            } else {
                out.print(TypeUtils.getIRRep(args.get(i).type) + " " + args.get(i).name);
            }
        }
        out.println(" ) {\nentry:");
    }

    void generateIRForMainClass(AST.program program) {
    	for(AST.class_ cl: program.classes){
    	}
    }
}
