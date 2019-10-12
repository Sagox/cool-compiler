package cool;

import java.util.*;

public class Semantic{
	private boolean errorFlag = false;
	public void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag(){
		return errorFlag;
	}

/*
	Don't change code above this line
*/
	public Semantic(AST.program program){
		// first as mentioned in instructions we check if all the properties related to
		// class inheritance holds for the given ast

		// the constructor of this class takes the AST.program as an argument using which the inheritance
		// graph is built, while building, basic checks on classes are also done.
		String filename = program.classes.get(0).filename;
		InheritanceGraph IG = new InheritanceGraph(program);
		// IG.printInheritanceGraph();

		// check if the inheritance graph containts any loops
		if(IG.containsCycle()) {
			ErrorReporter.reportGenericError("There is a cycle in the inheritance graph");
			System.exit(1);
		}

		// Generate class table which is responsible for storing all the classes and its attributes, methods.
		ClassTable CT = new ClassTable(program);
		// Generate scope table using class Table and assign types wherever necessary.
		ScopeTableImpl ST = new ScopeTableImpl(program, IG, CT);
    // System.out.print("checking node bfs: " + CT.cnHm);
    // System.out.print("\n");
	}
}
