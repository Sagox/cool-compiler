class Main inherits IO {
    i : Int;
    j : Int;
    k : Bool <- true;
    io : IO;
    a : A;
    main() : Int {
        {
            io <- new IO;
            a <- new A;
            io@IO.out_string("Enter an positive integer value : ");
            i <- io@IO.in_int();
            j <- a@A.oddEven(i);
            0;
        }
    };
};
class A inherits IO {
    a : Int;
    io : IO <- new IO;
    oddEven(a : Int) : Int {
        {
            while 1 < a loop a <- a - 2 pool;
            if a = 0 then io@IO.out_string("Divisble by 2 - even\n") else io@IO.out_string("Not Divisble by 2 - odd\n") fi;
            0;
        }
    };
};
