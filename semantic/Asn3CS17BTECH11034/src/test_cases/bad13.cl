(*
  When assignment is being done, LHS and RHS must have same types
  *)

class A {
  a : Int;
  b : String;
};

class B {
  a : A <- new A;
  b : Int;
  wrongReturn(x: Int) : Int { -- If immediate type is not known we try to compare with their parents wherever needed until base Class is reached.
    let x : B <- a in -- Since, class A and class B have no relation we cannot assign class A variable to class B.
      {
        b <- 5;
      }
  };
};
