(*
  Switch case
*)
class A {
    a : Int;
    c : String;
    d : String;
    foo() : Object { {
        case c of
                a : A => a <- 1;
                b : B => b <- 1;
                d : A => d <- "abc";
            esac;
    } };
};
