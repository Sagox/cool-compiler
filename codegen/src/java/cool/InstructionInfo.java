package cool;

import java.util.*;
import java.io.PrintWriter;

/*
    This Class is a helper class conatining the following information for an IR instruction
    1. Register value used in that instruction
    2. Type of last instruction which is an object of TypeMapping class
    3. Name of last basic block
*/
public class InstructionInfo {
    public int registerVal;
    TypeUtils lastInstructionType;
    String lastBasicBlockName;

    public InstructionInfo() {
        registerVal = 0;
        lastInstructionType = new TypeUtils(TypeUtils.Typegt.EMPTY);
        lastBasicBlockName = "";
    }

    public InstructionInfo(int regVal, TypeUtils operand, String lbType) {
        registerVal = regVal;
        lastInstructionType = operand;
        lastBasicBlockName = lbType;
    }

    public void reintialiseToDefault(int regVal, TypeUtils lit, String lbn) {
        registerVal = regVal;
        lastInstructionType = lit;
        lastBasicBlockName = lbn;
    }
}
