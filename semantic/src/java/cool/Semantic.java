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
		InheritanceGraph IG = new InheritanceGraph(program);
		IG.printInheritanceGraph();
		if(IG.containsCycle())
			System.out.println("There are cycles! :(");
		else System.out.println("No cycles! :)");
	}
}