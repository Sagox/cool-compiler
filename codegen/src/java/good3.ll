; I am a comment in LLVM-IR. Feel free to remove me.
; ModuleID = "good3.cl"
source_filename = "good3.cl"
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
	call void @Main_main( %class_Main* %obj1 )
	ret i32 0
}
%class_Main = type {  }

define %class_Main* @Main_Cons_Main( %class_Main* %this ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
	ret %class_Main* %this1
}

define i32 @Main_in_int( %class_Main* %this ) {
entry:
	%retval = alloca i32
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
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

define %class_Int @Main_getNum( %class_Main* %this, %class_Int* %a, %class_Int* %b ) {
entry:
	%retval = alloca %class_Int
	%this.addr = alloca %class_Main*
	%a.addr = alloca %class_Int*
	%b.addr = alloca %class_Int*
	store %class_Int* %a, %class_Int* %a.addr
	store %class_Int* %b, %class_Int* %b.addr
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
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
	%0 = load %class_Int, %class_Int* %retval
	ret %class_Int %0
}

define void @Main_main( %class_Main* %this ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
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

define %class_IO @Main_out_string( %class_Main* %this, i8* %out_string ) {
entry:
	%retval = alloca %class_IO
	%this.addr = alloca %class_Main*
	%out_string.addr = alloca i8*
	store i8* %out_string, i8** %out_string.addr
	store %class_Main* %this, %class_Main** %this.addr
	%this1 = load %class_Main*, %class_Main** %this.addr
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
