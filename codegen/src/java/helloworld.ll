; I am a comment in LLVM-IR. Feel free to remove me.
; ModuleID = "helloworld.cl"
source_filename = "helloworld.cl"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

define i8* @strcpy( i8*, i8* )


define i8* @strcmp( i8*, i8* )


define i8* @strncpy( i8*, i8*, i32 )


define i8* @strlen( i8* )


define i32 @printf( i8*, ... )


define i32 @scanf( i8*, ... )


define i8* @malloc( i32 )


define void @exit( i32 )

