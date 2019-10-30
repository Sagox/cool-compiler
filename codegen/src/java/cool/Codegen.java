package cool;

import java.io.PrintWriter;

public class Codegen{

	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
        out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
        LLVMIRPrinter printer = new LLVMIRPrinter(out);
        printer.printMetaData(program);
        printer.printRequiredCFunctionsDeclaration();
        ClassTable classTable = new ClassTable();
        printer.generateIRForMainClass(program);
	}
}
