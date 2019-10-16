(*
  Attribute redeclaration not allowed
  Method redeclaration not allowed
  Use of undeclared variables
  *)

Class A {
  a : Int;
  a : String; -- attribute a is declared twice, so error.
  ret: Int;

  redeclare() : Int { -- this method is declared twice
    {
      ret1 <- 1; -- ret1 is not declared.
    }
  };

  redeclare(x : Int, s : String) : Int {
    {
      ret <- 1;
    }
  };

};
