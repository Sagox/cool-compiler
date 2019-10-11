package cool;

import java.util.*;

public class InheritanceGraph {
	// number of nodes in the inheritance graph i.e. number of classes in
	// the given program
	int numberOfNodes;
	// a map to store the index at which a given class is found in the program
	// ast given to us
  public HashMap<String, Integer> classIDs = new HashMap<String, Integer>();
  public HashMap<Integer, String> iDName = new HashMap<Integer, String>();
	public HashMap<String, AST.class_> nameClass = new HashMap<String, AST.class_>();

	// the representation of the inhertiance graph in terms of an adjacency list
	public LinkedList<Integer> adjacencyList[];

	ArrayList<String> basicClasses = new ArrayList<String>();
	ArrayList<String> cannotInheritFrom = new ArrayList<String>();

	// given an AST.program construct the inheritance graph of program
	InheritanceGraph(AST.program program) {
		// adding basic classes
		basicClasses.add("Object");
		basicClasses.add("IO");
		basicClasses.add("String");
		basicClasses.add("Int");
		basicClasses.add("Bool");

		// Adding classes that cannot be inherited from
		cannotInheritFrom.add("String");
		cannotInheritFrom.add("Int");
		cannotInheritFrom.add("Bool");

		numberOfNodes = program.classes.size();
		// there are five basic classes in cool Object, IO, String, Int and Bool
		// it is an error to inherit from the latter three, so they will not be
		// present in the inheritance graph, but the former two will be present
		//in the inheritance graph
		adjacencyList = new LinkedList[numberOfNodes + 2];
		classIDs = new HashMap<>();

		// adding the basic classes into the hashmap
    classIDs.put("Object", numberOfNodes);
		iDName.put(numberOfNodes, "Object");
    classIDs.put("IO", numberOfNodes+1);
		iDName.put(numberOfNodes+1, "IO");

		// adding vertices to the inheritance graph, each class is a node in this graph
		// the first two nodes are the basic classes as above
		adjacencyList[numberOfNodes] = new LinkedList<>();
		adjacencyList[numberOfNodes+1] = new LinkedList<>();
		for(int i=0;i<numberOfNodes;i++) {
			String className = program.classes.get(i).name, parentClassName = program.classes.get(i).parent;
			checkClass(program.classes.get(i));
			checkParentClass(program.classes.get(i));
			classIDs.put(className, i);
      iDName.put(i, className);
      nameClass.put(className, program.classes.get(i));
			adjacencyList[i] = new LinkedList<>();
		}

		// adding edges to the adjacency list
		for(int i=0;i<numberOfNodes;i++) {
			AST.class_ currentClass = program.classes.get(i);
			if(classIDs.containsKey(currentClass.parent)) {
				adjacencyList[classIDs.get(currentClass.parent)].add(i);
			} else {
				ErrorReporter.reportError(currentClass.filename, currentClass.lineNo, "Parent does not exist");
				System.exit(1);
			}
		}

	}

	// Sanity check method
	void printInheritanceGraph() {
		System.out.println("Number of nodes " + numberOfNodes + "\n");
		// print out the entire adjacency list
		for(int i=0;i<numberOfNodes+2;i++) {
			System.out.print(i + ": ");
			for(int j=0;j<adjacencyList[i].size();j++) {
				System.out.print(adjacencyList[i].get(j) + " ");
			}
			System.out.print("\n");
		}
	}

	// check for the correctness of the class name before adding it to the inheritance graph
	void checkClass(AST.class_ currentClass) {
		String className = currentClass.name;
		// the basic classes cannot be redefined
		if(basicClasses.contains(className)) {
			ErrorReporter.reportError(currentClass.filename, currentClass.lineNo, "Class name is not allowed");
			System.exit(1);
		// the same class cannot be defined multiple times
		} else if(classIDs.containsKey(className)) {
			ErrorReporter.reportError(currentClass.filename, currentClass.lineNo, "Class redefinition is not allowed");
			System.exit(1);
		}
	}

	// check got the correctness of the parent class before adding it to the inheritance graph
	void checkParentClass(AST.class_ currentClass) {
		String parentClassName = currentClass.parent;
		if(cannotInheritFrom.contains(parentClassName)) {
			// some classes cannot be inherited from according to the cool specification
			ErrorReporter.reportError(currentClass.filename, currentClass.lineNo, "Attemp to inherit from invalid class");
			System.exit(1);
		}
	}


// We use dfs to check for cycles in the inheritance graph, the presence of a cycle can be simply
// confirmed if we can find even one backedge
    Boolean checkBackEdge(int i, boolean[] visited, boolean[] parentStack)
    {

        // check if the current node is already in the parent stack, if yes, implies there is
        // a backedge
        if (parentStack[i])
            return true;

  		// if this node is already visisted and we did not find a backedge there is no point in checking it again
        if (visited[i])
            return false;

        // mark the current node as visited
        visited[i] = true;

  		// add the current node to the parent stack
        parentStack[i] = true;

        // get the children of the current node
        List<Integer> children = adjacencyList[i];

        // recursively check the rest of the children nodes for cycles
        for (int j=0;j<children.size();j++) {
        	int c = children.get(j);
            if (checkBackEdge(c, visited, parentStack))
                return true;
        }
        parentStack[i] = false;

        return false;
    }

    Boolean containsCycle()
    {

    	// arrays to track which nodes have been visited in the dfs
        boolean[] visited = new boolean[numberOfNodes + 2];

        // this is the stack containing all the nodes which we are currently
        // in the subtree of, we use this to check for a backedge
        boolean[] parentStack = new boolean[numberOfNodes + 2];


        // Recursively check for backedges in the tree
        for (int i = 0; i < numberOfNodes+2; i++)
            if (checkBackEdge(i, visited, parentStack))
                return true;

        return false;
    }

}
