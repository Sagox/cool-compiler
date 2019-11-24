package cool;

import java.util.*;
import java.io.PrintWriter;

public class VisitNodeByClass {
    public ClassTable ct;

    VisitNodeByClass(ClassTable classTable) {
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
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.bool_const exprBool, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT1), String.valueOf(registerCounter.registerVal));
        printUtil.storeInstUtil((ArgumentInfo)new CoolBool((exprBool).value), new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1PTR)));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT1),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1PTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT1)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT1), registerCounter.lastBasicBlockName);
    }

    // exprString
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.string_const exprString, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        String str_val = exprString.value;
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), String.valueOf(registerCounter.registerVal));
        out.print("\tstore i8* getelementptr inbounds ([" + String.valueOf(str_val.length() + 1) + " x i8], [" + String.valueOf(str_val.length() + 1) + " x i8]* @.str." + LLVMIRPrinter.stringToLineNoMapping.get(str_val) + ", i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT8DOUBLEPTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT8PTR)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT8PTR), registerCounter.lastBasicBlockName);
    }

    // int_const
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.int_const exprInt, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT32), String.valueOf(registerCounter.registerVal));
        printUtil.storeInstUtil((ArgumentInfo)new CoolInt((exprInt).value), new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT32PTR)));
        printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT32),  new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT32PTR)), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT32), registerCounter.lastBasicBlockName);
    }

    // object
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.object exprObj, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        TypeUtils interType = LLVMIRPrinter.coolTypeToLLVMType(exprObj.type, 1);
        if(exprObj.name.equals("self")) {
            printUtil.loadInstUtil(interType, new ArgumentInfo("this1", interType), new ArgumentInfo(String.valueOf(registerCounter.registerVal), interType));
        } else {
            printUtil.loadInstUtil(interType, new ArgumentInfo(attributeAddressOfObj(exprObj.name, clsName, functionFormalNameList), interType.getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal), interType));
        }
        return new InstructionInfo(registerCounter.registerVal + 1, interType, registerCounter.lastBasicBlockName);
    }

    // equal
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.eq expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);

        if(op1.lastInstructionType.name.equals("i1")) {
            printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "EQ", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)));
        } else if(op1.lastInstructionType.name.equals("i32")) {
            printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "EQ", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));
        } else if(op1.lastInstructionType.name.equals("i8*")) {
            ArrayList<ArgumentInfo> args = new ArrayList<ArgumentInfo>();
            ArgumentInfo retVal = new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT1));

            args.add(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            args.add(new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT8PTR)));
            printUtil.printCallInstruction(new ArrayList<TypeUtils>(), "String_strcmp", true, args, retVal);
        }

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    // assign
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.assign node, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        registerCounter = VisitorPattern(out, printUtil, node.e1, registerCounter, clsName, functionFormalNameList);
        String getAddr = attributeAddressOfObj(node.name, clsName, functionFormalNameList);
        printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType), new ArgumentInfo(getAddr, registerCounter.lastInstructionType.getPtr()));
        return registerCounter;
    }

    // comp
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.comp exprComp, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (exprComp).e1, registerCounter, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "xor", (ArgumentInfo)new CoolBool(true), new ArgumentInfo(String.valueOf(op1.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)));

        return new InstructionInfo(op1.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op1.lastBasicBlockName);
    }

    // less than equal
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.leq expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "LE", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    // less than
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.lt expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "LT", new ArgumentInfo(String.valueOf(op2.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT1), op2.lastBasicBlockName);
    }

    // neg = (* -1)
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.neg expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "mul", (ArgumentInfo)new CoolInt(-1), new ArgumentInfo(String.valueOf(op1.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op1.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op1.lastBasicBlockName);
    }

    // add
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.plus expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT32)), "add", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    // sub
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.sub expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating Code for subtraction Operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "sub", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    // mul
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.mul expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        // Generating code for multiplication operation
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "mul", new ArgumentInfo(String.valueOf(op2.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)));

        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), op2.lastBasicBlockName);
    }

    // div. two cases - divisor is 0 or !0
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.divide expr, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        InstructionInfo op1 = VisitorPattern(out, printUtil, (expr).e1, registerCounter, clsName, functionFormalNameList);
        InstructionInfo op2 = VisitorPattern(out, printUtil, (expr).e2, op1, clsName, functionFormalNameList);
        op2.registerVal--;
        printUtil.cmpInstUtil(new ArgumentInfo(String.valueOf(op2.registerVal), new TypeUtils(TypeUtils.Typegt.INT32)), "EQ", (ArgumentInfo)new CoolInt(0), new ArgumentInfo("comp_" + String.valueOf(op2.registerVal) + "_0", new TypeUtils(TypeUtils.Typegt.INT1)));
        printUtil.brConditionUtil(new ArgumentInfo("comp_" + String.valueOf(op2.registerVal)  + "_0", new TypeUtils(TypeUtils.Typegt.INT1)), "func_div_by_zero_err", "branch_" + String.valueOf(op2.registerVal)  + "_0");

        out.println("\nproceed_" + String.valueOf(op2.registerVal)  + "_0:");
        printUtil.arithmeticUtil(new ArgumentInfo(String.valueOf(op1.registerVal - 1 ), new TypeUtils(TypeUtils.Typegt.INT32)), "udiv", new ArgumentInfo(String.valueOf(op2.registerVal),new TypeUtils(TypeUtils.Typegt.INT32)), new ArgumentInfo(String.valueOf(op2.registerVal + 1), new TypeUtils(TypeUtils.Typegt.INT32)));
        //reset
        op2.registerVal++;
        return new InstructionInfo(op2.registerVal + 1, new TypeUtils(TypeUtils.Typegt.INT32), "branch_" + String.valueOf(op2.registerVal - 1) + "_0:");
    }

    // Static dispatch - <expr>@<type>.id(<expr>,...,<expr>)
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.static_dispatch exprSD, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        ArrayList<ArgumentInfo> argumentList = new ArrayList<ArgumentInfo>();

        if((exprSD.typeid.equals("Int") || exprSD.typeid.equals("Bool") || exprSD.typeid.equals("String")) == false) {
            registerCounter = VisitorPattern(out, printUtil, exprSD.caller, registerCounter, clsName, functionFormalNameList);
            out.print("\t%" + registerCounter.registerVal + " = icmp eq " + registerCounter.lastInstructionType.name + " null, %" + (registerCounter.registerVal - 1) + "\n");
            printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal), new TypeUtils(TypeUtils.Typegt.INT1)), "dispatch_on_void_basic_block", "branch_" + String.valueOf(registerCounter.registerVal));

            out.print("\nproceed_" + String.valueOf(registerCounter.registerVal) + ":\n");
            registerCounter.registerVal = registerCounter.registerVal + 1;
            registerCounter.lastBasicBlockName = "branch_" + String.valueOf(registerCounter.registerVal) + ":";
        }

        for(AST.expression expAct : exprSD.actuals) {
            registerCounter = VisitorPattern(out, printUtil, expAct, registerCounter, clsName, functionFormalNameList);
            argumentList.add(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType));
        }

        registerCounter = VisitorPattern(out, printUtil, exprSD.caller, registerCounter, clsName, functionFormalNameList);
        if(exprSD.typeid.equals("IO") == false) // add to the start
            argumentList.add(0, new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), registerCounter.lastInstructionType));

        printUtil.printCallInstruction(new ArrayList<TypeUtils>(), exprSD.typeid + "_" + exprSD.name, true, argumentList, new ArgumentInfo(String.valueOf(registerCounter.registerVal), returnTypeOfMethod(exprSD.typeid, exprSD.name)));
        if(returnTypeOfMethod(exprSD.typeid, exprSD.name).gt.equals(TypeUtils.Typegt.VOID)) {
            return new InstructionInfo(registerCounter.registerVal, returnTypeOfMethod(exprSD.typeid, exprSD.name), registerCounter.lastBasicBlockName);
        } else {
            return new InstructionInfo(registerCounter.registerVal + 1, returnTypeOfMethod(exprSD.typeid, exprSD.name), registerCounter.lastBasicBlockName);
        }
    }

    // if else
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.cond exprCond, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        int counter = LLVMIRPrinter.nestedIfCount;
        LLVMIRPrinter.nestedIfCount = LLVMIRPrinter.nestedIfCount + 1;

        // predicate
        InstructionInfo predInst = VisitorPattern(out, printUtil, exprCond.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, registerCounter.lastBasicBlockName), clsName, functionFormalNameList);
        printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(predInst.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "if.then" + String.valueOf(counter), "if.else" + String.valueOf(counter));

        // then body
        out.println("\nif.then" + String.valueOf(counter) + ":");
        InstructionInfo thenInst = VisitorPattern(out, printUtil, exprCond.ifbody, new InstructionInfo(predInst.registerVal, predInst.lastInstructionType, "%if.then" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brUncoditionUtil("if.end" + String.valueOf(counter));

        // else
        out.println("\nif.else" + String.valueOf(counter) + ":");
        InstructionInfo elseInst = VisitorPattern(out, printUtil, exprCond.elsebody, new InstructionInfo(thenInst.registerVal, thenInst.lastInstructionType, "%if.else" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brUncoditionUtil("if.end" + String.valueOf(counter));

        // exit
        out.println("\nif.end" + String.valueOf(counter) + ":");

        TypeUtils conditionType = elseInst.lastInstructionType;
        if(conditionType.name.equals("void")) {
            return new InstructionInfo(elseInst.registerVal , conditionType, "%if.end" + String.valueOf(counter));
        }
        out.println("\t%" + elseInst.registerVal + " = phi " + conditionType.name + " [ %" + (thenInst.registerVal - 1) + ", " + (thenInst.lastBasicBlockName) + " ]" + ", " + " [ %" + (elseInst.registerVal - 1) + ", " + (elseInst.lastBasicBlockName) + " ]");
        if(LLVMIRPrinter.mthdType.name.equals(elseInst.lastInstructionType.name) && ((LLVMIRPrinter.mthdType.name.equals("void")) == false && elseInst.registerVal >= 0)) {
            printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(elseInst.registerVal), LLVMIRPrinter.mthdType), new cool.ArgumentInfo("retval", LLVMIRPrinter.mthdType.getPtr()));
        }
        return new InstructionInfo(elseInst.registerVal + 1, conditionType, "%if.end" + String.valueOf(counter));
    }

    // loops
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.loop exprLoop, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        int counter = LLVMIRPrinter.nestedLoopCount;
        LLVMIRPrinter.nestedLoopCount = LLVMIRPrinter.nestedLoopCount + 1;
        printUtil.brUncoditionUtil("for.cond" + String.valueOf(counter));
        // loop init
        out.println("\nfor.cond" + String.valueOf(counter) + ":");
        // Predicate of Loop
        InstructionInfo loopPred = VisitorPattern(out, printUtil, exprLoop.predicate, new InstructionInfo(registerCounter.registerVal, registerCounter.lastInstructionType, "%for.cond" + String.valueOf(counter)), clsName, functionFormalNameList);
        printUtil.brConditionUtil(new ArgumentInfo(String.valueOf(loopPred.registerVal - 1), new TypeUtils(TypeUtils.Typegt.INT1)), "for.body" + String.valueOf(counter), "for.end" + String.valueOf(counter));
        // loop body
        out.println("\nfor.body" + String.valueOf(counter) + ":");
        InstructionInfo bodyLoop = VisitorPattern(out, printUtil, exprLoop.body, new InstructionInfo(loopPred.registerVal, new TypeUtils(TypeUtils.Typegt.INT1), "%for.body" + String.valueOf(counter)), clsName, functionFormalNameList);
        TypeUtils loopTypeVar = bodyLoop.lastInstructionType;
        printUtil.brUncoditionUtil("for.cond" + String.valueOf(counter));
        out.println("\nfor.end" + String.valueOf(counter) + ":");

        return (new InstructionInfo(bodyLoop.registerVal, loopTypeVar, "%for.end" + String.valueOf(counter)));
    }

    // block
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.block exprBlock, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        for(AST.expression interExpr : exprBlock.l1) {
            registerCounter = VisitorPattern(out, printUtil, interExpr, registerCounter, clsName, functionFormalNameList);
        }
        if(registerCounter.registerVal - 1 >= 0 && LLVMIRPrinter.mthdType.name.equals(registerCounter.lastInstructionType.name) && ((LLVMIRPrinter.mthdType.name.equals("void")) == false)) {
            printUtil.storeInstUtil(new ArgumentInfo(String.valueOf(registerCounter.registerVal - 1), LLVMIRPrinter.mthdType), new cool.ArgumentInfo("retval", LLVMIRPrinter.mthdType.getPtr()));
        }
        return registerCounter;
    }

    // new
    public InstructionInfo VisitNode(PrintWriter out, LLVMIRPrinter printUtil, AST.new_ exprNew, InstructionInfo registerCounter, String clsName, List<String> functionFormalNameList) {
        if(exprNew.typeid.equals("Bool")) {
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT1), String.valueOf(registerCounter.registerVal));
            printUtil.storeInstUtil((ArgumentInfo)new CoolBool(false), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT1)).getPtr()));
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT1), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT1)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), (new TypeUtils(TypeUtils.Typegt.INT1))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT1), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("Int")) {
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT32), String.valueOf(registerCounter.registerVal));
            printUtil.storeInstUtil((ArgumentInfo)new CoolInt(0), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT32)).getPtr()));
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT32), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT32)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1),(new TypeUtils(TypeUtils.Typegt.INT32))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT32), registerCounter.lastBasicBlockName);
        } else if(exprNew.typeid.equals("String")) {
            printUtil.printAllocaInstruction(new TypeUtils(TypeUtils.Typegt.INT8PTR), String.valueOf(registerCounter.registerVal));
            out.print("\tstore i8* getelementptr inbounds (" + "[1 x i8]" + ", " + "[1 x i8]" + "* @.str.empty, i32 0, i32 0), i8** %" + String.valueOf(registerCounter.registerVal) + "\n");
            printUtil.loadInstUtil(new TypeUtils(TypeUtils.Typegt.INT8PTR), new ArgumentInfo(String.valueOf(registerCounter.registerVal), (new TypeUtils(TypeUtils.Typegt.INT8PTR)).getPtr()), new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), (new TypeUtils(TypeUtils.Typegt.INT8PTR))));
            return new InstructionInfo(registerCounter.registerVal + 2, new TypeUtils(TypeUtils.Typegt.INT8PTR), registerCounter.lastBasicBlockName);
        }
        // else
        printUtil.printAllocaInstruction(LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 0), String.valueOf(registerCounter.registerVal));
        ArrayList<ArgumentInfo> operandList;
        operandList = new ArrayList<ArgumentInfo>();

        operandList.add(new ArgumentInfo(String.valueOf(registerCounter.registerVal), LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1)));
        printUtil.printCallInstruction(new ArrayList<TypeUtils>(), exprNew.typeid + "_Cons_" + exprNew.typeid, true, operandList, new ArgumentInfo(String.valueOf(registerCounter.registerVal + 1), LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1)));
        return new InstructionInfo(registerCounter.registerVal + 2, LLVMIRPrinter.coolTypeToLLVMType(exprNew.typeid, 1), registerCounter.lastBasicBlockName);
    }
}
