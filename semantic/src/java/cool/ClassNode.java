package cool;

import java.util.*;

public class ClassNode {
  String name;
  String parent = null;
  HashMap<String, AST.attr> attrs;
  HashMap<String, AST.method> methods;

  ClassNode(String clName, String paName, HashMap<String, AST.attr> attrPass, HashMap<String, AST.method> methodsPass) {
    name = clName;
    if (paName != null)
      parent = paName;
    attrs = new HashMap<String, AST.attr>();
    attrs.putAll(attrPass);
    methods = new HashMap<String, AST.method>();
    methods.putAll(methodsPass);
  }
}
