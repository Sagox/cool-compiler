; I am a comment in LLVM-IR. Feel free to remove me.
; ModuleID = "helloworld.cl"
source_filename = "helloworld.cl"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"
declare i8* @strcpy( i8*, i8* )

declare i8* @strcmp( i8*, i8* )

declare i8* @strncpy( i8*, i8*, i32 )

declare i8* @strlen( i8* )

declare i32 @printf( i8*, ... )

declare i32 @scanf( i8*, ... )

declare i8* @malloc( i32 )

declare vogt @exit( i32 )


define i32 @main(  ) {
entry:
	%Main_obj = alloca %class_Main
	%obj1 = call %class_Main* @Main_Cons_Main( %class_Main %Main_obj )
	%0 = call %class_IO @Main_main( %class_Main* %obj1 )
	ret i32 %0
}
%class.Main = type {  }

define %class_Main* @Main_Cons_Main( %class_Main* %this,  ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%class_Main* = load %class_Main*, %class_Main** %this.addr
	ret %class_Main* %this1
}

define vogt @Main_abort( %class_Main* %this,  ) {
entry:
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%class_Main* = load %class_Main*, %class_Main** %this.addr
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	i8* = load i8*, i8** %err_msg_void_dispatch
	call  @IO_out_string( i8* %print_err_msg_void_dispatch )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_abort:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	i8* = load i8*, i8** %err_msg
	call  @IO_out_string( i8* %print_err_msg )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	ret void
}

define i8* @Main_type_name( %class_Main* %this,  ) {
entry:
	%%retval = alloca i8*
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%class_Main* = load %class_Main*, %class_Main** %this.addr
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	i8* = load i8*, i8** %err_msg_void_dispatch
	call  @IO_out_string( i8* %print_err_msg_void_dispatch )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_abort:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	i8* = load i8*, i8** %err_msg
	call  @IO_out_string( i8* %print_err_msg )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	i8* = load i8*, i8** %retval
	ret i8* %0
}

define %class_IO @Main_main( %class_Main* %this,  ) {
entry:
	%%retval = alloca %class_IO
	%this.addr = alloca %class_Main*
	store %class_Main* %this, %class_Main** %this.addr
	%class_Main* = load %class_Main*, %class_Main** %this.addr
	br label %fun_returning_basic_block

dispatch_on_void_basic_block:
	%err_msg_void_dispatch = alloca i8*
	store i8* getelementptr inbounds ([47 x i8], [47 x i8]* @staticdispatchonvoiderr, i32 0, i32 0), i8** %err_msg_void_dispatch
	i8* = load i8*, i8** %err_msg_void_dispatch
	call  @IO_out_string( i8* %print_err_msg_void_dispatch )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

func_div_by_zero_abort:
	%err_msg = alloca i8*
	store i8* getelementptr inbounds ([31 x i8], [31 x i8]* @divby0err, i32 0, i32 0), i8** %err_msg
	i8* = load i8*, i8** %err_msg
	call  @IO_out_string( i8* %print_err_msg )
	call  @Object_abort(  )
	br label %fun_returning_basic_block

fun_returning_basic_block:
	%class_IO = load %class_IO,  %retval
	ret %class_IO %0
}
