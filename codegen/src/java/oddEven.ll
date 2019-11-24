; I am a comment in LLVM-IR. Feel free to remove me.
; ModuleID = "oddEven.cl"
source_filename = "oddEven.cl"
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
%class_Main = type { %class_A*, %class_IO*, %class_Int*, %class_Int*, %class_Bool* }

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
	store i32 0, i32* %j
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
	%0 = alloca i1
	store i1 true, i1* %0
	%1 = load i1, i1* %0
	store i1 %1, i1* %k
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
@.str.0 = private unnamed_addr constant [35 x i8] c"Enter an positive integer value : \00"

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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
	store i8* getelementptr inbounds ([35 x i8], [35 x i8]* @.str.0, i32 0, i32 0), i8** %6	%7 = load i8*, i8** %6
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
	%18 = call %class_Int* @A_oddEven( %class_A* %17, %class_Int* %16 )
	store %class_Int* %18, %class_Int* %j.addr
	%19 = alloca i32
	store i32 0, i32* %19
	%20 = load i32, i32* %19
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
	%21 = load %class_Int, %class_Int* %retval
	ret %class_Int %21
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
	%k = getelementptr inbounds %class_Main, %class_Main* %this1, i32 0, i32 4
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
%class_A = type { %class_Int*, %class_IO* }

define %class_A* @A_Cons_A( %class_A* %this ) {
entry:
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	store i32 0, i32* %a
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	%0 = alloca %class_IO
	%1 = call %class_IO* @IO_Cons_IO( %class_IO* %0 )
	store %class_IO* %1, %class_IO* %io
	ret %class_A* %this1
}

define i32 @A_in_int( %class_A* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class_A*
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%a = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 0
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
@.str.1 = private unnamed_addr constant [22 x i8] c"Divisble by 2 - even\0A\00"
@.str.2 = private unnamed_addr constant [25 x i8] c"Not Divisble by 2 - odd\0A\00"

define %class_Int @A_oddEven( %class_A* %this, %class_Int* %a ) {
entry:
	%retval = alloca %class_Int
	%this.addr = alloca %class_A*
	%a.addr = alloca %class_Int*
	store %class_Int* %a, %class_Int* %a.addr
	store %class_A* %this, %class_A** %this.addr
	%this1 = load %class_A*, %class_A** %this.addr
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
	br label %for.cond0


for.cond0:
	%0 = alloca i32
	store i32 1, i32* %0
	%1 = load i32, i32* %0
	%2 = load %class_Int*, %class_Int* %a.addr
	%3 = icmp slt i32 %1, %2
	br i1 %3, label %for.body0, label %for.end0

for.body0:
	%4 = load %class_Int*, %class_Int* %a.addr
	%5 = alloca i32
	store i32 2, i32* %5
	%6 = load i32, i32* %5
	i32 = sub i32 %4, %6
	store i32 %7, i32* %a.addr
	br label %for.cond0


for.end0:
	%8 = load %class_Int*, %class_Int* %a.addr
	%9 = alloca i32
	store i32 0, i32* %9
	%10 = load i32, i32* %9
	br i1 %11, label %if.then0, label %if.else0

if.then0:
	%12 = load %class_IO*, %class_IO* %io.addr
	%13 = icmp eq class_IO null, %12
	br i1 %13, label %dispatch_on_void_basic_block, label %branch_13

proceed_13:
	%14 = alloca i8*
	store i8* getelementptr inbounds ([22 x i8], [22 x i8]* @.str.1, i32 0, i32 0), i8** %14	%15 = load i8*, i8** %14
	%16 = load %class_IO*, %class_IO* %io.addr
	%17 = call %class_IO* @IO_out_string( i8* %15 )
	br label %if.end0


if.else0:
	%18 = load %class_IO*, %class_IO* %io.addr
	%19 = icmp eq class_IO null, %18
	br i1 %19, label %dispatch_on_void_basic_block, label %branch_19

proceed_19:
	%20 = alloca i8*
	store i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str.2, i32 0, i32 0), i8** %20	%21 = load i8*, i8** %20
	%22 = load %class_IO*, %class_IO* %io.addr
	%23 = call %class_IO* @IO_out_string( i8* %21 )
	br label %if.end0


if.end0:
	%24 = phi class_IO [ %17, branch_14: ],  [ %23, branch_20: ]
	%25 = alloca i32
	store i32 0, i32* %25
	%26 = load i32, i32* %25
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
	%27 = load %class_Int, %class_Int* %retval
	ret %class_Int %27
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
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
	%io = getelementptr inbounds %class_A, %class_A* %this1, i32 0, i32 1
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
