(*
  Incorrect argument type of child class w.r.t. Parent Class.
  Incorrect return type of method
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
      ret1 <- "bla"; -- actual return is String but declared is Int, not allowed.
    }
  };

};
