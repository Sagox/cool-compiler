(*
  Check add, sub, div, mult
  *)

class A {
  foo(a : Int, b : String) : Int {
    {
      a <- a + b;
      a <- a - b;
      a <- a * b;
      a <- a / b;
    }
  };
};
