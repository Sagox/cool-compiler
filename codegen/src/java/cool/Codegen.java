package cool;

import java.io.PrintWriter;

public class Codegen{

	void printMetaData(AST.program program) {
		String filename = program.classes.get(0).filename;
		// assumed to be same as filename
		System.out.println("; ModuleID = \""+filename+"\"");
		System.out.println("source_filename = \""+filename+"\"");
		// layout is in the following format
		// little endian | name mangling | int size and alignment | float size and alignment
		// | native integer widths supported | stack allignment
		System.out.println("target datalayout = \"e-m:e-i64:64-f80:128-n8:16:32:64-S128\"");
		System.out.println("target triple = \"x86_64-unknown-linux-gnu\"");
	}

	public Codegen(AST.program program, PrintWriter out){
		//Write Code generator code here
        out.println("; I am a comment in LLVM-IR. Feel free to remove me.");
        printMetaData(program);
        ClassTable classTable = new ClassTable();
	}
}
