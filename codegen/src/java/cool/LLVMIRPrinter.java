package cool;

import java.io.PrintWriter;
import java.util.*;

public class LLVMIRPrinter {
	PrintWriter out;
	public HashMap<String, Integer> stringToLineNoMapping = new HashMap<String, Integer>();
	public int stringLineNo = 0;
	ArrayList<String> functionFormalNameList;
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

    void printClassType(String className, List<TypeUtils> attributes, String nameVar) {
        out.print("%class." + className + " = type { ");
        int i = 0;
        while(i < attributes.size()) {
            if (i == attributes.size() - 1) {
                out.print(TypeUtils.getIRRep(attributes.get(i)));
            } else {
                out.print(TypeUtils.getIRRep(attributes.get(i)) + ", ");
            }
            i++;
        }
        out.print(" }\n");
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

    void stringEscUtil(String str, String nameVar) {
        int i = 0;
        while(i < str.length()) {
            char chr = str.charAt(i);
            switch(chr) {
                case '\\':
                    out.print("\\5C");
                    break;
                case '\"':
                    out.print("\\22");
                    break;
                case '\n':
                    out.print("\\0A");
                    break;
                case '\t':
                    out.print("\\09");
                    break;
                default:
                    out.print(chr);
            }
            i++;
        }
    }

    public void StringUtil(AST.expression expr, Integer strLine) {
        if (expr.getClass() == AST.string_const.class) {
            // If expr is of string_const class, then generate
            AST.string_const exprStr = (AST.string_const)expr;
            String tempString = exprStr.value;
            stringToLineNoMapping.put(tempString, stringLineNo);
            stringLineNo++;
            out.print("@.str." + stringToLineNoMapping.get(tempString) + " = private unnamed_addr constant [" + String.valueOf(tempString.length() + 1) + " x i8] c\"");
            stringEscUtil(tempString, null);
            out.println("\\00\"");
        } else if (expr.getClass() == AST.eq.class) {
            // Traverse on both sides of equality
            StringUtil(((AST.eq)expr).e1, 0);
            StringUtil(((AST.eq)expr).e2, 0);
        } else if (expr.getClass() == AST.assign.class) {
            // Traverse on assign expression
            StringUtil(((AST.assign)expr).e1, 0);
        } else if (expr.getClass() == AST.block.class) {
            for (AST.expression e : ((AST.block)expr).l1) {
                StringUtil(e, 0);
            }
        } else if (expr.getClass() == AST.loop.class) {
            // Traverse on loop.body and loop.predicate expression
            StringUtil(((AST.loop)expr).predicate, 0);
            StringUtil(((AST.loop)expr).body, 0);
        } else if (expr.getClass() == AST.cond.class) {
            // Traverse on cond.ifbody, cond.elsebody and cond.predicate expression
            StringUtil(((AST.cond)expr).predicate, 0);
            StringUtil(((AST.cond)expr).ifbody, 0);
            StringUtil(((AST.cond)expr).elsebody, 0);
        } else if (expr.getClass() == AST.static_dispatch.class) {
            // Traverse on static_dispatch.expr
            StringUtil(((AST.static_dispatch)expr).caller, 0);
            for (AST.expression e : ((AST.static_dispatch)expr).actuals) {
                StringUtil(e, 0);
            }
        }
    }

    void printCallInstruction(ArrayList<TypeUtils> argTypes, String methodName, boolean isGlobal,
    	ArrayList<ArgumentInfo> args, ArgumentInfo resultOp) {
        out.print("\t");
        if (resultOp.type.gt == TypeUtils.TypeID.VOID) {
            out.print("call " + TypeUtils.getIRRep(resultOp.type));
        } else
        out.print(resultOp.name + " = call " + TypeUtils.getIRRep(resultOp.type));
        int sz = argTypes.size();
        if ( sz > 0) {
            out.print(" (");
            int i = 0;
            while(i < sz) {
                if (i == sz - 1) {
                    out.print(TypeUtils.getIRRep(argTypes.get(i)) + ") ");
                } else {
                    out.print(TypeUtils.getIRRep(argTypes.get(i)) + ", ");
                }
                i++;
            }
        }
        if (isGlobal != true) {
            out.print(" %");
        } else {
            out.print(" @");
        }
        out.print(methodName + "( ");
        int i = 0;
        while(i < args.size()) {
            if (i == args.size() - 1) {
                out.print(TypeUtils.getIRRep(args.get(i).type) + " " + args.get(i).name);
            } else {
                out.print(TypeUtils.getIRRep(args.get(i).type) + " " + args.get(i).name + ", ");
            }
            i++;
        }
        out.print(" )\n");
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

    void loadInstUtil(TypeUtils type, ArgumentInfo op, ArgumentInfo result, String nameVar) {
        out.print("\t" + TypeUtils.getIRRep(result.type) + " = load " + TypeUtils.getIRRep(type) + ", " + TypeUtils.getIRRep(op.type) + " " + op.name + "\n");
    }

    void storeInstUtil(ArgumentInfo op, ArgumentInfo result, String nameVar) {
        out.print("\tstore " + TypeUtils.getIRRep(op.type) + " " + op.name + ", " + TypeUtils.getIRRep(result.type) + " " + result.name + "\n");
    }

    // public void generateConstructorOfClass(String clsName, InstructionInfo track, AST.class_ cl, ClassTable classTable) {

    //     // Name of constructor (mangled)
    //     String mthdName = clsName + "_Cons_" + clsName;

    //     // List of OpClass for attributes
    //     List<ArgumentInfo> attrOperandList = new ArrayList<ArgumentInfo>();
    //     attrOperandList.add(new ArgumentInfo(TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1), "this"));

    //     // Define the constructor and establish pointer information
    //     beginDefinition(TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1), mthdName, attrOperandList);
    //     printAllocaInstruction(TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1), new ArgumentInfo(TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1), "this.addr"));

    //     // Performing load and store operations for constructors
    //     storeInstUtil(out, new OpClass(operandType(clsName, true, 1), "this"), new OpClass(operandType(clsName, true, 2), "this" + ".addr"), null);
    //     loadInstUtil(out, operandType(clsName, true, 1), new OpClass(operandType(clsName, true, 2), "this" + ".addr"), new OpClass(operandType(clsName, true, 1), "this1"), null);

    //     List<AST.attr> attrListTemp = classTable.getJustAttrs(clsName);
    //     int i = 0;
    //     while(i < attrListTemp.size()) {
    //         AST.attr attrTemp = attrListTemp.get(i);
    //         ArgumentInfo res = new ArgumentInfo(attrTemp.name, new TypeUtils(TypeUtils.TypeID.INT32));
    //         List<ArgumentInfo> operandList = new ArrayList<ArgumentInfo>();
    //         operandList.add(new ArgumentInfo(TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1), "this1"));

    //         if (attrTemp.typeid.equals("Bool") == true) {
    //             // Bool attribute codegen
    //             operandList.add((ArgumentInfo)new CoolInt(0));
    //             operandList.add((ArgumentInfo)new CoolInt(i));
    //             printUtil.getElemPtrInstUtil(out, operandType(clsName, true, 0), operandList, res, true, null);
    //             TypeUtils ptr = new TypeUtils(TypeUtils.TypeID.INT1PTR);
    //             if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
    //                 track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, classTable, mainClass, functionFormalNameList);
    //                 printUtil.storeInstUtil(out, new OpClass(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new ArgumentInfo(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
    //             } else {
    //                 storeInstUtil((ArgumentInfo)new CoolBool(false), new ArgumentInfo(ptr, attrTemp.name), null);
    //             }
    //         } else if (attrTemp.typeid.equals("String") == true) {
    //             // String attribute codegen
    //             operandList.add((ArgumentInfo)new CoolInt(0));
    //             operandList.add((ArgumentInfo)new CoolInt(i));
    //             getElemPtrInstUtil(TypeUtils(clsName, true, 0), operandList, res, true, null);
    //             String lenString = null;
    //             if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
    //                 track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, classTable, mainClass, functionFormalNameList);
    //                 storeInstUtil(new ArgumentInfo(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new ArgumentInfo(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
    //             } else {
    //                 lenString = "[" + 1 + " x i8]";
    //                 out.println("\tstore i8* getelementptr inbounds (" + lenString + ", " + lenString + "* @.str.empty , i32 0, i32 0), i8** %" + attrTemp.name);
    //             }
    //         } else if (attrTemp.typeid.equals("Int") == true) {
    //             // Int attribute codegen
    //             operandList.add((ArgumentInfo)new CoolInt(0));
    //             operandList.add((ArgumentInfo)new CoolInt(i));
    //             getElemPtrInstUtil(TypeUtils(clsName, true, 0), operandList, res, true, null);
    //             TypeUtils ptr = new TypeUtils(TypeUtils.TypeID.INT32PTR);
    //             if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
    //                 track = visitNodeObject.VisitorPattern(out, printUtil, attrTemp.value, track, classTable, mainClass, functionFormalNameList);
    //                 storeInstUtil(out, new ArgumentInfo(track.lastInstructionType, String.valueOf(track.registerVal - 1)), new ArgumentInfo(track.lastInstructionType.correspondingPtrType(), attrTemp.name), null);
    //             } else {
    //                 storeInstUtil(out, (ArgumentInfo)new CoolInt(0), new ArgumentInfo(ptr, attrTemp.name), null);
    //             }
    //         } else {
    //             // other cases
    //             operandList.add((ArgumentInfo)new CoolInt(0));
    //             operandList.add((ArgumentInfo)new CoolInt(i));
    //             printUtil.getElemPtrInstUtil(operandType(clsName, true, 0), operandList, res, true, null);
    //             TypeUtils ptr = TypeUtils(TypeUtils.TypeID.CLASS, clsName, 1);
    //             if ((attrTemp.value.getClass() != AST.no_expr.class)) {
    //                 track = visitNodeObject.VisitorPattern(printUtil, attrTemp.value, track, classTable, mainClass, functionFormalNameList);
    //                 storeInstUtil(new ArgumentInfo(TypeUtils(TypeUtils.TypeID.CLASS, attrTemp.typeid, 1), String.valueOf(track.registerVal - 1)), new ArgumentInfo(TypeUtils(TypeUtils.TypeID.CLASS, attrTemp.typeid, 1).correspondingPtrType(), attrTemp.name), null);
    //             } else {
    //                 out.println("\tstore " + TypeUtils(TypeUtils.TypeID.CLASS, attrTemp.typeid, 1).name + " null , " + TypeUtils(attrTemp.typeid, true, 1).name + "* %" + attrTemp.name);
    //             }
    //         }
    //         i++;
    //     }
    //     returnInstUtil(out, new OpClass(operandType(clsName, true, 1), "this1"), null);
    // }

    void returnInstUtil(ArgumentInfo op, String nameVar) {
        out.print("\tret ");
        if (op.type.gt == TypeUtils.TypeID.VOID) {
            out.print("void\n");
        } else {
            out.print(TypeUtils.getIRRep(op.type) + " " + op.name + "\n");
        }
        out.print("}\n");
    }

    void generateIRForMainClass(AST.program program, ClassTable classTable) {
    	AST.class_ mainClass = null;
    	// get the main class
    	for(AST.class_ cl: program.classes){
    		if(cl.name.equals("Main")) {
    			mainClass = cl;
    		}
    	}
    	// System.out.println("" + mainClass.name);
    	// the main function of llvm does what this class does
    	beginDefinition(new TypeUtils(TypeUtils.TypeID.INT32), "main", new ArrayList<ArgumentInfo>());
    	// for every class we also create an struct and constructor
    	printAllocaInstruction(coolTypeToLLVMType("Main", 0), "Main_obj");

		ArrayList<ArgumentInfo> argList = new ArrayList<ArgumentInfo>();
        argList.add(new ArgumentInfo("Main_obj", coolTypeToLLVMType("Main", 0)));
        printCallInstruction(new ArrayList<TypeUtils>(), "Main_Cons_Main",
        	true, argList, new ArgumentInfo("obj1", new TypeUtils(TypeUtils.TypeID.CLASS, "Main", 1)));
        argList.set(0, new ArgumentInfo("obj1", new TypeUtils(TypeUtils.TypeID.CLASS, "Main", 1)));
        TypeUtils tempTypeUtilsObject = new TypeUtils(TypeUtils.TypeID.EMPTY);
        for(AST.feature ftre : mainClass.features) {
            if(ftre.getClass() == AST.method.class) {
                AST.method mthdTemp = (AST.method)ftre;
                if(mthdTemp.name.equals("main")) {
                    if(mthdTemp.typeid.equals("Object"))
                        tempTypeUtilsObject = new TypeUtils(TypeUtils.TypeID.VOID);
                    else
                        tempTypeUtilsObject = new TypeUtils(TypeUtils.TypeID.CLASS, mthdTemp.typeid, 0);
                    break;
                }
            }
        }
        printCallInstruction(new ArrayList<TypeUtils>(), "Main_main", true, argList, new ArgumentInfo("0", tempTypeUtilsObject));
        returnInstUtil((ArgumentInfo)new CoolInt(0), "");

        List<TypeUtils> attrTypesList = new ArrayList<TypeUtils>();
        for(AST.attr attrTemp : classTable.getJustAttrs(mainClass.name)) {
            attrTypesList.add(new TypeUtils(TypeUtils.TypeID.CLASS, attrTemp.typeid, 1));
            if(attrTemp.typeid.equals("String") && attrTemp.value.getClass() == AST.string_const.class) {
                // Means the current attribute is a string constant
                StringUtil(attrTemp.value, 0);
            }
        }
        // Generates the define code for attributes of class
        printClassType(mainClass.name, attrTypesList, null);

        // Generating code for assignment of type names
        // generateConstructorOfClass(out, printUtil, cl.name, new InstructionInfo(), cl, classTable, functionFormalNameList);

        // registerCounter = new InstructionInfo();


    }
}
