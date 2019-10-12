package cool;

import java.util.*;

public class ClassTable {

  public HashMap<String, ClassNode> cnHm = new HashMap<String, ClassNode>();
  public HashMap<String, Integer> classScope = new HashMap<String, Integer>();

  ClassTable(AST.program program) {
        /*
        Object:
           abort()
           copy()
           type_name()
        IO:
           out_string(x : String) : IO
           out_int(x : Int) : IO
           in_string() : String
           in_int() : Int
        String:
           length() : Int
           concat(s : String) : String
           substr(i : Int, l : Int) : String
        */
        // add object's methods
        HashMap<String, AST.method> objectHm = new HashMap<String, AST.method>();
        objectHm.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objectHm.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objectHm.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        cnHm.put("Object", new ClassNode("Object", null, new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        cnHm.get("Object").methods.putAll(objectHm);
        classScope.put("Object", 0);

        // add IO methods
        HashMap<String, AST.method> ioHm = new HashMap<String, AST.method>();
        List<AST.formal> io_string = new ArrayList<AST.formal>();
        io_string.add(new AST.formal("out_string", "String", 0));
        List<AST.formal> io_int = new ArrayList<AST.formal>();
        io_int.add(new AST.formal("out_int", "Int", 0));
        ioHm.put("out_string", new AST.method("out_string", io_string, "IO", new AST.no_expr(0), 0));
        ioHm.put("out_int", new AST.method("out_int", io_int, "IO", new AST.no_expr(0), 0));
        ioHm.put("in_string", new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        ioHm.put("in_int", new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        cnHm.put("IO", new ClassNode("IO", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        cnHm.get("IO").methods.putAll(ioHm);
        // IO inherits Object
        cnHm.get("IO").methods.putAll(objectHm);
        classScope.put("IO", 1);

        cnHm.put("Bool", new ClassNode("Bool", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        // Since Bool class inherits from Object class
        cnHm.get("Bool").methods.putAll(objectHm);
        classScope.put("Bool", 1);

        cnHm.put("Int", new ClassNode("Int", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        // Int inherits Object
        cnHm.get("Int").methods.putAll(objectHm);
        classScope.put("Int", 1);

        HashMap<String, AST.method> strHm = new HashMap<String, AST.method>();
        ArrayList<AST.formal> concat = new ArrayList<AST.formal>();
        concat.add(new AST.formal("s", "String", 0));
        ArrayList<AST.formal> substr = new ArrayList<AST.formal>();
        substr.add(new AST.formal("i", "Int", 0));
        substr.add(new AST.formal("l", "Int", 0));
        strHm.put("length", new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
        strHm.put("concat", new AST.method("concat", concat, "String", new AST.no_expr(0), 0));
        strHm.put("substr", new AST.method("substr", substr, "String", new AST.no_expr(0), 0));
        cnHm.put("String", new ClassNode("String", "Object", new HashMap<String, AST.attr>(), new HashMap<String, AST.method>()));
        cnHm.get("String").methods.putAll(strHm);
        // String inherits Object
        cnHm.get("String").methods.putAll(objectHm);
        classScope.put("String", 1);
  }

  public void insert(AST.class_ currClass) {
    // inherit parent attr and methods
    ClassNode currNode = new ClassNode(currClass.name, currClass.parent, new HashMap<String, AST.attr>(), new HashMap<String, AST.method>());
    currNode.attrs.putAll(cnHm.get(currClass.parent).attrs);
    currNode.methods.putAll(cnHm.get(currClass.parent).methods);
    // check multiple definitions of attributes and methods before adding
    HashMap<String, AST.attr> currAttrs = new HashMap<String, AST.attr>();
    HashMap<String, AST.method> currMethods = new HashMap<String, AST.method>();
    for(int i = 0; i < currClass.features.size(); i++) {
      if(currClass.features.get(i).getClass() == AST.method.class) {
          AST.method currMethod = (AST.method) currClass.features.get(i);
          if(currMethods.containsKey(currMethod.name))
            ErrorReporter.reportError(currClass.filename, currMethod.lineNo, ": Method - '" + currMethod.name + "' is redefined.");
          else currMethods.put(currMethod.name, currMethod);
      } else if(currClass.features.get(i).getClass() == AST.attr.class) {
          AST.attr currAttr = (AST.attr) currClass.features.get(i);
          if(currAttrs.containsKey(currAttr.name))
            ErrorReporter.reportError(currClass.filename, currAttr.lineNo, ": Attribute - '" + currAttr.name + "' is redefined");
          else currAttrs.put(currAttr.name, currAttr);
      }
    }
    // inherited re-definitions
    // method
    for(String currMethodName : currMethods.keySet()) {
      if(currNode.methods.containsKey(currMethodName)) {
        AST.method currMethod = currMethods.get(currMethodName);
        AST.method parentMethod = currNode.methods.get(currMethodName);
        // number of parameters
        if(currMethod.formals.size() != parentMethod.formals.size()) {
          // check
          ErrorReporter.reportError(currClass.filename, currMethod.lineNo, "Incorrect number of arguments in child class's method - '" + currMethod.name + "'");
          continue;
        }
        // same number of parameters so check all parameters
        boolean flag = true;
        for(int i = 0; i < currMethod.formals.size(); i++) {
          if(currMethod.formals.get(i).typeid.equals(parentMethod.formals.get(i).typeid) == false) {
            // check
            ErrorReporter.reportError(currClass.filename, currMethod.lineNo, "Child class's method - '" + currMethod.name + "' parameter type"
                + currMethod.formals.get(i).typeid + " is not equal to parent's parameter type '" + parentMethod.formals.get(i).typeid + "'");
            flag = false;
          }
        }
        if (flag == false)
          continue;
        // check return type
        if(currMethod.typeid.equals(parentMethod.typeid) == false) {
          // check
          ErrorReporter.reportError(currClass.filename, currMethod.lineNo, "Child class's method - '" + currMethod.name + "' return type '"
              + currMethod.typeid + "' is not equal to parent's return type '" + parentMethod.typeid + "'");
          continue;
        }
      }
      currNode.methods.put(currMethodName, currMethods.get(currMethodName));
    }
    // attr
    for(String currAttrName : currAttrs.keySet()) {
      if(currNode.attrs.containsKey(currAttrName))
        // check
        ErrorReporter.reportError(currClass.filename, currAttrs.get(currAttrName).lineNo, "Attribute - '" + currAttrs.get(currAttrName).name + "' is redefined");
      else currNode.attrs.put(currAttrName, currAttrs.get(currAttrName));
    }
    // now add this node
    cnHm.put(currNode.name, currNode);
    classScope.put(currNode.name, classScope.get(currNode.parent) + 1);
  }

  public boolean typeCheck(String cl1, String cl2) {
    while(cl1 != null || cl2 != null) {
      if (cl1.equals(cl2))
        return true;
      cl1 = cnHm.get(cl1).parent;
    }
    return false;
  }

  public String commAncestor(String cl1, String cl2) {
    if (cl1.equals(cl2))
      return cl1;
    if (classScope.get(cl1) < classScope.get(cl2)) {
      return commAncestor(cl1, cnHm.get(cl2).parent);
    } else {
      return commAncestor(cnHm.get(cl1).parent, cl2);
    }
  }
}
