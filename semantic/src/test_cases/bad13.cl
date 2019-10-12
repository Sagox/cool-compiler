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
    let x : B <- a in -- Since, class A and class B have no relation we cannot assign class A variable to class B.
      {
        b <- 5;
      }
  };
};
