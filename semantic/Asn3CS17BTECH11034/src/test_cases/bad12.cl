(*
  let typeid must be equal to its value's type
  Predicates in loop/if must evaluate to a Bool
  *)

Class A {
  a : Int;
  b : Bool;
  ret: Int;

  redeclare(x: String, y: Int) : Int {
    let cur_x : String <- y in -- let has typeid String but is being assigned an Int, which is not legal.
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
