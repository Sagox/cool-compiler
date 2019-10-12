package cool;

import java.util.*;

public class ScopeTableImpl {

  public ScopeTable<AST.attr> st = new ScopeTable<AST.attr>();
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
      // System.out.print("checking node bfs: " + IG.iDName.get(classID));
      //   System.out.print("\n");
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
      // System.out.print(i + " : ST attr : " + currClass.name + CT.cnHm);
      // System.out.print("\n");
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
              currMethod.lineNo,"Method's parameter - '" + ((AST.attr)st.lookUpLocal(currMethod.formals.get(j).name)).name + "' is redefined");
          }
          st.insert(currMethod.formals.get(j).name, new AST.attr(currMethod.formals.get(j).name,
            currMethod.formals.get(j).typeid, new AST.no_expr(currMethod.formals.get(j).lineNo),
            currMethod.formals.get(j).lineNo));
        }
        traverseNode(currMethod.body);
        if(CT.typeCheck(currMethod.body.type, currMethod.typeid) == false) {
          ErrorReporter.reportError(currClass.filename,currMethod.body.lineNo,
            "Method return type - '" + currMethod.typeid + "' is not equal to actual type '" + currMethod.body.type + "'");
        }
        st.exitScope();
      }
      else if(currClass.features.get(i).getClass() == AST.attr.class){
        AST.attr currAttr = (AST.attr) currClass.features.get(i);
        if(currAttr.value.getClass() != AST.no_expr.class) {
            traverseNode(currAttr.value);
            if(CT.typeCheck(currAttr.value.type, currAttr.typeid) == false) {
              ErrorReporter.reportError(filename, currAttr.value.lineNo, "Declared Type - '" + currAttr.typeid + "' of '"
                  + currAttr.name + "' is not equal to actual type '" + currAttr.value.type + "'");
            }
        }
      }
    }
  }

private void traverseNode(AST.expression expr) {
    // constants - Bool, Int, String
    if(expr.getClass() == AST.bool_const.class)
      ((AST.bool_const)expr).type = "Bool";
    else if(expr.getClass() == AST.int_const.class)
      ((AST.int_const)expr).type = "Int";
    else if(expr.getClass() == AST.string_const.class)
      ((AST.string_const)expr).type = "String";
    // object
    else if(expr.getClass() == AST.object.class) {
      AST.object curExp = (AST.object)expr;
      if(st.lookUpGlobal(curExp.name) == null) {
          ErrorReporter.reportError(filename, curExp.lineNo, " Identifier - '" + curExp.name + "' is not declared");
          // assign Object
          curExp.type = "Object";
      } else curExp.type = st.lookUpGlobal(curExp.name).typeid;
    }
    // complement
    else if(expr.getClass() == AST.comp.class) {
      AST.comp curExp = (AST.comp)expr;
      traverseNode(curExp.e1);
      if(curExp.e1.type.equals("Int") == false) {
            ErrorReporter.reportError(filename, curExp.lineNo, "Compliment cannot be applied on type '" + curExp.e1.type);
        }
      // complement of int is still of type int.
      curExp.type = "Int";
    }
    else if(expr.getClass() == AST.eq.class) {
      AST.eq curExp = (AST.eq)expr;
      // assign the types
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);
      if((curExp.e1.type.equals("Bool") || curExp.e1.type.equals("Int") || curExp.e1.type.equals("String"))) {
        if(curExp.e1.type.equals(curExp.e2.type) == false) {
            ErrorReporter.reportError(filename, curExp.lineNo, "LHS type - '" + curExp.e1.type + "' is not equal to RHS type - '" + curExp.e2.type + "'");
        }
      }
      // return true or false, so boolean
      curExp.type = "Bool";
    }
    else if(expr.getClass() == AST.leq.class) {
      AST.leq curExp = (AST.leq)expr;
      // assign the types
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);

      if(curExp.e1.type.equals("Int") == false ) {
        ErrorReporter.reportError(filename, curExp.lineNo, "LHS type '" + curExp.e1.type + "' is not equal to Int");
      } else if(curExp.e2.type.equals("Int") == false) {
        ErrorReporter.reportError(filename, curExp.lineNo, "RHS type '" + curExp.e2.type + "' is not equal to Int");
      }
      // return true or false, so boolean
      curExp.type = "Bool";
    }
    else if(expr.getClass() == AST.lt.class) {
      AST.lt curExp = (AST.lt)expr;
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);

      if(curExp.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, curExp.lineNo, "LHS type '" + curExp.e1.type + "' is not equal to Int");
      } else if(curExp.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "RHS type '" + curExp.e2.type + "' is not equal to Int");
      }
      // return true or false, so boolean
      curExp.type = "Bool";
    }
    else if(expr.getClass() == AST.neg.class) {
      AST.neg curExp = (AST.neg)expr;
      // assign the types
      traverseNode(curExp.e1);
      if(curExp.e1.type.equals("Bool") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Negation cannot be applied on type '" + curExp.e1.type + "'");
      }
      // return negation of true or false, so boolean
      curExp.type = "Bool";
    }
    else if(expr.getClass() == AST.divide.class) {
      AST.divide curExp = (AST.divide)expr;
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);
      if(curExp.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Numerator has to be of type Int and not '" + curExp.e1.type + "'");
      }
      if(curExp.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Denominator has to be of type Int and not '" + curExp.e2.type + "'");
      }
      curExp.type = "Int";
    }
    else if(expr.getClass() == AST.mul.class) {
      AST.mul curExp = (AST.mul)expr;
      // assign the types
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);
      if(curExp.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, curExp.lineNo, "First Operand has to be of type Int and not '" + curExp.e1.type + "'");
      }
      if(curExp.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Second Operand has to be of type Int and not '" + curExp.e2.type + "'");
      }
      curExp.type = "Int";
    }
    else if(expr.getClass() == AST.sub.class) {
      AST.sub curExp = (AST.sub)expr;
      // assign the types
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);
      if(curExp.e1.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "First Operand has to be of type Int and not '" + curExp.e1.type + "'");
      }
      if(curExp.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Second Operand has to be of type Int and not '" + curExp.e2.type + "'");
      }
      curExp.type = "Int";
    }
    else if(expr.getClass() == AST.plus.class) {
      AST.plus curExp = (AST.plus)expr;
      // assign the types
      traverseNode(curExp.e1);
      traverseNode(curExp.e2);
      if(curExp.e1.type.equals("Int") == false ) {
          ErrorReporter.reportError(filename, curExp.lineNo, "First Operand has to be of type Int and not '" + curExp.e1.type + "'");
      }
      if(curExp.e2.type.equals("Int") == false) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Second Operand has to be of type Int and not '" + curExp.e2.type + "'");
      }
      curExp.type = "Int";
    }
    else if(expr.getClass() == AST.isvoid.class) {
      AST.isvoid curExp = (AST.isvoid)expr;
      curExp.type = "Bool";
    }
    else if(expr.getClass() == AST.new_.class) {
      AST.new_ curExp = (AST.new_)expr;
      // get class info
      if(CT.cnHm.get(curExp.typeid) != null)
        curExp.type = curExp.typeid;
      else {
        ErrorReporter.reportError(filename, curExp.lineNo, "Cannot call 'new' on type '" + curExp.typeid +"'");
        curExp.type = "Object";
      }
    }
    else if(expr.getClass() == AST.assign.class) {
      AST.assign curExp = (AST.assign)expr;
      AST.attr attr = st.lookUpGlobal(curExp.name);
      traverseNode(curExp.e1);
      if(attr == null) {
          ErrorReporter.reportError(filename, curExp.lineNo, "Variable'" + curExp.name + "' is not declared");
      } else if(CT.typeCheck(curExp.e1.type, attr.typeid) == false) { //Evaluted expression's type
          ErrorReporter.reportError(filename, curExp.lineNo, "The type '" + attr.typeid + "' of identifier '" + attr.name + "' does not match with the type '" + curExp.e1.type + "' of the expression.");
          ErrorReporter.reportError(filename, curExp.lineNo, "RHS type'" + curExp.e1.type + " is not equal to declared - " + attr.name + "' type " + attr.typeid);
      }
      curExp.type = curExp.e1.type;
    }
    else if(expr.getClass() == AST.block.class) {
      AST.block curExp = (AST.block)expr;
      for(int i = 0; i < curExp.l1.size(); i++) {
        traverseNode(curExp.l1.get(i));
      }
      // get last expression's type
      curExp.type = curExp.l1.get(curExp.l1.size() - 1).type;
    }
    else if(expr.getClass() == AST.loop.class) {
      AST.loop curExp = (AST.loop)expr;
      traverseNode(curExp.predicate);
      if(curExp.predicate.type.equals("Bool") == false) {
          ErrorReporter.reportError(filename, curExp.predicate.lineNo, "Loop Condition has to evalute to a Bool");
      }
      traverseNode(curExp.body);
      curExp.type = "Object";
    }
    else if(expr.getClass() == AST.cond.class) {
      AST.cond curExp = (AST.cond)expr;
        traverseNode(curExp.predicate);
        traverseNode(curExp.ifbody);
        traverseNode(curExp.elsebody);
        if(curExp.predicate.type.equals("Bool") == false) {
            ErrorReporter.reportError(filename, curExp.predicate.lineNo, "Predicate return type has to be of type Bool");
        }
        // ********
        // The common ancestor class of ifbody expression and elsebody expression is assigned to 'cond' type
        curExp.type = CT.commAncestor(curExp.ifbody.type, curExp.elsebody.type);
    }
    // let ID : TYPEID [ <- expression ]
    else if(expr.getClass() == AST.let.class) {
      AST.let curExp = (AST.let)expr;
      if(curExp.value.getClass() != AST.no_expr.class) {
        traverseNode(curExp.value);
        if(CT.typeCheck(curExp.value.type, curExp.typeid) == false) {
            ErrorReporter.reportError(filename, curExp.lineNo, "'Let' declared type - '" + curExp.value.type + " is not equal to '" + curExp.name + "' type '" + curExp.typeid);
        }
      }
      traverseNode(curExp.body);
      st.enterScope();
      st.insert(curExp.name, new AST.attr(curExp.name, curExp.typeid, curExp.value, curExp.lineNo));
      st.exitScope();
      curExp.type = curExp.body.type;
    }
    // handling the types of method calls

    // type #1
    // <expr>.<id>(<expr>,....)
    else if(expr.getClass() == AST.dispatch.class) {
        AST.dispatch curExp= (AST.dispatch)expr;
        // assume method does not exist
        boolean methodExists = false;

        // Checking the validity of the caller
        traverseNode(curExp.caller);

        // Checking the validity of the arguments, i.e. expression list
        for(AST.expression exp : curExp.actuals) {
            traverseNode(exp);
        }
        // get the classNode of the caller from the calssNode hashmap
        ClassNode callerClass = CT.cnHm.get(curExp.caller.type);
        if(callerClass == null) {
            // if does not exist then report error
            ErrorReporter.reportError(filename, curExp.lineNo, "The class '" + curExp.caller.type + "' is undefined in this context.");
        } else {
            // check if the method is actually present in the class
            if(callerClass.methods.containsKey(curExp.name)) {
                methodExists = true;
                // check if required number of arguments and given number of arguments are same                
                if(curExp.actuals.size() != callerClass.methods.get(curExp.name).formals.size()) {
                    // If the number of arguments is different then report error
                    ErrorReporter.reportError(filename, curExp.lineNo, "Number of arguments in method("+ curExp.actuals.size() +") '" + callerClass.methods.get(curExp.name).name + "' not same as expected"+ callerClass.methods.get(curExp.name).formals.size() +"from defined method.");
                } else {
                    // If number of arguments provided is same then we must check for the types of these arguments
                    for(int i=0; i<curExp.actuals.size(); i++) {
                        if(CT.typeCheck(curExp.actuals.get(i).type, callerClass.methods.get(curExp.name).formals.get(i).typeid) == false) {
                            // if the types do not match report error
                          AST.method temp = callerClass.methods.get(curExp.name);
                            ErrorReporter.reportError(filename, curExp.lineNo, "Given('" + curExp.actuals.get(i).type + "') method type differs from expected type('" + temp.formals.get(i).typeid + "') in '" + temp.name + "'.");
                        }
                    }
                }
            } else {
                // Means the method does not belong to the class
                ErrorReporter.reportError(filename, curExp.lineNo, "Method '" + curExp.name + "' is not present in class " + curExp.caller.type + ".");
            }
        }

        if(methodExists) {
            curExp.type = callerClass.methods.get(curExp.name).typeid;
        } else {
          // this sets the type to default Object, this may lead to some confusing error messages since
          // an arbitrary expression may be assumed to be of class object if containing any error
            curExp.type = "Object";
        }
    }
    // type #2 of method calls
    // * <expr>@<type>.<id> (<expr>,....)
    else if(expr.getClass() == AST.static_dispatch.class) {
      AST.static_dispatch curExp = (AST.static_dispatch)expr;
        boolean methodExists = false;

        // Checking the validity of the caller
        traverseNode(curExp.caller);

        // get the classNode of the caller from the calssNode hashmap
        ClassNode callerClass = CT.cnHm.get(curExp.typeid);

        for(AST.expression exp : curExp.actuals) {
            // Checking the validity of the arguments, i.e. expression list
            traverseNode(exp);
        }

        if(callerClass == null) {
            // no class returned
            ErrorReporter.reportError(filename, curExp.lineNo, "The class '" + curExp.typeid + "' is undefined in the context of this dispatch.");
        } else if(CT.typeCheck(curExp.caller.type, callerClass.name) == false) {
            // the name of the class does not match the type of the dispatch expression
            ErrorReporter.reportError(filename, curExp.lineNo, "The expected class of the method in the static dispatch is '" + callerClass.name + "' which is different from the expression type '" + curExp.caller.type + "'.");
        } else {
            if(callerClass.methods.containsKey(curExp.name)) {
                // the method called is present in the class mentioned in the static dispatch
                methodExists = true;

                // Comparing the required number of arguments to the given number of arguments
                if(curExp.actuals.size() != callerClass.methods.get(curExp.name).formals.size()) {
                    // Different number of parameters in the called static dispatch
                    ErrorReporter.reportError(filename, curExp.lineNo, "Number of arguments in method("+ curExp.actuals.size() +") '" + callerClass.methods.get(curExp.name).name + "' not same as expected"+ callerClass.methods.get(curExp.name).formals.size() +"from defined method.");
                } else {
                    // If the number of arguments match, we must check if the arguments are of the same type
                    for(int i=0; i<curExp.actuals.size(); i++) {
                        if(CT.typeCheck(curExp.actuals.get(i).type, callerClass.methods.get(curExp.name).formals.get(i).typeid) == false) {
                            // Means the type does not match
                            AST.method temp = callerClass.methods.get(curExp.name);
                            ErrorReporter.reportError(filename, curExp.lineNo, "Given('" + curExp.actuals.get(i).type + "') method type differs from expected type('" + temp.formals.get(i).typeid + "') in '" + temp.name + "'.");
                        }
                    }
                }
            } else {
                // Means the method is not defined in any class
                ErrorReporter.reportError(filename, curExp.lineNo, "Method '" + curExp.name + "' is not present in the class" + callerClass.name+ ".");
            }
        }

        if(methodExists) {
            curExp.type = callerClass.methods.get(curExp.name).typeid;
        } else {
          // this sets the type to default Object, this may lead to some confusing error messages since
          // an arbitrary expression may be assumed to be of class object if containing any error
            curExp.type = "Object";
        }
    }
    else if(expr.getClass() == AST.typcase.class) {
      AST.typcase curExp = (AST.typcase)expr;
        traverseNode(curExp.predicate);
        for(AST.branch br : curExp.branches) {
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
        AST.branch brn = curExp.branches.get(0);
        String brType = brn.value.type;

        for(AST.branch br : curExp.branches) {
            if(brnchMap.containsKey(br.type) == false) {
                brnchMap.put(br.type, true);
            } else {
                ErrorReporter.reportError(filename, br.lineNo, "Another branch has same type '" + br.type + "'.");
            }
            brType = CT.commAncestor(brType, br.value.type);
        }

        // Updating the type of typcase with last branch type
        curExp.type = brType;
    }
  }
}
