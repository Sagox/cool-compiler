(*
  Check add, sub, div, mult
  *)

class A {
  foo(a : Int, b : String) : Int {
    {
      -- LHS and RHS of operator has to be of type Int.
      a <- a + b;
      a <- a - b;
      a <- a * b;
      a <- a / b;
    }
  };
};
