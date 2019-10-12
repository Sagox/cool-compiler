(*
  Check that add, sub, div, mult should have Int type operands only.
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
