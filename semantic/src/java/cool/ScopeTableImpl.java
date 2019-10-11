package cool;

import java.util.*;

public class ScopeTableImpl {
  // // number of nodes in the inheritance graph i.e. number of classes in
  // // the given program
  // int numberOfNodes;
  // // a map to store the index at which a given class is found in the program
  // // ast given to us
  // HashMap<String, Integer> classIDs;

  // // the representation of the inhertiance graph in terms of an adjacency list
  // LinkedList<Integer> adjacencyList[];

  // ArrayList<String> basicClasses = new ArrayList<String>();
  // ArrayList<String> cannotInheritFrom = new ArrayList<String>();

  public ScopeTable<AST.attr> st = new ScopeTable<AST.attr>();
  public List<Error> errors = new ArrayList<Error>();
  public ClassTable CT;
  public String filename;

  ScopeTableImpl(AST.program program, InheritanceGraph IG, ClassTable ClT) {
    this.CT = ClT;
    this.filename = program.classes.get(0).filename;
    int numberOfNodes = program.classes.size();
    //generate the table with bfs
    Queue<Integer> q = new LinkedList<Integer>();
    q.offer(numberOfNodes);
    q.offer(numberOfNodes + 1);
    while (!q.isEmpty()) {
      int classID = q.poll();
      System.out.print("checking node bfs: " + IG.iDName.get(classID));
        System.out.print("\n");
      if(classID != numberOfNodes && classID != numberOfNodes + 1) {
        // insert classes in BFS-order so that methods and attributes can be inherited.
        // System.out.print("checking node bfs: " + IG.iDName.get(classID));
        // System.out.print("\n");
        CT.insert(IG.nameClass.get(IG.iDName.get(classID)));
      }
      for(int i = 0; i < IG.adjacencyList[classID].size(); i++) {
        q.offer(IG.adjacencyList[classID].get(i));
      }
    }
    // insert into scope table
    for(int i = 0; i < numberOfNodes; i++) {
      AST.class_ currClass = program.classes.get(i);
      st.enterScope();
      // insert self
      st.insert("self", new AST.attr("self", currClass.name, new AST.no_expr(currClass.lineNo), currClass.lineNo));
      // now insert all declared and inherited
      System.out.print(i + " : ST attr : " + currClass.name + CT.cnHm);
      System.out.print("\n");
      HashMap<String, AST.attr> attributes = CT.cnHm.get(currClass.name).attrs;
      for(String attrName : attributes.keySet()) {
        st.insert(attrName, attributes.get(attrName));
      }
      traverse(currClass);
      st.exitScope();
    }
  }

  void traverse(AST.class_ currClass) {
    for(int i = 0; i < currClass.features.size(); i++) {
      if(currClass.features.get(i).getClass() == AST.method.class) {
        AST.method currMethod = (AST.method) currClass.features.get(i);
        st.enterScope();
        for(int j = 0; j < currMethod.formals.size(); j++) {
          if(st.lookUpLocal(currMethod.formals.get(j).name) != null && st.lookUpLocal(currMethod.formals.get(j).name).getClass() == AST.attr.class) {
            ErrorReporter.reportError(currClass.filename,
              currMethod.lineNo,"Method's parameter - " + currMethod.name + " is redefined.");
          }
          st.insert(currMethod.formals.get(j).name, new AST.attr(currMethod.formals.get(j).name,
            currMethod.formals.get(j).typeid, new AST.no_expr(currMethod.formals.get(j).lineNo),
            currMethod.formals.get(j).lineNo));
        }
        traverseNode(currMethod.body);
        if(CT.isConforming(currMethod.body.type, currMethod.typeid) == false) {
          ErrorReporter.reportError(currClass.filename,currMethod.body.lineNo,
            "Declared Return type - " + currMethod.typeid + " is not equal to " + currMethod.body.type);
        }
        st.exitScope();
      }
      else if(currClass.features.get(i).getClass() == AST.attr.class){
        // If attr's value is an expression class
        // undeclared??
        AST.attr currAttr = (AST.attr) currClass.features.get(i);
        if(currAttr.value.getClass() != AST.no_expr.class) {
            traverseNode(currAttr.value);
            // conform??
            if(CT.isConforming(currAttr.value.type, currAttr.typeid) == false) {
              ErrorReporter.reportError(filename, currAttr.value.lineNo, "Inferred type " + currAttr.value.type + " of initialization of attribute "
                  + currAttr.name + " does not conform to declared type " + currAttr.typeid);
            }
        }
      }
    }
  }

private void traverseNode(AST.expression expr) {
    /* traverseNode functions given in the following order
     * program = class_list
     * class =
     * feature = method / attribute
     * ID <- expr [ASSIGN]
     * <expr>.<id>(<expr>,....) [DISPATCH]
     * <expr>@<type>.<id> (<expr>,....) [STATIC DISPATCH]
     * if <expr> then <expr> else <expr> fi [COND]
     * while <expr> loop <expr> pool [WHILE]
     * { <expr>, <expr>, ... }  [BLOCK]
     * let ID : TYPE <- expr  [LET-EXPR]
     * case <expr> of ID : TYPE => expr esac [CASE]
     * new TYPE [NEW]
     * isvoid <expr> [ISVOID]
     * expr + expr  [PLUS]
     * expr - expr  [SUB]
     * expr * expr  [MUL]
     * expr / expr  [DIVIDE]
     * ~expr    [COMP]
     * expr < expr  [LT]
     * expr <= expr [LEQ]
     * expr = expr  [EQ]
     * not expr   [NEG]
     * (expr)
     * ID     [
     * integer    [INT_CONST]
     * string   [STRING_CONST]
     * true     [BOOL_CONST]
     * false    [BOOL_CONST]
     */
    // constants - Bool, Int, String
    if(expr.getClass() == AST.bool_const.class)
      ((AST.bool_const)expr).type = "Bool";
    else if(expr.getClass() == AST.int_const.class)
      ((AST.int_const)expr).type = "Int";
    else if(expr.getClass() == AST.string_const.class)
      ((AST.string_const)expr).type = "String";
    // object
    else if(expr.getClass() == AST.object.class) {
      AST.object expr1 = (AST.object)expr;
      if(st.lookUpGlobal(expr1.name) == null) {
          ErrorReporter.reportError(filename, expr1.lineNo, "Undeclared Indentifier - '" + expr1.name);
          // assign Object
          expr1.type = "Object";
      } else expr1.type = st.lookUpGlobal(expr1.name).typeid;
    }
    // complement
    else if(expr.getClass() == AST.comp.class) {
      AST.comp expr1 = (AST.comp)expr;
      traverseNode(expr1.e1);
      if(expr1.e1.type.equals("Int") == false) {
            ErrorReporter.reportError(filename, expr1.lineNo, "In the compliment, argument must have type Int instead of type '" + expr1.e1.type + "'.");
        }
      // complement of int is still of type int.
      expr1.type = "Int";
    }
    else if(expr.getClass() == AST.eq.class) {
      AST.eq expr1 = (AST.eq)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);

      if((expr1.e1.type.equals("Bool") || expr1.e1.type.equals("Int") || expr1.e1.type.equals("String"))) {
        if(expr1.e1.type.equals(expr1.e2.type) == false) {
            ErrorReporter.reportError(filename, expr1.lineNo, "The argument in equals have different types '" + expr1.e1.type + "'"+ " and '" + expr1.e2.type + "'.");
        }
      }
      // return true or false, so boolean
      expr1.type = "Bool";
    }
    else if(expr.getClass() == AST.leq.class) {
      AST.leq expr1 = (AST.leq)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);

      if(expr1.e1.type.equals("Int") == false ) {
        ErrorReporter.reportError(filename, expr1.lineNo, "The argument in less-than equal to is of type non-Int '" + expr1.e1.type + "'");
      } else if(expr1.e2.type.equals("Int") == false) {
        ErrorReporter.reportError(filename, expr1.lineNo, "The argument in less-than equal to is of type non-Int '" + expr1.e2.type + "'");
      }
      // return true or false, so boolean
      expr1.type = "Bool";
    }
    else if(expr.getClass() == AST.lt.class) {
      AST.lt expr1 = (AST.lt)expr;
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);

      if(expr1.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in less-than is of type non-Int '" + expr1.e1.type + "'");
      } else if(expr1.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in less-than is of type non-Int '" + expr1.e2.type + "'");
      }
      // return true or false, so boolean
      expr1.type = "Bool";
    }
    else if(expr.getClass() == AST.neg.class) {
      AST.neg expr1 = (AST.neg)expr;
      // assign the types
      traverseNode(expr1.e1);
      if(expr1.e1.type.equals("Bool") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "In the negation, argument must have type 'Bool' instead of type '" + expr1.e1.type + "'.");
      }
      // return negation of true or false, so boolean
      expr1.type = "Bool";
    }
    else if(expr.getClass() == AST.divide.class) {
      AST.divide expr1 = (AST.divide)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);
      if(expr1.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument(first) in division is of type non-Int '" + expr1.e1.type + "'");
      }
      if(expr1.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument(second) in division is of type non-Int '" + expr1.e2.type + "'");
      }
      expr1.type = "Int";
    }
    else if(expr.getClass() == AST.mul.class) {
      AST.mul expr1 = (AST.mul)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);
      if(expr1.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in multiplication is of type non-Int '" + expr1.e1.type + "'");
      }
      if(expr1.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in multiplication is of type non-Int '" + expr1.e2.type + "'");
      }
      expr1.type = "Int";
    }
    else if(expr.getClass() == AST.sub.class) {
      AST.sub expr1 = (AST.sub)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);
      if(expr1.e1.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in subtraction is of type non-Int '" + expr1.e1.type + "'");
      }
      if(expr1.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in subtraction is of type non-Int '" + expr1.e2.type + "'");
      }
      expr1.type = "Int";
    }
    else if(expr.getClass() == AST.plus.class) {
      AST.plus expr1 = (AST.plus)expr;
      // assign the types
      traverseNode(expr1.e1);
      traverseNode(expr1.e2);
      if(expr1.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in addition is of type non-Int '" + expr1.e1.type + "'");
      }
      if(expr1.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The argument in addition is of type non-Int '" + expr1.e2.type + "'");
      }
      expr1.type = "Int";
    }
    else if(expr.getClass() == AST.isvoid.class) {
      AST.isvoid expr1 = (AST.isvoid)expr;
      expr1.type = "Bool";
    }
    else if(expr.getClass() == AST.new_.class) {
      AST.new_ expr1 = (AST.new_)expr;
      // get class info
      if(CT.cnHm.get(expr1.typeid) != null)
        expr1.type = expr1.typeid;
      else {
        ErrorReporter.reportError(filename, expr1.lineNo, "The class '" + expr1.typeid +"' with 'new' is undefined.");
        expr1.type = "Object";
      }
    }
    else if(expr.getClass() == AST.assign.class) {
      AST.assign expr1 = (AST.assign)expr;
      AST.attr attr = st.lookUpGlobal(expr1.name);
      traverseNode(expr1.e1);
      if(attr == null) {
          ErrorReporter.reportError(filename, expr1.lineNo, "Variable '" + expr1.name + "' is undeclared during assignment.");
      } else if(CT.isConforming(expr1.e1.type, attr.typeid) == false) {
          ErrorReporter.reportError(filename, expr1.lineNo, "The type '" + attr.typeid + "' of identifier '" + attr.name + "' does not match with the type '" + expr1.e1.type + "' of the expression.");
      }
      expr1.type = expr1.e1.type;
    }
    else if(expr.getClass() == AST.block.class) {
      AST.block expr1 = (AST.block)expr;
      for(int i = 0; i < expr1.l1.size(); i++) {
        traverseNode(expr1.l1.get(i));
      }
      // get last expression's type
      expr1.type = expr1.l1.get(expr1.l1.size() - 1).type;
    }
    else if(expr.getClass() == AST.loop.class) {
      AST.loop expr1 = (AST.loop)expr;
      traverseNode(expr1.predicate);
      if(expr1.predicate.type.equals("Bool") == false) {
          ErrorReporter.reportError(filename, expr1.predicate.lineNo, "Loop condition's return type is not 'Bool'");
      }
      traverseNode(expr1.body);
      expr1.type = "Object";
    }
    else if(expr.getClass() == AST.cond.class) {
      AST.cond expr1 = (AST.cond)expr;
        traverseNode(expr1.predicate);
        traverseNode(expr1.ifbody);
        traverseNode(expr1.elsebody);
        if(expr1.predicate.type.equals("Bool") == false) {
            ErrorReporter.reportError(filename, expr1.predicate.lineNo, "Predicate return type does not match type 'Bool'");
        }
        // ********
        // The common ancestor class of ifbody expression and elsebody expression is assigned to 'cond' type
        expr1.type = CT.commonAncestor(expr1.ifbody.type, expr1.elsebody.type);
    }
    // let ID : TYPEID [ <- expression ]
    else if(expr.getClass() == AST.let.class) {
      AST.let expr1 = (AST.let)expr;
      if(expr1.value.getClass() != AST.no_expr.class) {
        traverseNode(expr1.value);
        if(CT.isConforming(expr1.value.type, expr1.typeid) == false) {
            ErrorReporter.reportError(filename, expr1.lineNo, "The identifier " + expr1.name + " declared type '" + expr1.typeid + "' does not match with let value type '" + expr1.value.type + ".");
        }
      }
      traverseNode(expr1.body);
      st.enterScope();
      st.insert(expr1.name, new AST.attr(expr1.name, expr1.typeid, expr1.value, expr1.lineNo));
      expr1.type = expr1.body.type;
      st.exitScope();
    }
    // <expr>.<id>(<expr>,....)
    else if(expr.getClass() == AST.dispatch.class) {
      AST.dispatch expr1 = (AST.dispatch)expr;
        AST.method mthd = null;
        boolean found = false;

        // Calling the visit node on caller expression
        traverseNode(expr1.caller);

        for(AST.expression exp : expr1.actuals) {
            // actuals is the list of expressions
            traverseNode(exp);
        }

        // Returns the type class basic block for caller expression of dispatch
        ClassNode bcb = CT.cnHm.get(expr1.caller.type);
        if(bcb == null) {
            // Means no type is returned
            ErrorReporter.reportError(filename, expr1.lineNo, "Undefined class '" + expr1.caller.type + "' of dispatch caller type.");
        } else {
            if(bcb.methods.containsKey(expr1.name)) {
                // Means the dispatch method is present in the method list of basic class block
                found = true;
                mthd = bcb.methods.get(expr1.name);

                // Now we will compare the method formal list and number of parameters to the dispatch method
                if(expr1.actuals.size() != mthd.formals.size()) {
                    // Different number of parameters in the called dispatch
                    ErrorReporter.reportError(filename, expr1.lineNo, "Different number of arguments present in the method '" + mthd.name + "' as compared to the defined method.");
                } else {
                    // The number of parameters match but the type of individual parameter may not be same as defined
                    for(int i=0; i<expr1.actuals.size(); i++) {
                        if(CT.isConforming(expr1.actuals.get(i).type, mthd.formals.get(i).typeid) == false) {
                            // Means the type does not match
                            ErrorReporter.reportError(filename, expr1.lineNo, "The method type '" + expr1.actuals.get(i).type + "' does not match with the declared type '" + mthd.formals.get(i).typeid + "' in the method '" + mthd.name + "'.");
                        }
                    }
                }
            } else {
                // Means the method is not defined in any class
                ErrorReporter.reportError(filename, expr1.lineNo, "Method '" + expr1.name + "' is undefined.");
            }
        }

        if(found) {
            expr1.type = mthd.typeid;
        } else {
            expr1.type = "Object";
        }
    }
    // * <expr>@<type>.<id> (<expr>,....)
    else if(expr.getClass() == AST.static_dispatch.class) {
      AST.static_dispatch expr1 = (AST.static_dispatch)expr;
        AST.method mthd = null;
        boolean found = false;

        // Calling the visit node on caller expression
        traverseNode(expr1.caller);

        for(AST.expression exp : expr1.actuals) {
            // actuals is the list of expressions
            traverseNode(exp);
        }

        // Returns the type class basic block for caller expression of dispatch
        ClassNode bcb = CT.cnHm.get(expr1.typeid);
        if(bcb == null) {
            // Means no type is returned
            ErrorReporter.reportError(filename, expr1.lineNo, "Undefined class '" + expr1.typeid + "' of Static dispatch type.");
        } else if(CT.isConforming(expr1.caller.type, bcb.name) == false) {
            // Means the static dispatch type doesn't match to the expression type
            ErrorReporter.reportError(filename, expr1.lineNo, "The declared static dispatch type '" + bcb.name + "' is different from the expression type '" + expr1.caller.type + "'.");
        } else {
            if(bcb.methods.containsKey(expr1.name)) {
                // Means the static dispatch method is present in the method list of basic class block
                found = true;
                mthd = bcb.methods.get(expr1.name);

                // Now we will compare the method formal list and number of parameters to the static dispatch method
                if(expr1.actuals.size() != mthd.formals.size()) {
                    // Different number of parameters in the called static dispatch
                    ErrorReporter.reportError(filename, expr1.lineNo, "Different number of arguments present in the method '" + mthd.name + "' as compared to the defined method.");
                } else {
                    // The number of parameters match but the type of individual parameter may not be same as defined
                    for(int i=0; i<expr1.actuals.size(); i++) {
                        if(CT.isConforming(expr1.actuals.get(i).type, mthd.formals.get(i).typeid) == false) {
                            // Means the type does not match
                            ErrorReporter.reportError(filename, expr1.lineNo, "The method type '" + expr1.actuals.get(i).type + "' does not match with the declared type '" + mthd.formals.get(i).typeid + "' in the method '" + mthd.name + "'.");
                        }
                    }
                }
            } else {
                // Means the method is not defined in any class
                ErrorReporter.reportError(filename, expr1.lineNo, "Method '" + expr1.name + "' is undefined.");
            }
        }

        if(found) {
            expr1.type = mthd.typeid;
        } else {
            expr1.type = "Object";
        }
    }
    else if(expr.getClass() == AST.typcase.class) {
      AST.typcase expr1 = (AST.typcase)expr;
        traverseNode(expr1.predicate);
        for(AST.branch br : expr1.branches) {
            // We are iterating over the branches of the typcase expression
            st.enterScope();
            ClassNode cl = CT.cnHm.get(br.type);

            if(cl == null) {
                ErrorReporter.reportError(filename, br.lineNo, "Case branch has undefined type '" + br.type + "'.");
                // To recover from the error, we add this unidentified class
                st.insert(br.name, new AST.attr(br.name, "Object", br.value, br.lineNo));
            } else {
                st.insert(br.name, new AST.attr(br.name, br.type, br.value, br.lineNo));
            }

            // Visiting the branch value expression
            traverseNode(br.value);
            st.exitScope();
        }

        HashMap <String, Boolean> brnchMap = new HashMap<String, Boolean>();
        AST.branch brn = expr1.branches.get(0);
        String brType = brn.value.type;

        for(AST.branch br : expr1.branches) {
            if(brnchMap.containsKey(br.type) == false) {
                brnchMap.put(br.type, true);
            } else {
                ErrorReporter.reportError(filename, br.lineNo, "Another branch has same type '" + br.type + "'.");
            }
            brType = CT.commonAncestor(brType, br.value.type);
        }

        // Updating the type of typcase with last branch type
        expr1.type = brType;
    }
  }
}
