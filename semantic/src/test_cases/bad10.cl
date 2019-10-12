(*
  Method parameter redeclaration is not allowed.
  *)

Class A {
  a : Int;
  ret: Int;

  redeclare(x: String, s: String, x: Int, s: String) : Int { -- x has been declared twice in the method
    {
      ret <- 1;
    }
  };

};
