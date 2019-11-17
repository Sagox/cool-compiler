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

	void printDeclaration(ArrayList<TypeUtils> args, TypeUtils retType, String name) {
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
		ArrayList<TypeUtils> StringFunctionArguments = new ArrayList<TypeUtils>();
		StringFunctionArguments.add(new TypeUtils(TypeUtils.TypeID.INT8PTR));
		StringFunctionArguments.add(new TypeUtils(TypeUtils.TypeID.INT8PTR));
 		// strcpy
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.TypeID.INT8PTR), "strcpy"); 

		// strcmp
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.TypeID.INT8PTR), "strcmp");
		StringFunctionArguments.add(new TypeUtils(TypeUtils.TypeID.INT32));

		// strncpy
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.TypeID.INT8PTR), "strncpy");

		// strlen
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.TypeID.INT8PTR))),
			new TypeUtils(TypeUtils.TypeID.INT8PTR), "strlen");

		ArrayList<TypeUtils> IOFunctionArguments = new ArrayList<TypeUtils>();		
		IOFunctionArguments.add(new TypeUtils(TypeUtils.TypeID.INT8PTR));
		IOFunctionArguments.add(new TypeUtils(TypeUtils.TypeID.VARARG));
		//printf
		printDeclaration(IOFunctionArguments, new TypeUtils(TypeUtils.TypeID.INT32), "printf");
		//scanf
		printDeclaration(IOFunctionArguments, new TypeUtils(TypeUtils.TypeID.INT32), "scanf");

		// malloc
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.TypeID.INT32))),
			new TypeUtils(TypeUtils.TypeID.INT8PTR), "malloc");
		// exit
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.TypeID.INT32))),
			new TypeUtils(TypeUtils.TypeID.VOID), "exit");

	}

    void beginDefinition(TypeUtils retType, String name, ArrayList<ArgumentInfo> args) {
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

    TypeUtils coolTypeToLLVMType(String coolType, int pointerDepth) {
    	TypeUtils tr;
    	if(coolType == "void")
    		return new TypeUtils(TypeUtils.TypeID.VOID);
    	else if(coolType == "String")
    		tr = new TypeUtils(TypeUtils.TypeID.INT8PTR);
    	else if(coolType == "Int")
    		tr = new TypeUtils(TypeUtils.TypeID.INT32);
    	else if(coolType == "Bool")
    		tr = new TypeUtils(TypeUtils.TypeID.INT1);
    	else
    		tr = new TypeUtils(TypeUtils.TypeID.CLASS, "class_" + coolType, pointerDepth);
    	return tr;
    }
    // print an alloca instruction
    void printAllocaInstruction(TypeUtils retType, String name) {
        out.print("\t%" + name + " = alloca " + TypeUtils.getIRRep(retType) + "\n");
    }

    void generateIRForMainClass(AST.program program) {
    	AST.class_ mainClass;
    	// get the main class
    	for(AST.class_ cl: program.classes){
    		if(cl.name == "Main") {
    			mainClass = cl;
    		}
    	}
    	// the main function of llvm does what this class does
    	beginDefinition(new TypeUtils(TypeUtils.TypeID.INT32), "main", new ArrayList<ArgumentInfo>());
    	// for every class we also create an struct and constructor
    	printAllocaInstruction(coolTypeToLLVMType("Main", 0), "Main_obj");

		ArrayList<ArgumentInfo> argList = new ArrayList<ArgumentInfo>();
        argList.add(new ArgumentInfo("Main_obj", coolTypeToLLVMType("Main", 0)));

    }
}
