

(*
	Error in typcase node
*)
class Main {
    main() : Int {
        0
    };
};
class ErrorClass {
    a : Int;
    c : String;
    d : String;
    b : Int;
    rando() : IO { {
        case c of
                a : String => a;
                b : String => b;
                d : Bool => d;
            esac;
    } };
};
