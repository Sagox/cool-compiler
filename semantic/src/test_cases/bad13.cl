(*
  Check class types
  *)

class A {
  a : Int;
  b : String;
};

class B {
  a : A <- new A;
  b : Int;
  wrongReturn(x: Int) : Int {
    let x : B <- a in
      {
        b <- 5;
      }
  };
};
