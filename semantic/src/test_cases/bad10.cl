(*
  Method parameter redeclare
  *)

Class A {
  a : Int;
  ret: Int;

  redeclare(x: String, s: String, x: Int) : Int {
    {
      ret <- 1;
    }
  };

};
