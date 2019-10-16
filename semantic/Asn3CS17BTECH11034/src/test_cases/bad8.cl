(*
  Incorrect arguments size of child class w.r.t. Parent Class.
  Incorrect return type of child class w.r.t. Parent Class.
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

  redeclare(x : Int, s : String) : String { -- child's method has more number of parameters than what is defined in parent
  -- child's method return type is different from parent's method.
    {
      ret1 <- "bla";
    }
  };

};
