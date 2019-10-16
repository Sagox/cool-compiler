(*
  Attribute redeclaration not allowed
  Method redeclaration not allowed
  Use of undeclared variables
  *)

Class A {
  ret : Int <- 5+5;
  c : B;
  redeclare(x : Int, s : String) : B {
    {
       c;
    }
  };

};

Class C {
  ret1 : A;
};

class Main inherits IO {
  main() : Int {
    {
      5;
    }
  } ;
} ;