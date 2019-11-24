; I am a comment in LLVM-IR. Feel free to remove me.
; ModuleID = "calc.cl"
source_filename = "calc.cl"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"
@strfmt = private unnamed_addr constant [3 x i8] c"%s\00"
@intfmt = private unnamed_addr constant [3 x i8] c"%d\00"
@.str.empty = private unnamed_addr constant [1 x i8] c"\00"

@divby0err = private unnamed_addr constant [31 x i8] c"Runtime Error: Divide by Zero\0A\00"
@staticdispatchonvoiderr = private unnamed_addr constant [47 x i8] c"Runtime Error: Static Dispatch on void object\0A\00"

declare i32 @printf( i8*, ... )

declare i32 @scanf( i8*, ... )

declare i8* @malloc( i32 )

declare void @exit( i32 )

declare i8* @strcat( i8*, i8* )

declare i32 @strcmp( i8*, i8* )

declare i8* @strcpy( i8*, i8* )

declare i32 @strlen( i8* )

declare i8* @strncpy( i8*, i8*, i32 )


define i32 @main(  ) {
entry:
	%Main_obj = alloca %class_Main
	%obj1 = call %class_Main* @Main_Cons_Main( %class_Main* %Main_obj )
	%0 = call %class_Int @Main_main( %class_Main* %obj1 )
	ret i32 0
}
%class_Main = type { %class_A*, %class_IO*, %class_Int*, %class_Int* }

define %class_Main* @Main_Cons_Main( %class_Main* %this ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	store %class_A* null , %class_A** %a
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	store %class_IO* null , %class_IO** %io
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	store i32 0, i32* %i
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	%0 = alloca i32
	store i32 5, i32* %0
	%1 = load i32, i32* %0
	store i32 %1, i32* %j
	ret %class_Main* %this1
}

define i32 @Main_in_int( %class_Main* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i32, i32* %retval
	ret i32 %0
}

define i8* @Main_type_name( %class_Main* %this ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i8*, i8** %retval
	ret i8* %0
}

define i8* @Main_in_string( %class_Main* %this ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i8*, i8** %retval
	ret i8* %0
}

define %class_IO @Main_out_int( %class_Main* %this, i32 %out_int ) {
entry:
	%retval = alloca %class_IO
	%this.addr = alloca %class_Main*
	%out_int.addr = alloca i32
	store i32 %out_int, i32* %out_int.addr
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load %class_IO, %class_IO* %retval
	ret %class_IO %0
}

define void @Main_abort( %class_Main* %this ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	ret void
}
@.str.0 = private unnamed_addr constant [26 x i8] c"Enter an integer value : \00"
@.str.1 = private unnamed_addr constant [19 x i8] c"Its negation is : \00"

define %class_Int @Main_main( %class_Main* %this ) {
entry:
	%retval = alloca %class_Int
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	%0 = alloca %class_IO
	%1 = call %class_IO* @IO_Cons_IO( %class_IO* %0 )
	store %class_IO* %1, %class_IO* %io.addr
	%2 = alloca %class_A
	%3 = call %class_A* @A_Cons_A( %class_A* %2 )
	store %class_A* %3, %class_A* %a.addr
	%4 = load %class_IO*, %class_IO* %io.addr
	%5 = icmp eq class_IO null, %4
	br i1 %5, label %dispatch_on_void_basic_block, label %branch_5

proceed_5:
	%6 = alloca i8*
	store i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.0, i32 0, i32 0), i8** %6	%7 = load i8*, i8** %6
	%8 = load %class_IO*, %class_IO* %io.addr
	%9 = call %class_IO* @IO_out_string( i8* %7 )
	%10 = load %class_IO*, %class_IO* %io.addr
	%11 = icmp eq class_IO null, %10
	br i1 %11, label %dispatch_on_void_basic_block, label %branch_11

proceed_11:
	%12 = load %class_IO*, %class_IO* %io.addr
	%13 = call i32 @IO_in_int(  )
	store i32 %13, i32* %i.addr
	%14 = load %class_A*, %class_A* %a.addr
	%15 = icmp eq class_A null, %14
	br i1 %15, label %dispatch_on_void_basic_block, label %branch_15

proceed_15:
	%16 = load %class_Int*, %class_Int* %i.addr
	%17 = load %class_A*, %class_A* %a.addr
	%18 = call %class_Int* @A_neg( %class_A* %17, %class_Int* %16 )
	store %class_Int* %18, %class_Int* %i.addr
	%19 = load %class_IO*, %class_IO* %io.addr
	%20 = icmp eq class_IO null, %19
	br i1 %20, label %dispatch_on_void_basic_block, label %branch_20

proceed_20:
	%21 = alloca i8*
	store i8* getelementptr inbounds ([19 x i8], [19 x i8]* @.str.1, i32 0, i32 0), i8** %21	%22 = load i8*, i8** %21
	%23 = load %class_IO*, %class_IO* %io.addr
	%24 = call %class_IO* @IO_out_string( i8* %22 )
	%25 = load %class_IO*, %class_IO* %io.addr
	%26 = icmp eq class_IO null, %25
	br i1 %26, label %dispatch_on_void_basic_block, label %branch_26

proceed_26:
	%27 = load %class_Int*, %class_Int* %i.addr
	%28 = load %class_IO*, %class_IO* %io.addr
	%29 = call %class_IO* @IO_out_int( %class_Int* %27 )
	%30 = alloca i32
	store i32 0, i32* %30
	%31 = load i32, i32* %30
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%32 = load %class_Int, %class_Int* %retval
	ret %class_Int %32
}

define %class_IO @Main_out_string( %class_Main* %this, i8* %out_string ) {
entry:
	%retval = alloca %class_IO
	%this.addr = alloca %class_Main*
	%out_string.addr = alloca i8*
	store i8* %out_string, i8** %out_string.addr
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	%a = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 1
	%i = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 2
	%j = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 3
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load %class_IO, %class_IO* %retval
	ret %class_IO %0
}

define i32 @String_length( i8* %this ) {
entry:
	%retval = call i32 @strlen( i8* %this )
	ret i32 %retval
}

define i8* @String_concat( i8* %this, i8* %this2 ) {
entry:
	%memnew = call i8* @malloc( i32 1024 )
	%copystring = call i8* @strcpy( i8* %memnew, i8* %this )
	%retval = call i8* @strcat( i8* %copystring, i8* %this2 )
	ret i8* %retval
}

define i8* @String_substr( i8* %this, i32 %start, i32 %len ) {
entry:
	%0 = call i8* @malloc( i32 1024 )
	%1 = getelementptr inbounds i8, i8* %this, i32 %start
	%2 = call i8* @strncpy( i8* %0, i8* %1, i32 %len )
	%3 = getelementptr inbounds [1 x i8], [1 x i8]* @.str.empty, i32 0, i32 0
	%retval = call i8* @strcat( i8* %2, i8* %3 )
	ret i8* %retval
}

define i1 @String_strcmp( i8* %this, i8* %start ) {
entry:
	%0 = call i32 @strcmp( i8* %this, i8* %start )
	%1 = icmp eq i32 %0, 0
	ret i1 %1
}

define void @Object_abort(  ) {
entry:
	call void (i32) @exit(i32 0)
	ret void
}


define void @IO_out_string( i8* %given ) {
entry:
	%0 = getelementptr inbounds [3 x i8], [3 x i8]* @strfmt, i32 0, i32 0
	%call = call i32 ( i8*, ... ) @printf(i8* %0, i8* %given)
	ret void
}


define void @IO_out_int( i32 %given ) {
entry:
	%0 = getelementptr inbounds [3 x i8], [3 x i8]* @intfmt, i32 0, i32 0
	%call = call i32 ( i8*, ... ) @printf(i8* %0, i32 %given)
	ret void
}


define i8* @IO_in_string(  ) {
entry:
	%0 = bitcast [3 x i8]* @strfmt to i8*
	%retval = call i8* @malloc( i32 1024 )
	%1 = call i32 (i8*, ...)  @scanf( i8* %0, i8* %retval )
	ret i8* %retval
}

define i32 @IO_in_int(  ) {
entry:
	%0 = bitcast [3 x i8]* @intfmt to i8*
	%1 = call i8* @malloc( i32 4 )
	%2 = bitcast i8* %1 to i32*
	%3 = call i32 (i8*, ...)  @scanf( i8* %0, i32* %2 )
	%retval = load i32, i32* %2
	ret i32 %retval
}
%class_Object = type {  }

define %class_Object* @Object_Cons_Object( %class_Object* %this ) {
entry:
	%this.addr = alloca %class_Object*
	store %class_Object* %this, %class_Object** %this.addr
	%this1 = load %class_Object*, %class_Object** %this.addr
	ret %class_Object* %this1
}
%class_IO = type {  }

define %class_IO* @IO_Cons_IO( %class_IO* %this ) {
entry:
	%this.addr = alloca %class_IO*
	store %class_IO* %this, %class_IO** %this.addr
	%this1 = load %class_IO*, %class_IO** %this.addr
	ret %class_IO* %this1
}
%class_A = type { %class_Int*, %class_Bool* }

define %class_A* @A_Cons_A( %class_A* %this ) {
entry:
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	store i32 0, i32* %a
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	store i1 false, i1* %b
	ret %class_A* %this1
}

define %class_Int @A_neg( %class_A* %this, %class_Int* %a ) {
entry:
	%retval = alloca %class_Int
	%this.addr = alloca %class_A*
	%a.addr = alloca %class_Int*
	store %class_Int* %a, %class_Int* %a.addr
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	%0 = load %class_Int*, %class_Int* %a.addr
	i32 = mul i32 %0, -1
	store i32 %1, i32* %a.addr
	%2 = load %class_Int*, %class_Int* %a.addr
	store %class_Int %2, %class_Int* %retval
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%3 = load %class_Int, %class_Int* %retval
	ret %class_Int %3
}

define i32 @A_in_int( %class_A* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i32, i32* %retval
	ret i32 %0
}

define i8* @A_type_name( %class_A* %this ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i8*, i8** %retval
	ret i8* %0
}

define i8* @A_in_string( %class_A* %this ) {
entry:
	%retval = alloca i8*
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load i8*, i8** %retval
	ret i8* %0
}

define %class_IO @A_out_int( %class_A* %this, i32 %out_int ) {
entry:
	%retval = alloca %class_IO
	%this.addr = alloca %class_A*
	%out_int.addr = alloca i32
	store i32 %out_int, i32* %out_int.addr
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load %class_IO, %class_IO* %retval
	ret %class_IO %0
}

define void @A_abort( %class_A* %this ) {
entry:
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	ret void
}

define %class_IO @A_out_string( %class_A* %this, i8* %out_string ) {
entry:
	%retval = alloca %class_IO
	%this.addr = alloca %class_A*
	%out_string.addr = alloca i8*
	store i8* %out_string, i8** %out_string.addr
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%b = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	%print_err_msg_void_dispatch = load i8*, i8** %err_msg_void_dispatch
	call void @IO_out_string( i8* %print_err_msg_void_dispatch )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_err:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	%print_err_msg = load i8*, i8** %err_msg
	call void @IO_out_string( i8* %print_err_msg )
	call void @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%0 = load %class_IO, %class_IO* %retval
	ret %class_IO %0
}
