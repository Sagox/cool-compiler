(*
  let
  conditional
  block
  *)

Class A {
  a : Int;
  b : Bool;
  ret: Int;

  redeclare(x: String, y: Int) : Int {
    let cur_x : String <- y in
    {
      if y <- 18 then a <- 5 else 0 fi; -- Here predicate return is not Bool, so report.
      while (cur_x) loop
        {
          ret <- 5;
        }
      pool;
    }

  };

};
