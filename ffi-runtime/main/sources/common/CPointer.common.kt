@file:Suppress("NO_ACTUAL_FOR_EXPECT")

package dev.whyoleg.ffi

//TODO: add CArrayPointer.plus/get(index)/set(index)
public typealias CArrayPointer<T> = CPointer<T>
public typealias CArrayPointerVariable<T> = CPointerVariable<T>

public expect class CPointer<T : CPointed>
public expect class CPointerVariable<T : CPointed> : CVariable

public expect var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?

public expect val <T : CPointed> T.pointer: CPointer<T>
public expect val <T : CPointed> CPointer<T>.pointed: T
