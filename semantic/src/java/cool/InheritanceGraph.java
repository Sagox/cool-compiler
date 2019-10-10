package cool;

import java.util.*;

public class InheritanceGraph {
	// number of nodes in the inheritance graph i.e. number of classes in
	// the given program
	int numberOfNodes;
	// a map to store the index at which a given class is found in the program
	// ast given to us
	HashMap<String, Integer> classIDs;

	// the representation of the inhertiance graph in terms of an adjacency list
	LinkedList<Integer> adjacencyList[];

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
		classIDs.put("IO", numberOfNodes+1);

		// adding vertices to the inheritance graph, each class is a node in this graph
		// the first two nodes are the basic classes as above
		adjacencyList[numberOfNodes] = new LinkedList<>();
		adjacencyList[numberOfNodes+1] = new LinkedList<>();
		for(int i=0;i<numberOfNodes;i++) {
			String className = program.classes.get(i).name, parentClassName = program.classes.get(i).parent;
			checkClassName(className);
			checkParentClassName(parentClassName);
			classIDs.put(className, i);
			adjacencyList[i] = new LinkedList<>();
		}

		// adding edges to the adjacency list
		for(int i=0;i<numberOfNodes;i++) {
			if(classIDs.containsKey(program.classes.get(i).parent)) {
				adjacencyList[classIDs.get(program.classes.get(i).parent)].add(i);
			} else {
				System.out.println("Parent does not exist?\n");
			}
		}

	}

	// Sanity check method
	void printInheritanceGraph() {
		System.out.println("Number of nodes " + numberOfNodes + "\n");
		// print out the entire adjacency list
		for(int i=0;i<numberOfNodes;i++) {
			System.out.print(i + ": ");
			for(int j=0;j<adjacencyList[i].size();j++) {
				System.out.print(adjacencyList[i].get(j) + " ");
			}
			System.out.print("\n");
		}
	}

	// check for the correctness of the class name before adding it to the inheritance graph
	Boolean checkClassName(String className) {
		// the basic classes cannot be redefined
		if(basicClasses.contains(className)) {
			System.out.println("Cannot redefine basic class\n");
			return false;
		// the same class cannot be defined multiple times
		} else if(classIDs.containsKey(className)) {
			System.out.println("Cannot redefine class\n");
			return false;
		} else return true;
	}

	// check got the correctness of the parent class before adding it to the inheritance graph
	Boolean checkParentClassName(String parentClassName) {
		if(cannotInheritFrom.contains(parentClassName)) {
			// some classes cannot be inherited from according to the cool specification
			System.out.println("Invalid Inheritance");
			return false;
		} else return true;
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