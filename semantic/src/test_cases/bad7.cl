(*
  Attribute & method redeclare
  undeclared use
  *)

Class A {
  a : Int;
  a : String;
  ret: Int;

  redeclare() : Int {
    {
      ret1 <- 1;
    }
  };

  redeclare(x : Int, s : String) : Int {
    {
      ret <- 1;
    }
  };

};
