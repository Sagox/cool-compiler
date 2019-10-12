(*
  Incorrect argument type
  Incorrect return type
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

  redeclare(x : Int, s : String) : Int { -- Child's method argument x's type is different in Parent class method.
    {
      ret1 <- "bla";
    }
  };

};
