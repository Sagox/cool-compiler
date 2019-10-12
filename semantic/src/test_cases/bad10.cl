(*
  Method parameter redeclare
  *)

Class A {
  a : Int;
  ret: Int;

  redeclare(x: String, s: String, x: Int) : Int { -- x has been declared twice in the method
    {
      ret <- 1;
    }
  };

};
