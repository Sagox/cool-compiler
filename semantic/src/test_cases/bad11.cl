(*
  Complement and negation
  Same type checking for both sides of '<', '<=' operator
  Checking where 'new' can be applied.
  Undefined method
  *)

Class A {
  a : Int;
  b : Bool;
  ret: Int;

  redeclare(x: String, y: Int) : Int {
    {
      ret <- "bla";
      if y < "x" then a <- 5 else 0
      fi;
      if y <= "x" then a <- 5 else 0
      fi;
      ret <- ~x;
    }
  };

  redeclare1(x: String) : Bool {
    {
      b <- not x;
    }
  };

};

class Main inherits IO {
  main() : Object {
    {
      (new LazyPerson).init1();
    }
  } ;
} ;
