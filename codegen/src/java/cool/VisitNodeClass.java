package cool;

import java.util.*;
import java.io.PrintWriter;

public class VisitNodeClass {
    public ClassTable ct;

    VisitNodeClass(ClassTable classTable) {
        ct = classTable;
    }

    public String attributeAddressOfObj(String objName, String clName, List<String> functionFormalNameList) {
        for(int i = 0; i < functionFormalNameList.size(); i++) {
            if(functionFormalNameList.get(i).equals(objName)) {
                return (objName + ".addr");
            }
        }
        HashMap<String, AST.attr> attributes = ct.classes.get(clName).alist;
        for (String attrName : attributes.keySet()) {
            if (objName.equals(attributes.get(attrName))) {
                return objName;
            }
        }
        return objName + ".addr";
    }

    // This method returns an object of TypeUtils class for the return type of method
    public TypeUtils returnTypeOfMethod(String clName, String mthdName) {
        HashMap<String, AST.method> methods = ct.classes.get(clName).mlist;
        for(String mthdKey : methods.keySet()) {
            if(mthdName.equals(methods.get(mthdKey).name)) {
                if(methods.get(mthdKey).typeid.equals("Object")) {
                    return (new TypeUtils(TypeUtils.Typegt.VOID));
                }
                return LLVMIRPrinter.coolTypeToLLVMType(methods.get(mthdKey).typeid, 1);
            }
        }
        return (new TypeUtils(TypeUtils.Typegt.VOID));
    }

    public InstructionInfo VisitorPattern(PrintWriter out, LLVMIRPrinter printUtil, AST.expression expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        if(expr.getClass() == AST.bool_const.class) {
            return VisitNode(out, printUtil, (AST.bool_const)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.string_const.class) {
            return VisitNode(out, printUtil, (AST.string_const)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.int_const.class) {
            return VisitNode(out, printUtil, (AST.int_const)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.object.class) {
            return VisitNode(out, printUtil, (AST.object)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.comp.class) {
            return VisitNode(out, printUtil, (AST.comp)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.eq.class) {
            return VisitNode(out, printUtil, (AST.eq)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.leq.class) {
            return VisitNode(out, printUtil, (AST.leq)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.lt.class) {
            return VisitNode(out, printUtil, (AST.lt)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.neg.class) {
            return VisitNode(out, printUtil, (AST.neg)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.divide.class) {
            return VisitNode(out, printUtil, (AST.divide)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.mul.class) {
            return VisitNode(out, printUtil, (AST.mul)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.sub.class) {
            return VisitNode(out, printUtil, (AST.sub)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.plus.class) {
            return VisitNode(out, printUtil, (AST.plus)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.new_.class) {
            return VisitNode(out, printUtil, (AST.new_)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.assign.class) {
            return VisitNode(out, printUtil, (AST.assign)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.block.class) {
            return VisitNode(out, printUtil, (AST.block)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.loop.class) {
            return VisitNode(out, printUtil, (AST.loop)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.cond.class) {
            return VisitNode(out, printUtil, (AST.cond)expr, registerCounter, clsName, functionFormalNameList);
        } else if(expr.getClass() == AST.static_dispatch.class) {
            return VisitNode(out, printUtil, (AST.static_dispatch)expr, registerCounter, clsName, functionFormalNameList);
        }
        return registerCounter;
    }

    // bool_const
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.bool_const expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating the IR for Bool Constants
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT1), String.valueOf(registerCounter.registerVal));
        printUtil.storeInstUtil((ArgumentInfo)new CoolBool((expr).value), new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1PTR)));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT1),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1PTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT1)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT1), registerCounter.lastBasicBlockName);
    }

    // exprString
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.string_const exprString, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        String stringTemp = exprString.value;
        // Generating the IR for string constants
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), String.valueOf(registerCounter.registerVal));
        out.print("\tstore i8* getelementptr inbounds ([" + String.valueOf(stringTemp.length() + 1) + " x i8], [" + String.valueOf(stringTemp.length() + 1) + " x i8]* @.str." + LLVMIRPrinter.stringToLineNoMapping.get(stringTemp) + ", i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT8DOUBLEPTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT8PTR)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT8PTR), registerCounter.lastBasicBlockName);
    }

    // Generating for Assign expression
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.assign node, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        registerCounter = VisitorPattern(out, printUtil, node.e1, registerCounter, clsName, functionFormalNameList);
        String newAddr = attributeAddressOfObj(node.name, clsName, functionFormalNameList);
        printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType), new ArgumentInfo(newAddr, registerCounter.lastInstructionType.getPtr()));
        return registerCounter;
    }

    // Generating for Static dispatch ask ask
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.static_dispatch exprStd, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // which is of type <expr>@<type>.id(<expr>,...,<expr>)
        String exprStdType = exprStd.typeid;
        String exprStdCallee;
        ArrayList<ArgumentInfo> argumentList = new ArrayList<ArgumentInfo>();
        String mangledFunctionName = exprStd.typeid + "_" + exprStd.name;

        if((exprStdType.equals("Int") || exprStdType.equals("Bool") || exprStdType.equals("String")) == false) {
            registerCounter = VisitorPattern(out, printUtil, exprStd.caller, registerCounter, clsName, functionFormalNameList);
            exprStdCallee = registerCounter.lastInstructionType.name;

            out.print("\t%" + registerCounter.registerVal + " = icmp eq " + exprStdCallee + " null, %" + (registerCounter.registerVal - 1) + "\n");
            printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)), "dispatch_on_void_basic_block", "proceed_" + String.valueOf(registerCounter.registerVal));

            out.print("\nproceed_" + String.valueOf(registerCounter.registerVal) + ":\n");
            registerCounter.registerVal = registerCounter.registerVal + 1;
            registerCounter.lastBasicBlockName = "proceed_" + String.valueOf(registerCounter.registerVal) + ":";
        }

        // Adding method arguments to 'argumentList'
        for(AST.expression exp : exprStd.actuals) {
            registerCounter = VisitorPattern(out, printUtil, exp, registerCounter, clsName, functionFormalNameList);
            ArgumentInfo opTemp = new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType);
            argumentList.add(opTemp);
        }

        // Calling VisitorPattern on caller expression
        registerCounter = VisitorPattern(out, printUtil, exprStd.caller, registerCounter, clsName, functionFormalNameList);

        // Adding this pointer to classes other than IO
        if(exprStd.typeid.equals("IO") == false)
            // Adding the 'this' pointer argument to the beggining of the list
            argumentList.add(0, new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType));

        printUtil.printCallInstruction(new ArrayList<TypeUtils>(), mangledFunctionName, true, argumentList, new ArgumentInfo(String.valueOf(registerCounter.registerVal), returnTypeOfMethod(exprStd.typeid, exprStd.name)));

        if(returnTypeOfMethod(exprStd.typeid, exprStd.name).gt.equals(TypeUtils.Typegt.VOID))
            return new InstructionInfo(registerCounter.registerVal, returnTypeOfMethod(exprStd.typeid, exprStd.name), registerCounter.lastBasicBlockName);
        else
            return new InstructionInfo(registerCounter.registerVal + 1, returnTypeOfMethod(exprStd.typeid, exprStd.name), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.cond exprCond, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating for IF-ELSE statements
        // taking care of nested IF-ELSE
        int counter = LLVMIRPrinter.nestedIfCount;
        LLVMIRPrinter.nestedIfCount = LLVMIRPrinter.nestedIfCount + 1;

        // Predicate Block of IF statement
        InstructionInfo preStmt = VisitorPattern(out, printUtil, exprCond.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, registerCounter.lastBasicBlockName), clsName, functionFormalNameList);
        printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(preStmt.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "if.then" + String.valueOf(counter), "if.else" + String.valueOf(counter));

        // IF Body of IF statement
        out.println("\nif.then" + String.valueOf(counter) + ":");
        InstructionInfo thenStmt = VisitorPattern(out, printUtil, exprCond.ifbody, new InstructionInfo(preStmt.registerVal, preStmt.lastInstructionType, "%if.then" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brUncoditionUtil("if.end" + String.valueOf(counter));

        // ELSE Body of IF statement
        out.println("\nif.else" + String.valueOf(counter) + ":");
        InstructionInfo elseStmt = VisitorPattern(out, printUtil, exprCond.elsebody, new InstructionInfo(thenStmt.registerVal, thenStmt.lastInstructionType, "%if.else" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brUncoditionUtil("if.end" + String.valueOf(counter));

        // if-else exit code for IR
        out.println("\nif.end" + String.valueOf(counter) + ":");

        TypeUtils conditionType = elseStmt.lastInstructionType;
        if(conditionType.name.equals("void") != false) { // what da
            return new InstructionInfo(elseStmt.registerVal , conditionType, "%if.end" + String.valueOf(counter));
        }

        out.println("\t%" + elseStmt.registerVal + " = phi " + conditionType.name + " [ %" + (thenStmt.registerVal - 1) + ", " + (thenStmt.lastBasicBlockName) + " ]" + ", " + " [ %" + (elseStmt.registerVal - 1) + ", " + (elseStmt.lastBasicBlockName) + " ]");

        if(elseStmt.registerVal >= 0 && LLVMIRPrinter.mthdType.name.equals(elseStmt.lastInstructionType.name) && ((LLVMIRPrinter.mthdType.name.equals("void")) == false)) {
            printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(elseStmt.registerVal), LLVMIRPrinter.mthdType), new cool.ArgumentInfo("retval", LLVMIRPrinter.mthdType.getPtr()));
        }
        return new InstructionInfo(elseStmt.registerVal + 1, conditionType, "%if.end" + String.valueOf(counter));
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.loop exprLoop, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        int counter = LLVMIRPrinter.nestedLoopCount;
        LLVMIRPrinter.nestedLoopCount = LLVMIRPrinter.nestedLoopCount++;
        printUtil.brUncoditionUtil("for.cond" + String.valueOf(counter));

        // Loop Condition start
        out.println("\nfor.cond" + String.valueOf(counter) + ":");
        // Predicate of Loop
        InstructionInfo preLoop;
        preLoop = VisitorPattern(out, printUtil, exprLoop.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, "%for.cond" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(preLoop.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "for.body" + String.valueOf(counter), "for.end" + String.valueOf(counter));

        // Loop Body Start
        out.println("\nfor.body" + String.valueOf(counter) + ":");
        InstructionInfo bodyLoop;
        bodyLoop = VisitorPattern(out, printUtil, exprLoop.body, new InstructionInfo(preLoop.registerVal, new TypeUtils(TypeUtils.Typegt.INT1), "%for.body" + String.valueOf(counter)), clsName, functionFormalNameList);
        TypeUtils loopTypeVar;
        loopTypeVar = bodyLoop.lastInstructionType;
        printUtil.brUncoditionUtil("for.cond" + String.valueOf(counter));

        out.println("\nfor.end" + String.valueOf(counter) + ":");

        // ask ask
        InstructionInfo tempInstInfo = new InstructionInfo(bodyLoop.registerVal, loopTypeVar, "%for.end" + String.valueOf(counter));
        return tempInstInfo;
    }

    // ask ask
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.block exprBlock, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating for Block expressions
        for(AST.expression exprTemp : exprBlock.l1) {
            registerCounter = VisitorPattern(out, printUtil, exprTemp, registerCounter, clsName, functionFormalNameList);
        }
        if(registerCounter.registerVal - 1 >= 0 && LLVMIRPrinter.mthdType.name.equals(registerCounter.lastInstructionType.name) && ((LLVMIRPrinter.mthdType.name.equals("void")) == false)) {
            printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), LLVMIRPrinter.mthdType), new cool.ArgumentInfo("retval", LLVMIRPrinter.mthdType.getPtr()));
        }
        return registerCounter;
    }

    // ask ask
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.new_ exprNew, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating code for new statements
        // which is of the form, expr ::= new ID

        /*
            Gnerating for predefined classes
        */
        if(exprNew.typeid.equals("String")) {
            // The length of a new string is 1
            String stringLengthIr = "[" + 1 + " x i8]";
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), String.valueOf(registerCounter.registerVal));
            out.print("\tstore i8* getelementptr inbounds (" + stringLengthIr + ", " + stringLengthIr + "* @.str.empty, i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal) + "\n");
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT8PTR)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), (new TypeUtils(TypeUtils.Typegt.INT8PTR))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT8PTR), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("Bool")) {
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT1), String.valueOf(registerCounter.registerVal));
            printUtil.storeInstUtil((ArgumentInfo)new CoolBool(false), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT1)).getPtr()));
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT1), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT1)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), (new TypeUtils(TypeUtils.Typegt.INT1))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT1), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("Int")) {
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT32), String.valueOf(registerCounter.registerVal));
            printUtil.storeInstUtil((ArgumentInfo)new CoolInt(0), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT32)).getPtr()));
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT32), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT32)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1),(new TypeUtils(TypeUtils.Typegt.INT32))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT32), registerCounter.lastBasicBlockName);
        }

        // Generating for other classes
        printUtil.printAllocaInstruction(LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 0), String.valueOf(registerCounter.registerVal));
        ArrayList<ArgumentInfo> operandList;
        operandList = new ArrayList<ArgumentInfo>();

        operandList.add(new ArgumentInfo(String.valueOf(registerCounter.registerVal), LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1)));
        printUtil.printCallInstruction(new ArrayList<TypeUtils>(), exprNew.typeid + "_Cons_" + exprNew.typeid, true, operandList, new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1)));
        return new InstructionInfo(registerCounter.registerVal + 2, LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1), registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.plus expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating code for addition Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "add", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.sub expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating Code for subtraction Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "sub", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.mul expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating code for multiplication operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "mul", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.divide expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating Code for Division Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        // In Division we have to check whether the Dividend is a number different than  Zero or not
        // String op2Str = String.valueOf(op2.registerVal - 1);
        op2.registerVal--;
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)), "EQ", (ArgumentInfo)new CoolInt(0), new ArgumentInfo("comp_" + String.valueOf(op2.registerVal) + "_0", new TypeUtils(TypeUtils.Typegt.INT1)));
        printUtil.brConditionUtil(new ArgumentInfo("comp_" + String.valueOf(op2.registerVal)  + "_0", new TypeUtils(TypeUtils.Typegt.INT1)), "func_div_by_zero_abort", "proceed_" + String.valueOf(op2.registerVal)  + "_0");
        // New branch to proceed to
        out.println("\nproceed_" + String.valueOf(op2.registerVal)  + "_0:");
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "udiv", new ArgumentInfo(String.valueOf(op2.registerVal),new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT32)));

        // returning with proceed as the last Basic Block
        //reset
        op2.registerVal++;
        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), "proceed_" + String.valueOf(op2.registerVal - 1) + "_0:");
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.comp expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        // Taking XOR to perform compliment
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "xor", (ArgumentInfo)new CoolBool(true), new ArgumentInfo(String.valueOf(op1.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)));
        return new InstructionInfo(op1.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op1.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.lt expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating for Less-Than Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "LT", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.leq expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating for Less-Than-EqualTo Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "LE", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.eq expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating for EqualTo expression
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        // ask ask
        if(op1.lastInstructionType.name.equals((new TypeUtils(TypeUtils.Typegt.INT32)).name)) { // can change the argument
            // If Operands are of type Int
            printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "EQ", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));
        } else if(op1.lastInstructionType.name.equals((new TypeUtils(TypeUtils.Typegt.INT1)).name)) {
            // If Operands are of type Bool
            printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "EQ", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)));
        } else if(op1.lastInstructionType.name.equals((new TypeUtils(TypeUtils.Typegt.INT8PTR)).name)) {
            // If operands are fo type String
            ArgumentInfo retVal = new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT1));
            ArrayList<ArgumentInfo> args = new ArrayList<ArgumentInfo>();

            String regName1 = String.valueOf(op1.registerVal - 1);
            String regName2 = String.valueOf(op2.registerVal - 1);
            args.add(new ArgumentInfo(regName1, new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            args.add(new ArgumentInfo(regName2, new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            // Invoking strcmp method of C to compare strings
            printUtil.printCallInstruction(new ArrayList<TypeUtils>(), "String_strcmp", true, args, retVal);
        }

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.neg expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        // Multiplying by -1 for negation
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "mul", (ArgumentInfo)new CoolInt(-1), new ArgumentInfo(String.valueOf(op1.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op1.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op1.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.object exprObj, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating IR for object ID
        TypeUtils operandTemp = LLVMIRPrinter.coolTypeToLLVMType(exprObj.type, 1);

        if(exprObj.name.equals("self")) {
            printUtil.loadInstUtil(operandTemp, new ArgumentInfo("this1", operandTemp), new ArgumentInfo(String.valueOf(registerCounter.registerVal), operandTemp));
        } else {
            printUtil.loadInstUtil(operandTemp, new ArgumentInfo(attributeAddressOfObj(exprObj.name, clsName, functionFormalNameList), operandTemp.getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal), operandTemp));
        }

        return new InstructionInfo(registerCounter.registerVal + 1, operandTemp, registerCounter.lastBasicBlockName);
    }

    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.int_const expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating the IR for Int Constants
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT32), String.valueOf(registerCounter.registerVal));
        printUtil.storeInstUtil((ArgumentInfo)new CoolInt((expr).value), new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT32PTR)));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT32),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT32PTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT32), registerCounter.lastBasicBlockName);
    }
}
