(*
  Incorrect argument type
  Wrong return type
  *)

Class A {
  a : Int;
  ret: Int;

  redeclare(x: String, s: String) : Int {
    {
      ret <- 1;
    }
  };

};

Class B inherits A {
  ret1 : String;

  redeclare(x : Int, s : String) : Int {
    {
      ret1 <- "bla";
    }
  };

};
