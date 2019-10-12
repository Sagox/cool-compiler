(*
  Complement cannot be applied on non-Int type
  Negation cannot be applied on non-Bool type
  LHS and RHS of '<', '<=' operator have to be equal to Int
  'new' can be applied to declared classes only.
  Expression type should be same as identifier's type to which assignment is being done.
  *)

Class A {
  a : Int;
  b : Bool;
  ret: Int;

  redeclare(x: String, y: Int) : Int {
    {
      ret <- "bla"; -- cannot assign String to Int type identifier.
      if y < "x" then a <- 5 else 0 -- '<' should be applied on Int types (LHS and RHS)
      fi;
      if y <= "x" then a <- 5 else 0 -- '<=' should be applied on Int types (LHS and RHS)
      fi;
      ret <- ~x; -- complement can only be applied on Int type.
    }
  };

  redeclare1(x: String) : Bool {
    {
      b <- not x; -- negation can only be applied on Bool
    }
  };

};

class Main inherits IO {
  main() : Object {
    {
      (new LazyPerson).init1(); -- LazyPerson is undefined so we cant use 'new' on it,
    }
  } ;
} ;
