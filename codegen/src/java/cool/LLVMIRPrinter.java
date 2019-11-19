package cool;

import java.io.PrintWriter;
import java.util.*;

public class LLVMIRPrinter {
	PrintWriter out;
	public HashMap<String, Integer> stringToLineNoMapping = new HashMap<String, Integer>();
	public int stringLineNo = 0;
    public static Integer nestedIfCount;
    public static Integer nestedLoopCount;
    public InstructionInfo registerCounter;
    public VisitNodeClass visitNodeObject;
	ArrayList<String> functionFormalNameList;
    public static TypeUtils mthdType;
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
		StringFunctionArguments.add(new TypeUtils(TypeUtils.Typegt.INT8PTR));
		StringFunctionArguments.add(new TypeUtils(TypeUtils.Typegt.INT8PTR));
 		// strcpy
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.Typegt.INT8PTR), "strcpy");

		// strcmp
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.Typegt.INT8PTR), "strcmp");
		StringFunctionArguments.add(new TypeUtils(TypeUtils.Typegt.INT32));

		// strncpy
		printDeclaration(StringFunctionArguments, new TypeUtils(TypeUtils.Typegt.INT8PTR), "strncpy");

		// strlen
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.Typegt.INT8PTR))),
			new TypeUtils(TypeUtils.Typegt.INT8PTR), "strlen");

		ArrayList<TypeUtils> IOFunctionArguments = new ArrayList<TypeUtils>();
		IOFunctionArguments.add(new TypeUtils(TypeUtils.Typegt.INT8PTR));
		IOFunctionArguments.add(new TypeUtils(TypeUtils.Typegt.VARARG));
		//printf
		printDeclaration(IOFunctionArguments, new TypeUtils(TypeUtils.Typegt.INT32), "printf");
		//scanf
		printDeclaration(IOFunctionArguments, new TypeUtils(TypeUtils.Typegt.INT32), "scanf");

		// malloc
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.Typegt.INT32))),
			new TypeUtils(TypeUtils.Typegt.INT8PTR), "malloc");
		// exit
		printDeclaration(new ArrayList<TypeUtils>(Arrays.asList(new TypeUtils(TypeUtils.Typegt.INT32))),
			new TypeUtils(TypeUtils.Typegt.VOID), "exit");

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

    public void allocateMethodAttributes(List<ArgumentInfo> aList) {
        // alloca for 'this' operand of method
        printAllocaInstruction(aList.get(0).type, "this.addr");

        // Calling alloca instruction on all method attributes
        int i = 1;
        while(i < aList.size()) {
            printAllocaInstruction(aList.get(i).type, aList.get(i).name.substring(1) + ".addr");
            i++;
        }

        // Calling store instruction on all method attributes
        i = 1;
        while(i < aList.size()) {
            storeInstUtil(new ArgumentInfo(aList.get(i).name.substring(1), aList.get(i).type), new ArgumentInfo(aList.get(i).name.substring(1) + ".addr", aList.get(i).type.getPtr()));
            i++ ;
        }
    }

    void callInstUtil(List<TypeUtils> argTypes, String funcName, boolean isGlobal, List<ArgumentInfo> args, ArgumentInfo resultOp) {
        out.print("\t");
        if (resultOp.type.gt == TypeUtils.Typegt.VOID) {
            out.print("call " + resultOp.type.name);
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
        out.print(funcName + "( ");
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

    void getElemPtrInstUtil(TypeUtils type, List<ArgumentInfo> operandList, ArgumentInfo result, boolean inbounds) {
        out.print("\t");
        if (result.type.gt == TypeUtils.Typegt.VOID) {
            out.print("getelementptr ");
        } else {
            out.print(result.name + " = ");
            out.print("getelementptr ");
        }
        if (inbounds != true) {}
        else
            out.print("inbounds ");
        out.print(TypeUtils.getIRRep(type) + ", ");
        for(int i = 0; i < operandList.size(); i++) {    
            if (i == operandList.size() - 1) {
                out.print(TypeUtils.getIRRep(operandList.get(i).type) + " " + operandList.get(i).name + "\n");
            } else {
                out.print(TypeUtils.getIRRep(operandList.get(i).type) + " " + operandList.get(i).name + ", ");
            }
        }
    }

    void printCallInstruction(ArrayList<TypeUtils> argTypes, String methodName, boolean isGlobal,
    	ArrayList<ArgumentInfo> args, ArgumentInfo resultOp) {
        out.print("\t");
        if (resultOp.type.gt == TypeUtils.Typegt.VOID) {
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

    public static TypeUtils coolTypeToLLVMType(String coolType, int pointerDepth) {
    	TypeUtils tr;
    	if(coolType == "void")
    		return new TypeUtils(TypeUtils.Typegt.VOID);
    	else if(coolType == "String")
    		tr = new TypeUtils(TypeUtils.Typegt.INT8PTR);
    	else if(coolType == "Int")
    		tr = new TypeUtils(TypeUtils.Typegt.INT32);
    	else if(coolType == "Bool")
    		tr = new TypeUtils(TypeUtils.Typegt.INT1);
    	else
    		tr = new TypeUtils(TypeUtils.Typegt.CLASS, "class_" + coolType, pointerDepth);
    	return tr;
    }
    // print an alloca instruction
    void printAllocaInstruction(TypeUtils retType, String name) {
        out.print("\t%" + name + " = alloca " + TypeUtils.getIRRep(retType) + "\n");
    }

    void loadInstUtil(TypeUtils type, ArgumentInfo op, ArgumentInfo result) {
        out.print("\t" + TypeUtils.getIRRep(result.type) + " = load " + TypeUtils.getIRRep(type) + ", " + TypeUtils.getIRRep(op.type) + " " + op.name + "\n");
    }

    void storeInstUtil(ArgumentInfo op, ArgumentInfo result) {
        out.print("\tstore " + TypeUtils.getIRRep(op.type) + " " + op.name + ", " + TypeUtils.getIRRep(result.type) + " " + result.name + "\n");
    }

    void arithmeticUtil(ArgumentInfo op1, String operation, ArgumentInfo op2, ArgumentInfo result) {
        out.print("\t");
        if (result.type.gt != TypeUtils.Typegt.VOID) {
            out.print(TypeUtils.getIRRep(result.type) + " = ");
        }
        out.print(operation + " " + TypeUtils.getIRRep(op1.type) + " " + op1.name + ", "  + op2.name + "\n");
    }

    void cmpInstUtil(ArgumentInfo op1, String cond, ArgumentInfo op2, ArgumentInfo result) {
        out.print("\t" + result.name + " = icmp ");
        switch(cond) {
            case "LT":
                out.print("slt ");
                break;
            case "EQ":
                out.print("eq ");
                break;
            case "LE":
                out.print("sle ");
                break;
        }
        out.print(TypeUtils.getIRRep(op1.type) + " " + op1.name + ", " + op2.name + "\n");
    }

    void brConditionUtil(ArgumentInfo op, String condTrue, String condFalse) {
        out.print("\tbr " + TypeUtils.getIRRep(op.type) + " " + op.name + ", label %" + condTrue + ", label %" + condFalse + "\n");
    }

    void brUncoditionUtil(String label) {
        out.print("\tbr label %" + label + "\n\n");
    }

    public void generateConstructorOfClass(String clsName, InstructionInfo track, AST.class_ cl, ClassTable classTable) {

        // Name of constructor (mangled)
        String mthdName = clsName + "_Cons_" + clsName;

        // List of OpClass for attributes
        ArrayList<ArgumentInfo> attrOperandList = new ArrayList<ArgumentInfo>();
        attrOperandList.add(new ArgumentInfo("this", coolTypeToLLVMType(clsName, 1)));

        // Define the constructor and establish pointer information
        beginDefinition(coolTypeToLLVMType(clsName, 1), mthdName, attrOperandList);
        printAllocaInstruction(coolTypeToLLVMType(clsName, 1), "this.addr");

        // Performing load and store operations for constructors
        storeInstUtil(new ArgumentInfo("this", coolTypeToLLVMType(clsName, 1)), new ArgumentInfo("this" + ".addr", coolTypeToLLVMType(clsName, 2)));
        loadInstUtil(coolTypeToLLVMType(clsName, 1), new ArgumentInfo("this" + ".addr", coolTypeToLLVMType(clsName, 2)), new ArgumentInfo("this1", coolTypeToLLVMType(clsName, 1)));

        List<AST.attr> attrListTemp = classTable.getJustAttrs(clsName);
        int i = 0;
        while(i < attrListTemp.size()) {
            AST.attr attrTemp = attrListTemp.get(i);
            ArgumentInfo res = new ArgumentInfo(attrTemp.name, new TypeUtils(TypeUtils.Typegt.INT32));
            List<ArgumentInfo> operandList = new ArrayList<ArgumentInfo>();
            operandList.add(new ArgumentInfo("this1", coolTypeToLLVMType(clsName, 1)));

            if (attrTemp.typeid.equals("Bool") == true) {
                // Bool attribute codegen
                operandList.add((ArgumentInfo)new CoolInt(0));
                operandList.add((ArgumentInfo)new CoolInt(i));
                getElemPtrInstUtil(coolTypeToLLVMType(clsName, 0), operandList, res, true);
                TypeUtils ptr = new TypeUtils(TypeUtils.Typegt.INT1PTR);
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, this, attrTemp.value, track, cl, functionFormalNameList);
                    storeInstUtil(new ArgumentInfo(String.valueOf(track.registerVal - 1), track.lastInstructionType), new ArgumentInfo(attrTemp.name, track.lastInstructionType.getPtr()));
                } else {
                    storeInstUtil((ArgumentInfo)new CoolBool(false), new ArgumentInfo(attrTemp.name, ptr));
                }
            } else if (attrTemp.typeid.equals("String") == true) {
                // String attribute codegen
                operandList.add((ArgumentInfo)new CoolInt(0));
                operandList.add((ArgumentInfo)new CoolInt(i));
                getElemPtrInstUtil(coolTypeToLLVMType(clsName, 0), operandList, res, true);
                String lenString = null;
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, this, attrTemp.value, track, cl, functionFormalNameList);
                    storeInstUtil(new ArgumentInfo(String.valueOf(track.registerVal - 1), track.lastInstructionType), new ArgumentInfo(attrTemp.name, track.lastInstructionType.getPtr()));
                } else {
                    lenString = "[" + 1 + " x i8]";
                    out.println("\tstore i8* getelementptr inbounds (" + lenString + ", " + lenString + "* @.str.empty , i32 0, i32 0), i8** %" + attrTemp.name);
                }
            } else if (attrTemp.typeid.equals("Int") == true) {
                // Int attribute codegen
                operandList.add((ArgumentInfo)new CoolInt(0));
                operandList.add((ArgumentInfo)new CoolInt(i));
                getElemPtrInstUtil(coolTypeToLLVMType(clsName, 0), operandList, res, true);
                TypeUtils ptr = new TypeUtils(TypeUtils.Typegt.INT32PTR);
                if (attrTemp.value.getClass() != AST.no_expr.class && attrTemp.value.getClass() != AST.new_.class) {
                    track = visitNodeObject.VisitorPattern(out, this, attrTemp.value, track, cl, functionFormalNameList);
                    storeInstUtil(new ArgumentInfo(String.valueOf(track.registerVal - 1), track.lastInstructionType), new ArgumentInfo(attrTemp.name, track.lastInstructionType.getPtr()));
                } else {
                    storeInstUtil((ArgumentInfo)new CoolInt(0), new ArgumentInfo(attrTemp.name, ptr));
                }
            } else {
                // other cases
                operandList.add((ArgumentInfo)new CoolInt(0));
                operandList.add((ArgumentInfo)new CoolInt(i));
                getElemPtrInstUtil(coolTypeToLLVMType(clsName, 0), operandList, res, true);
                TypeUtils ptr = coolTypeToLLVMType(clsName, 1);
                if ((attrTemp.value.getClass() != AST.no_expr.class)) {
                    track = visitNodeObject.VisitorPattern(out, this, attrTemp.value, track, cl, functionFormalNameList);
                    storeInstUtil(new ArgumentInfo(String.valueOf(track.registerVal - 1), coolTypeToLLVMType(attrTemp.typeid, 1)), new ArgumentInfo(attrTemp.name, coolTypeToLLVMType(attrTemp.typeid, 1).getPtr()));
                } else {
                    out.println("\tstore " + TypeUtils.getIRRep(coolTypeToLLVMType(attrTemp.typeid, 1)) + " null , " + TypeUtils.getIRRep(coolTypeToLLVMType(attrTemp.typeid, 1)) + "* %" + attrTemp.name);
                }
            }
            i++;
        }
        returnInstUtil(new ArgumentInfo("this1", coolTypeToLLVMType(clsName, 1)));
    }

    void returnInstUtil(ArgumentInfo op) {
        out.print("\tret ");
        if (op.type.gt == TypeUtils.Typegt.VOID) {
            out.print("void\n");
        } else {
            out.print(TypeUtils.getIRRep(op.type) + " " + op.name + "\n");
        }
        out.print("}\n");
    }

    void generateIRForMainClass(AST.program program, ClassTable classTable) {
        visitNodeObject = new VisitNodeClass(classTable);
    	AST.class_ mainClass = null;
    	// get the main class
    	for(AST.class_ cl: program.classes){
    		if(cl.name.equals("Main")) {
    			mainClass = cl;
    		}
    	}
    	// System.out.println("" + mainClass.name);
    	// the main function of llvm does what this class does
    	beginDefinition(new TypeUtils(TypeUtils.Typegt.INT32), "main", new ArrayList<ArgumentInfo>());
    	// for every class we also create an struct and constructor
    	printAllocaInstruction(coolTypeToLLVMType("Main", 0), "Main_obj");

		ArrayList<ArgumentInfo> argList = new ArrayList<ArgumentInfo>();
        argList.add(new ArgumentInfo("Main_obj", coolTypeToLLVMType("Main", 0)));
        printCallInstruction(new ArrayList<TypeUtils>(), "Main_Cons_Main",
        	true, argList, new ArgumentInfo("obj1", coolTypeToLLVMType("Main", 1)));
        argList.set(0, new ArgumentInfo("obj1", coolTypeToLLVMType("Main", 1)));
        TypeUtils tempTypeUtilsObject = new TypeUtils(TypeUtils.Typegt.EMPTY);
        for(AST.feature ftre : mainClass.features) {
            if(ftre.getClass() == AST.method.class) {
                AST.method mthdTemp = (AST.method)ftre;
                if(mthdTemp.name.equals("main")) {
                    if(mthdTemp.typeid.equals("Object"))
                        tempTypeUtilsObject = new TypeUtils(TypeUtils.Typegt.VOID);
                    else
                        tempTypeUtilsObject = coolTypeToLLVMType(mthdTemp.typeid, 0);
                    break;
                }
            }
        }
        printCallInstruction(new ArrayList<TypeUtils>(), "Main_main", true, argList, new ArgumentInfo("0", tempTypeUtilsObject));
        returnInstUtil((ArgumentInfo)new CoolInt(0));

        List<TypeUtils> attrTypesList = new ArrayList<TypeUtils>();
        for(AST.attr attrTemp : classTable.getJustAttrs(mainClass.name)) {
            attrTypesList.add(coolTypeToLLVMType(attrTemp.typeid, 1));
            if(attrTemp.typeid.equals("String") && attrTemp.value.getClass() == AST.string_const.class) {
                // Means the current attribute is a string constant
                StringUtil(attrTemp.value, 0);
            }
        }
        // Generates the define code for attributes of class
        printClassType(mainClass.name, attrTypesList, null);

        // Generating code for assignment of type names
        generateConstructorOfClass(mainClass.name, new InstructionInfo(), mainClass, classTable);

        // registerCounter = new InstructionInfo();
        registerCounter = new InstructionInfo();

            // Now, we iterate over the methods of the class and generate llvm ir for that
        for(AST.method mthdTemp : classTable.getJustMethods(mainClass.name)) {
            // For each method,
            // * We make a list of operand for the aList of the method, 
            // where the first argument is a pointer to the class with name 'this'
            // * Generate code for return type
            // * Mangle name of class with name of function
            // * Call the defined function
            StringUtil(mthdTemp.body, 0);

            ArrayList<ArgumentInfo> argsList = new ArrayList<ArgumentInfo>();
            // Adding 'this' operand of function
            argsList.add(new ArgumentInfo("this", coolTypeToLLVMType(mainClass.name, 1)));

            functionFormalNameList = new ArrayList<String>();

            // Adding other operands
            for(AST.formal frmlList : mthdTemp.formals) {
                ArgumentInfo argTemp = new ArgumentInfo(frmlList.name, coolTypeToLLVMType(frmlList.typeid, 1));
                argsList.add(argTemp);
                functionFormalNameList.add(frmlList.name);
            }

            String mthdMangledName = mainClass.name + "_" + mthdTemp.name;
            if(mthdTemp.typeid.equals("Object")) {
                mthdType = new TypeUtils(TypeUtils.Typegt.VOID);
            } else {
                mthdType = coolTypeToLLVMType(mthdTemp.typeid, 0);
            }
            beginDefinition(mthdType, mthdMangledName, argsList);

            // Generating code for retval
            TypeUtils mthdRetType = coolTypeToLLVMType(mthdTemp.typeid, 0);
            if(mthdTemp.typeid.equals("Object") == false) {
                ArgumentInfo retMthdVal = new ArgumentInfo("retval", coolTypeToLLVMType(mthdTemp.typeid, 0));
                printAllocaInstruction(coolTypeToLLVMType(mthdTemp.typeid, 0), retMthdVal.name);
            }

            // Generating the alloca instructions for argsList
            allocateMethodAttributes(argsList);

            ClassPlus currentClass = classTable.getClassPlus(mainClass.name);
            TypeUtils singlePtr = coolTypeToLLVMType(mainClass.name, 2);
            storeInstUtil(new ArgumentInfo("this", coolTypeToLLVMType(mainClass.name, 1)), new ArgumentInfo("this" + ".addr", coolTypeToLLVMType(mainClass.name, 2)));
            loadInstUtil(coolTypeToLLVMType(mainClass.name, 1), new ArgumentInfo("this" + ".addr", coolTypeToLLVMType(mainClass.name, 2)), new ArgumentInfo("this1", coolTypeToLLVMType(mainClass.name, 1)));

            for(int i=0; i<classTable.getJustAttrs(mainClass.name).size(); i++) {
                int flagTemp = 0;
                for(String elem : functionFormalNameList) {
                    if(elem.equals(classTable.getJustAttrs(mainClass.name).get(i).name)) {
                        flagTemp = 1;
                        break;
                    }
                }

                if(flagTemp == 1)
                    continue;

                List<ArgumentInfo> opList = new ArrayList<ArgumentInfo>();
                ArgumentInfo result = new ArgumentInfo(classTable.getJustAttrs(mainClass.name).get(i).name, new TypeUtils(TypeUtils.Typegt.INT32));
                opList.add(new ArgumentInfo("this1", coolTypeToLLVMType(mainClass.name, 1)));
                opList.add(new CoolInt(0));
                opList.add(new CoolInt(i));
                getElemPtrInstUtil(coolTypeToLLVMType(mainClass.name, 0), opList, result, true);
            }

            // Class Name of current class
            String currentClassName = mainClass.name;
            // For every method resetting The value of nested if and loop
            nestedIfCount = 0;
            nestedLoopCount = 0;
            
            /* 
                Reinitializing the register count to zero for each method
                Reinitializing the last instruction's type to method return type for each method
                Entry is the first label of every method
            */
            registerCounter.reintialiseToDefault(0, mthdRetType, "%entry");
            
            registerCounter = visitNodeObject.VisitorPattern(out, this, mthdTemp.body, registerCounter, mainClass, functionFormalNameList);
            
            if(((mthdTemp.body.getClass() != AST.block.class) && (mthdTemp.body.getClass() != AST.loop.class) && (mthdTemp.body.getClass() != AST.cond.class))) {
                if(registerCounter.registerVal - 1 >= 0 && mthdType.name.equals(registerCounter.lastInstructionType.name) && ((mthdType.name.equals("void")) == false)) {
                    storeInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), mthdType), new ArgumentInfo("retval", mthdType.getPtr()));
                }
            }

            brUncoditionUtil("fun_returning_basic_block");
            // Label for dispatch on void fucntion
            out.println("dispatch_on_void_basic_block:");
            printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), "err_msg_void_dispatch");
            out.println("\tstore i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch");
            loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR), new ArgumentInfo("err_msg_void_dispatch", new TypeUtils(TypeUtils.Typegt.INT8DOUBLEPTR)), new ArgumentInfo("print_err_msg_void_dispatch", new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            List<ArgumentInfo> printArguments;
            printArguments = new ArrayList<ArgumentInfo>();
            printArguments.add(new ArgumentInfo("print_err_msg_void_dispatch", new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            // Invoking out_string of IO to display the error message
            callInstUtil(new ArrayList<TypeUtils>(), "IO_out_string", true, printArguments, new ArgumentInfo("null", new TypeUtils(TypeUtils.Typegt.VOID)));
            // Aborting after printing the error message
            callInstUtil(new ArrayList<TypeUtils>(), "Object_abort", true, new ArrayList<ArgumentInfo>(), new ArgumentInfo("null", new TypeUtils(TypeUtils.Typegt.VOID)));
            brUncoditionUtil("fun_returning_basic_block");

            // Creating Print and abort labels
            out.print("func_div_by_zero_abort:\n");
            printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), "err_msg");
            out.print("\tstore i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg\n");
            loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR), new ArgumentInfo("err_msg", new TypeUtils(TypeUtils.Typegt.INT8DOUBLEPTR)), new ArgumentInfo("print_err_msg", new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            printArguments = new ArrayList<>();
            printArguments.add(new ArgumentInfo("print_err_msg", new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            // Invoking out_string of IO to display the error message
            callInstUtil(new ArrayList<TypeUtils>(), "IO_out_string", true, printArguments, new ArgumentInfo("null", new TypeUtils(TypeUtils.Typegt.VOID)));
            // Aborting after printing the error message
            callInstUtil(new ArrayList<TypeUtils>(), "Object_abort", true, new ArrayList<ArgumentInfo>(), new ArgumentInfo("null", new TypeUtils(TypeUtils.Typegt.VOID)));
            brUncoditionUtil("fun_returning_basic_block");

            out.print("fun_returning_basic_block:\n");
            
            // Printing the return type of the method
            if(!mthdTemp.typeid.equals("Object")) {
                loadInstUtil(mthdRetType, new ArgumentInfo("retval", mthdRetType.getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal), mthdRetType));
                returnInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal), mthdRetType));
                registerCounter.registerVal = registerCounter.registerVal + 1;
                
            } else {
                returnInstUtil(new ArgumentInfo("null", new TypeUtils(TypeUtils.Typegt.VOID)));
            }
        }

    }
}
