(*
  Incorrect arguments size
  Incorrect return type
  *)

Class A {
  a : Int;
  ret: Int;

  redeclare() : Int {
    {
      ret <- 1;
    }
  };

};

Class B inherits A {
  ret1 : String;

  redeclare(x : Int, s : String) : String {
    {
      ret1 <- "bla";
    }
  };

};
