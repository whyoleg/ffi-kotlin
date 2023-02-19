package dev.whyoleg.ffi

public expect class CPointer<T : CPointed>

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CPointerVariable<T : CPointed> : CVariable

public typealias CArrayPointer<T> = CPointer<T>
public typealias CArrayPointerVariable<T> = CPointerVariable<T>


public expect val <T : CPointed> T.pointer: CPointer<T>
public expect val <T : CPointed> CPointer<T>.pointed: T
