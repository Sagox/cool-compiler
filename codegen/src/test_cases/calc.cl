class Main inherits IO {
    i : Int;
    j : Int <- 5;
    io : IO;
    a : A;
    main() : Int {
        {
            io <- new IO;
            a <- new A;
            io@IO.out_string("Enter an integer value : ");
            i <- io@IO.in_int();
            i <- a@A.neg(i);
            io@IO.out_string("Its negation is : ");
            io@IO.out_int(i);
            0;
        }
    };
};
class A inherits IO {
    a : Int;
    b : Bool;
    neg(a : Int) : Int {
        {
            a <- ~a;
            a;
        }
    };
};
