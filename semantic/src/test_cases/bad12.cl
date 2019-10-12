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
    let cur_x : String <- y in -- let's value if of type String but is being assigned an Int, which should not happen.
    {
      if y <- 18 then a <- 5 else 0 fi; -- Here predicate return is not Bool, so report.
      while (cur_x) loop -- Loop predicate must be of type Bool.
        {
          ret <- 5;
        }
      pool;
    }

  };

};
